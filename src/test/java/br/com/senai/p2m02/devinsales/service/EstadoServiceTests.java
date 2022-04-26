package br.com.senai.p2m02.devinsales.service;


import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.repository.CidadeEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.SpecificationsEstadoEntity;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class EstadoServiceTests {
    @Mock
    private EstadoEntityRepository estadoEntityRepository;

    @Mock
    private CidadeEntityRepository cidadeEntityRepository;

//    @Mock
//    private SpecificationsEstadoEntity specifications;

    @InjectMocks
    private EstadoEntityService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Listar Estados")
    public void deveListarEstados() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Acre");
        estadoEntity.setSigla(SiglaEstado.AC);

        when(estadoEntityRepository.findAll(any(Specification.class))).thenReturn(List.of(estadoEntity));
        // Execução
        List<EstadoEntity> estadosRetornados = service.listar(estadoEntity.getNome());
        // Validação
        Assertions.assertEquals(List.of(estadoEntity), estadosRetornados);
        verify(this.estadoEntityRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Salvar Estado")

    public void deveSalvarEstadoQuandoCorpoEstaCompleto() {

        //Cenário
//        EstadoEntity estadoEntity = new EstadoEntity();
//        estadoEntity.setNome("Distrito Federal");
//        estadoEntity.setSigla(SiglaEstado.DF);

        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setNome("Distrito Federal");
        estadoDTO.setSigla("DF");

        when(estadoEntityRepository.findFirstByNome(estadoDTO.getNome())).thenReturn(Optional.empty());
        when(estadoEntityRepository.findFirstBySigla(SiglaEstado.valueOf(estadoDTO.getSigla()))).thenReturn(Optional.empty());
        when(estadoEntityRepository.save(any(EstadoEntity.class))).thenAnswer(new Answer<EstadoEntity>() {
            public EstadoEntity answer(InvocationOnMock invocation) throws Throwable {
                EstadoEntity estadoRetornado = invocation.getArgument(0);
                if (estadoRetornado != null) {
                    estadoRetornado.setId(27L);
                }
                return estadoRetornado;
            }
        });

        // Execução
        Long idEstado = service.salvar(estadoDTO);
        // Validação
        Assertions.assertEquals(27L, idEstado);
        verify(this.estadoEntityRepository, times(1)).findFirstByNome(estadoDTO.getNome());
        verify(this.estadoEntityRepository, times(1)).findFirstBySigla(SiglaEstado.valueOf(estadoDTO.getSigla()));
        verify(this.estadoEntityRepository, times(1)).save(any(EstadoEntity.class));
    }

    @Test
    @DisplayName("Não Salvar Estado Sem Nome")
    public void naoDeveSalvarEstadoQuandoCorpoFaltarNome() {
        //Cenário
//        EstadoEntity estadoEntity = new EstadoEntity();
//        estadoEntity.setNome("Distrito Federal");
//        estadoEntity.setSigla(SiglaEstado.DF);

        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setSigla("DF");

        // Validação
        Assertions.assertThrows(RequiredFieldMissingException.class, () -> {
            // Execução
            Long idEstado = service.salvar(estadoDTO);
        });
    }

    @Test
    @DisplayName("Não Salvar Estado Sem Sigla")
    public void naoDeveSalvarEstadoQuandoCorpoFaltarSigla() {
        //Cenário
//        EstadoEntity estadoEntity = new EstadoEntity();
//        estadoEntity.setNome("Distrito Federal");
//        estadoEntity.setSigla(SiglaEstado.DF);

        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setNome("Distrito Federal");

        // Validação
        Assertions.assertThrows(RequiredFieldMissingException.class, () -> {
            // Execução
            Long idEstado = service.salvar(estadoDTO);
        });
    }

    @Test
    @DisplayName("Não Salvar Estado Com Mesmo Nome")
    public void naoDeveSalvarEstadoQuandoNomeJaExistir() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setNome("Distrito Federal");
        estadoDTO.setSigla("DF");

        when(estadoEntityRepository.findFirstByNome(estadoDTO.getNome())).thenReturn(Optional.of(estadoEntity));

        // Validação
        Assertions.assertThrows(EntityExistsException.class, () -> {
            // Execução
            Long idEstado = service.salvar(estadoDTO);
        });
        verify(this.estadoEntityRepository, times(1)).findFirstByNome(estadoDTO.getNome());
    }

    @Test
    @DisplayName("Não Salvar Estado Com Mesma Sigla")
    public void naoDeveSalvarEstadoQuandoSiglaJaExistir() {
        //Cenário
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setNome("Distrito Federal 2");
        estadoDTO.setSigla("DF");

        when(estadoEntityRepository.findFirstByNome(estadoDTO.getNome())).thenReturn(Optional.empty());
        when(estadoEntityRepository.findFirstBySigla(SiglaEstado.valueOf(estadoDTO.getSigla()))).thenReturn(Optional.of(estadoEntity));

        // Validação
        Assertions.assertThrows(EntityExistsException.class, () -> {
            // Execução
            Long idEstado = service.salvar(estadoDTO);
        });

        verify(this.estadoEntityRepository, times(1)).findFirstByNome(estadoDTO.getNome());
        verify(this.estadoEntityRepository, times(1)).findFirstBySigla(SiglaEstado.valueOf(estadoDTO.getSigla()));
    }

    @Test
    @DisplayName("Não Salvar Estado Com Sigla Inválida")
    public void naoDeveSalvarEstadoQuandoSiglaForInvalida() {
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setNome("Bananalândia");
        estadoDTO.setSigla("BN");

        // Validação
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // Execução
            Long idEstado = service.salvar(estadoDTO);
        });
    }

    @Test
    @DisplayName("Deleta estado com Id válido")
    public void deveDeletarEstadoQuandoIdForValido(){
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findAll(any(Specification.class))).thenReturn(new ArrayList<>());
        doNothing().when(estadoEntityRepository).delete(estadoEntity);
        service.deletar(estadoEntity.getId());

        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.cidadeEntityRepository, times(1)).findAll(any(Specification.class));
        verify(this.estadoEntityRepository, times(1)).delete(estadoEntity);
    }

    @Test
    @DisplayName("Não deleta estado com Id inválido")
    public void naoDeveDeletarEstadoQuandoIdForInvalido(){
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        when(estadoEntityRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.deletar(2L);
        });

        verify(this.estadoEntityRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Não deleta estado com cidade referenciada")
    public void naoDeveDeletarEstadoComCidadeReferenciada(){
        EstadoEntity estadoEntity = new EstadoEntity();
        estadoEntity.setId(1L);
        estadoEntity.setNome("Distrito Federal");
        estadoEntity.setSigla(SiglaEstado.DF);

        CidadeEntity cidadeEntity = new CidadeEntity();
        cidadeEntity.setId(1L);
        cidadeEntity.setNome("Brasilia");
        cidadeEntity.setEstado(estadoEntity);

        when(estadoEntityRepository.findById(estadoEntity.getId())).thenReturn(Optional.of(estadoEntity));
        when(cidadeEntityRepository.findAll(any(Specification.class))).thenReturn(List.of(cidadeEntity));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> {
            service.deletar(estadoEntity.getId());
        });

        verify(this.estadoEntityRepository, times(1)).findById(estadoEntity.getId());
        verify(this.cidadeEntityRepository, times(1)).findAll(any(Specification.class));
    }
}