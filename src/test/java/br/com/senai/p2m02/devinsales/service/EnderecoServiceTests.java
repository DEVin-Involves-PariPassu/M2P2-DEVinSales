package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.EnderecoDTO;
import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.repository.CidadeEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EnderecoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EnderecoServiceTests {

    @Mock
    private EstadoEntityRepository estadoEntityRepository;

    @Mock
    private CidadeEntityRepository cidadeEntityRepository;

    @Mock
    private EnderecoEntityRepository enderecoEntityRepository;

    @InjectMocks
    private EnderecoEntityService service;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve listar endereços")
    public void deveListarEnderecos() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(enderecoEntityRepository.findAll(any(Specification.class))).thenReturn(List.of(enderecoEntity));

        // Execução
        List<EnderecoEntity> enderecosRetornados = service.listar(cidadeEntity.getId(), estadoEntity.getId(), enderecoEntity.getRua(), enderecoEntity.getNumero(), enderecoEntity.getComplemento());
        // Validação
        Assertions.assertEquals(List.of(enderecoEntity), enderecosRetornados);
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.enderecoEntityRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Não Deve listar endereços com estado inexistente")
    public void naoDeveListarEnderecosComEstadoInexistente() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(2L)).thenReturn(Optional.empty());

        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            List<EnderecoEntity> enderecosRetornados = service.listar(cidadeEntity.getId(), 2L, enderecoEntity.getRua(), enderecoEntity.getNumero(), enderecoEntity.getComplemento());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não Deve listar endereços com cidade inexistente")
    public void naoDeveListarEnderecosComCidadeInexistente() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(2L)).thenReturn(Optional.of(cidadeEntity));

        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            List<EnderecoEntity> enderecosRetornados = service.listar(2L, estadoEntity.getId(), enderecoEntity.getRua(), enderecoEntity.getNumero(), enderecoEntity.getComplemento());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não deve listar endereços com estado inválido")
    public void naoDeveListarEnderecosQuandoEstadoForInvalido() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        EstadoEntity estadoEntityInvalido = new EstadoEntity();
        estadoEntityInvalido.setId(2L);
        estadoEntityInvalido.setNome("Santa Catarina");
        estadoEntityInvalido.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(estadoEntityInvalido.getId())).thenReturn(Optional.of(estadoEntityInvalido));

        // Execução
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            List<EnderecoEntity> enderecosRetornados = service.listar(cidadeEntity.getId(), estadoEntityInvalido.getId(), enderecoEntity.getRua(), enderecoEntity.getNumero(), enderecoEntity.getComplemento());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntityInvalido.getId());
    }

    @Test
    @DisplayName("Deve listar endereço por Id")
    public void deveListarEnderecoPorId() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(enderecoEntityRepository.findById(enderecoEntity.getId())).thenReturn(Optional.of(enderecoEntity));

        // Execução
        EnderecoEntity enderecoRetornado = service.listarPorId(cidadeEntity.getId(), estadoEntity.getId(), enderecoEntity.getId());
        // Validação
        Assertions.assertEquals(enderecoEntity, enderecoRetornado);
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.enderecoEntityRepository, times(1)).findById(enderecoEntity.getId());
    }

    @Test
    @DisplayName("Não deve listar endereço por Id com estado inexistente")
    public void naoDeveListarEnderecoPorIdQuandoEstadoNaoExistir() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(2L)).thenReturn(Optional.empty());

        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            EnderecoEntity enderecoRetornado = service.listarPorId(cidadeEntity.getId(), 2L, enderecoEntity.getId());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não deve listar endereço por Id com cidade inexistente")
    public void naoDeveListarEnderecoPorIdQuandoCidadeNaoExistir() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(2L)).thenReturn(Optional.of(cidadeEntity));

        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            EnderecoEntity enderecoRetornado = service.listarPorId(2L, estadoEntity.getId(), enderecoEntity.getId());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não deve listar endereço por Id com endereço inexistente")
    public void naoDeveListarEnderecoPorIdQuandoEnderecoNaoExistir() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(enderecoEntityRepository.findById(2L)).thenReturn(Optional.empty());


        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            EnderecoEntity enderecoRetornado = service.listarPorId(cidadeEntity.getId(), estadoEntity.getId(), 2L);
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.enderecoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não deve listar endereço por Id com estado inválido")
    public void naoDeveListarEnderecoPorIdQuandoEstadoForInvalido() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        EstadoEntity estadoEntityInvalido = new EstadoEntity();
        estadoEntityInvalido.setId(2L);
        estadoEntityInvalido.setNome("Santa Catarina");
        estadoEntityInvalido.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(estadoEntityInvalido.getId())).thenReturn(Optional.of(estadoEntityInvalido));
        when(enderecoEntityRepository.findById(enderecoEntity.getId())).thenReturn(Optional.of(enderecoEntity));

        // Execução
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EnderecoEntity enderecoRetornado = service.listarPorId(cidadeEntity.getId(), estadoEntityInvalido.getId(), enderecoEntity.getId());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntityInvalido.getId());
        verify(this.enderecoEntityRepository, times(1)).findById(enderecoEntity.getId());
    }

    @Test
    @DisplayName("Não deve listar endereço por Id com cidade inválida")
    public void naoDeveListarEnderecoPorIdQuandoCidadeForInvalida() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        EstadoEntity estadoEntityInvalido = new EstadoEntity();
        estadoEntityInvalido.setId(2L);
        estadoEntityInvalido.setNome("Santa Catarina");
        estadoEntityInvalido.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        CidadeEntity cidadeEntityInvalida = new CidadeEntity();
        cidadeEntityInvalida.setId(2L);
        cidadeEntityInvalida.setNome("Florianópolis");
        cidadeEntityInvalida.setEstado(estadoEntityInvalido);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(123);
        enderecoEntity.setComplemento("Primavera Garden");
        enderecoEntity.setCidade(cidadeEntity);

        when(cidadeEntityRepository.findById(cidadeEntityInvalida.getId())).thenReturn(Optional.of(cidadeEntityInvalida));
        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(enderecoEntityRepository.findById(enderecoEntity.getId())).thenReturn(Optional.of(enderecoEntity));

        // Execução
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EnderecoEntity enderecoRetornado = service.listarPorId(cidadeEntityInvalida.getId(), estadoEntity.getId(), enderecoEntity.getId());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntityInvalida.getId());
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.enderecoEntityRepository, times(1)).findById(enderecoEntity.getId());
    }

    @Test
    @DisplayName("Salvar Endereço com Corpo da Requisição Completo")
    public void deveSalvarEnderecoQuandoCorpoEstaCompleto(){

        EstadoEntity estadoEntity = new EstadoEntity();
        CidadeEntity cidadeEntity = new CidadeEntity();

        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Florianópolis");
        cidadeEntity.setEstado(estadoEntity);

        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setNumero(123);
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(enderecoEntityRepository.save(any(EnderecoEntity.class))).thenAnswer(new Answer<EnderecoEntity>() {
            public EnderecoEntity answer(InvocationOnMock invocation) throws Throwable {
                EnderecoEntity enderecoRetornado = invocation.getArgument(0);
                if (enderecoRetornado != null) {
                    enderecoRetornado.setId(1L);
                }
                return enderecoRetornado;
            }
        });

        Long idEndereco = service.salvar(enderecoDTO, estadoEntity.getId(), cidadeEntity.getId());

        Assertions.assertEquals(1L, idEndereco);
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.enderecoEntityRepository, times(1)).save(any(EnderecoEntity.class));
    }

    @Test
    @DisplayName("Não Salvar Endereco Quando Estado Não Existir")
    public void naoDeveSalvarEnderecoQuandoEstadoNaoExistir(){

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setEstado(estadoEntity);
        cidadeEntity.setNome("Florianópolis");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setNumero(123);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setComplemento("DevInHouse");

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, ()-> {
            Long idEndereco = service.salvar(enderecoDTO, 2L, cidadeEntity.getId());
        });

        verify(this.estadoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não Salvar Endereco Quando Cidade Não Existir")
    public void naoDeveSalvarEnderecoQuandoCidadeNaoExistir(){

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setEstado(estadoEntity);
        cidadeEntity.setNome("Florianópolis");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setNumero(123);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, ()-> {
            Long idEndereco = service.salvar(enderecoDTO, estadoEntity.getId(), 2L);
        });

        verify(this.cidadeEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não Salvar Endereço Quando Rua for Nulo")
    public void naoDeveSalvarEnderecoQuandoRuaForNulo(){

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setEstado(estadoEntity);
        cidadeEntity.setNome("Florianópolis");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setNumero(123);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));

        Assertions.assertThrows(RequiredFieldMissingException.class, ()-> {
            Long idEndereco = service.salvar(enderecoDTO, estadoEntity.getId(), cidadeEntity.getId());
        });
    }

    @Test
    @DisplayName("Não Salvar Endereço Quando Número for Nulo")
    public void naoDeveSalvarEnderecoQuandoNumeroForNulo(){

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setEstado(estadoEntity);
        cidadeEntity.setNome("Florianópolis");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));

        Assertions.assertThrows(RequiredFieldMissingException.class, ()-> {
            Long idEndereco = service.salvar(enderecoDTO, 1L, 1L);
        });
    }

    @Test
    @DisplayName("Não Salvar Endereço Quando ID Estado for Inválido")
    public void naoDeveSalvarEnderecoQuandoIDEstadoForInválido(){

        EstadoEntity estadoEntityInvalido = new EstadoEntity();
        estadoEntityInvalido.setId(2L);
        estadoEntityInvalido.setNome("Paraná");
        estadoEntityInvalido.setSigla(SiglaEstado.PR);

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setEstado(estadoEntity);
        cidadeEntity.setNome("Florianópolis");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setNumero(123);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estadoEntityInvalido.getId())).thenReturn(Optional.of(estadoEntityInvalido));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));

        Assertions.assertThrows(IllegalArgumentException.class, ()-> {
            Long idEndereco = service.salvar(enderecoDTO, estadoEntityInvalido.getId(), cidadeEntity.getId());
        });

        verify(this.estadoEntityRepository, times(1)).findById(estadoEntityInvalido.getId());
    }

    @Test
    @DisplayName("Não Salvar Endereço Quando ID Cidade for Inválido")
    public void naoDeveSalvarEnderecoQuandoIDCidadeForInválido(){

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        EstadoEntity estadoEntityInvalido = new EstadoEntity();
        estadoEntityInvalido.setId(2L);
        estadoEntityInvalido.setNome("Paraná");
        estadoEntityInvalido.setSigla(SiglaEstado.PR);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setEstado(estadoEntity);
        cidadeEntity.setNome("Florianópolis");

        CidadeEntity cidadeEntityInvalido = new CidadeEntity();
        cidadeEntityInvalido.setId(2L);
        cidadeEntityInvalido.setEstado(estadoEntityInvalido);
        cidadeEntityInvalido.setNome("Curitiba");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCidadeId(2L);
        enderecoDTO.setNumero(123);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(cidadeEntityInvalido.getId())).thenReturn(Optional.of(cidadeEntityInvalido));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));

        Assertions.assertThrows(IllegalArgumentException.class, ()-> {
            Long idEndereco = service.salvar(enderecoDTO, estadoEntity.getId(), cidadeEntityInvalido.getId());
        });

        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntityInvalido.getId());
    }

    @Test
    @DisplayName("Não Deleta Endereco com ID de Estado Inválido ")
    public void naoDeveDeletarEnderecoQuandoIDEstadoForInvalido(){

        EstadoEntity estadoEntityInvalido = new EstadoEntity();
        estadoEntityInvalido.setId(2L);
        estadoEntityInvalido.setNome("Paraná");
        estadoEntityInvalido.setSigla(SiglaEstado.PR);

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Florianópolis");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setCidade(cidadeEntity);
        enderecoEntity.setNumero(123);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estadoEntityInvalido.getId())).thenReturn(Optional.of(estadoEntityInvalido));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(enderecoEntityRepository.findById(enderecoEntity.getId())).thenReturn(Optional.of(enderecoEntity));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.deletar(estadoEntityInvalido.getId(), cidadeEntity.getId(),enderecoEntity.getId());
        });

        verify(this.estadoEntityRepository, times(1)).findById(estadoEntityInvalido.getId());
    }

    @Test
    @DisplayName("Não Deleta Endereco com ID de Endereço Inexistente")
    public void naoDeveDeletarEnderecoQuandoIDEnderecoForInexistente(){

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Florianópolis");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setCidade(cidadeEntity);
        enderecoEntity.setNumero(123);
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(enderecoEntityRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.deletar(estadoEntity.getId(), cidadeEntity.getId(),2L);
        });

        verify(this.enderecoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Deleta endereço por id válido")
    public void deveDeletarEnderecoQuandoIdForValido(){

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua Ipanema");
        enderecoEntity.setNumero(10);
        enderecoEntity.setComplemento("Casa");
        enderecoEntity.setCidade(cidadeEntity);

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(enderecoEntityRepository.findById(enderecoEntity.getId())).thenReturn(Optional.of(enderecoEntity));
        doNothing().when(enderecoEntityRepository).delete(enderecoEntity);
        service.deletar(estadoEntity.getId(), cidadeEntity.getId(),enderecoEntity.getId());


        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.enderecoEntityRepository, times(1)).delete(enderecoEntity);
    }

}
