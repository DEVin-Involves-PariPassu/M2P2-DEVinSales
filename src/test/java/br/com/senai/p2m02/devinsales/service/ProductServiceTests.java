package br.com.senai.p2m02.devinsales.service;


import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ItemVendaEntityRepository itemVendaRepository;

    @InjectMocks
    private ProductService service = new ProductService();

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deleta um produto")
    public void deveDeletarUmProdutoQuandoPassarUmIdValido(){
        //Cenário
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        when(productRepository.findAll()).thenAnswer(new Answer<List<ProductEntity>>() {
            @Override
            public List<ProductEntity> answer(InvocationOnMock invocationOnMock) throws Throwable {
                List<ProductEntity> products = new ArrayList<>();
                products.add(productEntity);
                return products;
            }
        });
        when(itemVendaRepository.findByProduto(productEntity)).thenReturn(Optional.empty());
        // Execução
        service.delete(productEntity.getId());
        // Validação
        verify(this.itemVendaRepository, times(1)).findByProduto(productEntity);
        verify(this.productRepository, times(1)).delete(productEntity);
    }

    @Test
    @DisplayName("Lançar exceção ao deletar caso id inválido")
    public void deveLancarEntityNotFoundExceptionQuandoPassarUmIdInvalido(){
        //Cenário
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        when(productRepository.findAll()).thenAnswer(new Answer<List<ProductEntity>>() {
            @Override
            public List<ProductEntity> answer(InvocationOnMock invocationOnMock) throws Throwable {
                List<ProductEntity> products = new ArrayList<>();
                products.add(productEntity);
                return products;
            }
        });
        // Validação
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.delete(2L);
            verify(this.productRepository, times(1)).findAll();
        });
    }
}
