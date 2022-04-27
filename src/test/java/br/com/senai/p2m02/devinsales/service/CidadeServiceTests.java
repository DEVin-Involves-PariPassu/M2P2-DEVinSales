package br.com.senai.p2m02.devinsales.service;


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

