package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.CidadeDTO;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
    private EnderecoEntityService enderecoEntityService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Salvar Endereço com Corpo da Requisição Completo")
    public void deveSalvarEnderecoQuandoCorpoEstaCompleto(){

        EstadoEntity estado = new EstadoEntity();
        CidadeEntity cidade = new CidadeEntity();

        cidade.setId(1L);
        cidade.setNome("Florianópolis");
        cidade.setEstado(estado);

        estado.setId(1L);
        estado.setNome("Santa Catarina");
        estado.setSigla(SiglaEstado.SC);

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setNumero(123);
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estado.getId())).thenReturn(Optional.of(estado));
        when(cidadeEntityRepository.findById(cidade.getId())).thenReturn(Optional.of(cidade));
        when(enderecoEntityRepository.save(any(EnderecoEntity.class))).thenAnswer(new Answer<EnderecoEntity>() {
            public EnderecoEntity answer(InvocationOnMock invocation) throws Throwable {
                EnderecoEntity enderecoRetornado = invocation.getArgument(0);
                if (enderecoRetornado != null) {
                    enderecoRetornado.setId(1L);
                }
                return enderecoRetornado;
            }
        });

        // Execução
        Long idEndereco = enderecoEntityService.salvar(enderecoDTO, estado.getId(), cidade.getId());
        // Validação
        Assertions.assertEquals(1L, idEndereco);
        verify(this.estadoEntityRepository, times(1)).findById(estado.getId());
        verify(this.cidadeEntityRepository, times(1)).findById(cidade.getId());
        verify(this.enderecoEntityRepository, times(1)).save(any(EnderecoEntity.class));
    }

    @Test
    @DisplayName("Não Salvar Endereco Quando Estado Não Existir")
    public void naoDeveSalvarEnderecoQuandoEstadoNaoExistir(){

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Santa Cataria");
        estado.setSigla(SiglaEstado.SC);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setEstado(estado);
        cidade.setNome("Florianópolis");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setNumero(123);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setComplemento("DevInHouse");

        when(cidadeEntityRepository.findById(cidade.getId())).thenReturn(Optional.of(cidade));
        when(estadoEntityRepository.findById(2L)).thenReturn(Optional.empty());

        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, ()-> {
            Long idEndereco = enderecoEntityService.salvar(enderecoDTO, 2L, 1L);
        });
        // Validação
        verify(this.estadoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não Salvar Endereco Quando Cidade Não Existir")
    public void naoDeveSalvarEnderecoQuandoCidadeNaoExistir(){

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Santa Cataria");
        estado.setSigla(SiglaEstado.SC);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setEstado(estado);
        cidade.setNome("Florianópolis");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setRua("Rua Principal");
        enderecoDTO.setNumero(123);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estado.getId())).thenReturn(Optional.of(estado));
        when(cidadeEntityRepository.findById(2L)).thenReturn(Optional.empty());

        // Execução
        Assertions.assertThrows(EntityNotFoundException.class, ()-> {
            Long idEndereco = enderecoEntityService.salvar(enderecoDTO, 1L, 2L);
        });
        // Validação
        verify(this.cidadeEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não Salvar Endereço Quando Rua for Nulo")
    public void naoDeveSalvarEnderecoQuandoRuaForNulo(){

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Santa Cataria");
        estado.setSigla(SiglaEstado.SC);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setEstado(estado);
        cidade.setNome("Florianópolis");

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setEstadoId(1L);
        enderecoDTO.setNumero(123);
        enderecoDTO.setCidadeId(1L);
        enderecoDTO.setComplemento("DevInHouse");

        when(estadoEntityRepository.findById(estado.getId())).thenReturn(Optional.of(estado));
        when(cidadeEntityRepository.findById(cidade.getId())).thenReturn(Optional.of(cidade));

        // Execução
        Assertions.assertThrows(RequiredFieldMissingException.class, ()-> {
            Long idEndereco = enderecoEntityService.salvar(enderecoDTO, 1L, 1L);
        });
        // Validação
        verify(this.estadoEntityRepository, times(1)).findById(estado.getId());
        verify(this.cidadeEntityRepository, times(1)).findById(cidade.getId());
    }

}
