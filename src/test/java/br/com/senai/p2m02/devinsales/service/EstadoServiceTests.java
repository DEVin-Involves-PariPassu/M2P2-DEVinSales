package br.com.senai.p2m02.devinsales.service;


import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.SpecificationsEstadoEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class EstadoServiceTests {
    @Mock
    private EstadoEntityRepository repository;

//    @Mock
//    private SpecificationsEstadoEntity specifications;

    @InjectMocks
    private EstadoEntityService service = new EstadoEntityService();

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

        Specification specification = Specification.where(
                SpecificationsEstadoEntity.nome(estadoEntity.getNome())
        );

        when(repository.findAll(specification)).thenReturn(List.of(estadoEntity));
        // Execução
        List<EstadoEntity> estadosRetornados = service.listar(estadoEntity.getNome());
        // Validação
        Assertions.assertEquals(List.of(estadoEntity), estadosRetornados);
        //verify(this.repository, times(1)).findAll(specification);
    }
}

