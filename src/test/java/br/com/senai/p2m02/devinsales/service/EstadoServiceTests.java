package br.com.senai.p2m02.devinsales.service;


import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.repository.CidadeEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.SpecificationsEstadoEntity;
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
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Listar Estados")
    public void deveListarEstados(){
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
    public void deveSalvarEstado(){
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
        verify(this.estadoEntityRepository, times(1)).save(any(EstadoEntity.class));
    }
}

