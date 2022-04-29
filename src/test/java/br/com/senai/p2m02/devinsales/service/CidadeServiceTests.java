package br.com.senai.p2m02.devinsales.service;


import br.com.senai.p2m02.devinsales.dto.CidadeDTO;
import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.repository.CidadeEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EnderecoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.service.exception.EntityIsReferencedException;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CidadeServiceTests {
    @Mock
    private EstadoEntityRepository estadoEntityRepository;

    @Mock
    private CidadeEntityRepository cidadeEntityRepository;

    @Mock
    private EnderecoEntityRepository enderecoEntityRepository;

    @InjectMocks
    private CidadeEntityService service;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Listar Cidade Por Id")
    public void deveListarCidadePorId(){
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        // Execução
        CidadeEntity cidadeRetornado = service.listarPorId(cidadeEntity.getId(), estadoEntity.getId());
        // Validação
        Assertions.assertEquals(cidadeEntity, cidadeRetornado);
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
    }

    @Test
    @DisplayName("Não Listar Cidade Por Id Inexistente")
    public void naoDeveListarCidadePorIdInexistente(){
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        when(cidadeEntityRepository.findById(2L)).thenReturn(Optional.empty());
        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            CidadeEntity cidadeRetornado = service.listarPorId(2L, estadoEntity.getId());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não Listar Cidade Por Id de Estado Inexistente")
    public void naoDeveListarCidadePorIdDeEstadoInexistente(){
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Rio Branco");
        cidadeEntity.setEstado(estadoEntity);

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(2L)).thenReturn(Optional.empty());
        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            CidadeEntity cidadeRetornado = service.listarPorId(cidadeEntity.getId(), 2L);
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não Listar Cidade Por Id de Estado Inválido")
    public void naoDeveListarCidadePorIdDeEstadoInvalido(){
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

        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(estadoEntityRepository.findById(estadoEntityInvalido.getId())).thenReturn(Optional.of(estadoEntityInvalido));
        // Execução
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CidadeEntity cidadeRetornado = service.listarPorId(cidadeEntity.getId(), estadoEntityInvalido.getId());
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.estadoEntityRepository, times(1)).findById(estadoEntityInvalido.getId());
    }

    @Test
    @DisplayName("Salvar cidade com corpo da requisição completo")
    public void deveSalvarCidadeQuandoCorpoEstaCompleto(){

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        CidadeDTO cidadeDTO = new CidadeDTO();
        cidadeDTO.setNome("Brasília");
        cidadeDTO.setEstadoId(1L);

        when(estadoEntityRepository.findById(estado.getId())).thenReturn(Optional.of(estado));
        when(cidadeEntityRepository.findFirstByNome(cidadeDTO.getNome())).thenReturn(Optional.empty());
        when(cidadeEntityRepository.save(any(CidadeEntity.class))).thenAnswer(new Answer<CidadeEntity>() {
            public CidadeEntity answer(InvocationOnMock invocation) throws Throwable {
                CidadeEntity cidadeRetornada = invocation.getArgument(0);
                if (cidadeRetornada != null) {
                    cidadeRetornada.setId(1L);
                }
                return cidadeRetornada;
            }
        });

        // Execução
        Long idCidade = service.salvar(cidadeDTO, estado.getId());
        // Validação
        Assertions.assertEquals(1L, idCidade);
        verify(this.estadoEntityRepository, times(1)).findById(estado.getId());
        verify(this.cidadeEntityRepository, times(1)).findFirstByNome(cidadeDTO.getNome());
        verify(this.cidadeEntityRepository, times(1)).save(any(CidadeEntity.class));
    }

    @Test
    @DisplayName("Não salvar cidade quando estado não existir")
    public void naoDeveSalvarCidadeQuandoEstadoNaoExistir(){

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        CidadeDTO cidadeDTO = new CidadeDTO();
        cidadeDTO.setNome("Brasília");
        cidadeDTO.setEstadoId(1L);

        when(estadoEntityRepository.findById(2L)).thenReturn(Optional.empty());

        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, ()-> {
            Long idCidade = service.salvar(cidadeDTO, 2L);
        });
        // Validação
        verify(this.estadoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não salvar cidade quando já existir cidade com o mesmo nome")
    public void naodeveSalvarCidadeQuandoJaExistirCidadeComMesmoNome() {

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Brasília");
        cidadeEntity.setEstado(estado);

        CidadeDTO cidadeDTO = new CidadeDTO();
        cidadeDTO.setNome("Brasília");
        cidadeDTO.setEstadoId(1L);

        when(estadoEntityRepository.findById(estado.getId())).thenReturn(Optional.of(estado));
        when(cidadeEntityRepository.findFirstByNome(cidadeDTO.getNome())).thenReturn(Optional.of(cidadeEntity));

        // Execução
        Assertions.assertThrows(EntityExistsException.class, ()-> {
            Long idCidade = service.salvar(cidadeDTO, estado.getId());
        });
        // Validação
        verify(this.estadoEntityRepository, times(1)).findById(estado.getId());
        verify(this.cidadeEntityRepository, times(1)).findFirstByNome(cidadeDTO.getNome());
    }

    @Test
    @DisplayName("Não salvar cidade quando o nome for nulo")
    public void naoDeveSalvarCidadeQuandoNomeForNulo(){

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        CidadeDTO cidadeDTO = new CidadeDTO();
        cidadeDTO.setEstadoId(1L);

        when(estadoEntityRepository.findById(estado.getId())).thenReturn(Optional.of(estado));

        // Execução
        Assertions.assertThrows(RequiredFieldMissingException.class, ()-> {
            Long idCidade = service.salvar(cidadeDTO, estado.getId());
        });
        // Validação
        verify(this.estadoEntityRepository, times(1)).findById(estado.getId());
    }

    @Test
    @DisplayName("Deleta cidade com Id válido")
    public void deveDeletarCidadeQuandoIdForValido(){
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Brasilia");
        cidadeEntity.setEstado(estadoEntity);

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(enderecoEntityRepository.findAll(any(Specification.class))).thenReturn(new ArrayList<>());
        doNothing().when(cidadeEntityRepository).delete(cidadeEntity);
        service.deletar(cidadeEntity.getId(),estadoEntity.getId());

        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.enderecoEntityRepository, times(1)).findAll(any(Specification.class));
        verify(this.cidadeEntityRepository, times(1)).delete(cidadeEntity);
    }

    @Test
    @DisplayName("Não deleta cidade com Id de estado inexistente")
    public void naoDeveDeletarCidadeQuandoIdDeEstadoForInexistente(){
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Brasilia");
        cidadeEntity.setEstado(estadoEntity);

        when(estadoEntityRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.deletar(cidadeEntity.getId(), 2L);
        });

        verify(this.estadoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não deleta cidade com Id de cidade inexistente")
    public void naoDeveDeletarCidadeQuandoIdDeCidadeForInexistente(){
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Brasilia");
        cidadeEntity.setEstado(estadoEntity);

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.deletar(2L, estadoEntity.getId());
        });

        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.cidadeEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não deleta cidade com Id de Estado inválido ")
    public void naoDeveDeletarCidadeQuandoIdDeEstadoForInvalido(){
        EstadoEntity estadoEntityInvalido = new EstadoEntity();
        estadoEntityInvalido.setId(1L);
        estadoEntityInvalido.setNome("Distrito Federal");
        estadoEntityInvalido.setSigla(SiglaEstado.DF);

        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(2L);
        estadoEntity.setNome("Santa Catarina");
        estadoEntity.setSigla(SiglaEstado.SC);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(2L);
        cidadeEntity.setNome("Florianópolis");
        cidadeEntity.setEstado(estadoEntity);

        when(estadoEntityRepository.findById(estadoEntityInvalido.getId())).thenReturn(Optional.of(estadoEntityInvalido));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.deletar(cidadeEntity.getId(), estadoEntityInvalido.getId());
        });

        verify(this.estadoEntityRepository, times(1)).findById(estadoEntityInvalido.getId());
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
    }

    @Test
    @DisplayName("Não deleta cidade com endereco referenciado")
    public void naoDeveDeletarCidadeComEnderecoReferenciado(){
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Brasilia");
        cidadeEntity.setEstado(estadoEntity);

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(1L);
        enderecoEntity.setRua("Rua 2");
        enderecoEntity.setNumero(50);
        enderecoEntity.setCidade(cidadeEntity);

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findById(cidadeEntity.getId())).thenReturn(Optional.of(cidadeEntity));
        when(enderecoEntityRepository.findAll(any(Specification.class))).thenReturn(List.of(enderecoEntity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> {
            service.deletar(cidadeEntity.getId(),estadoEntity.getId());
        });

        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.cidadeEntityRepository, times(1)).findById(cidadeEntity.getId());
        verify(this.enderecoEntityRepository, times(1)).findAll(any(Specification.class));
    }
}

