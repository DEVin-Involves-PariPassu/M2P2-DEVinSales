package br.com.senai.p2m02.devinsales.service;


import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTests {
    private static final Long ID = 1L;
    private static final String NOME = "Headset Gamer";
    private static final BigDecimal PRECO = BigDecimal.valueOf(399.90);

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ItemVendaEntityRepository itemVendaRepository;

    @InjectMocks
    private ProductService service = new ProductService();


    private ProductDTO productDTO;
    private ProductEntity productEntity;
    private Optional<ProductEntity> optionalProduct;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        startProduct();
    }



    @Test
    @DisplayName("Deleta um produto")
    public void deveDeletarUmProdutoQuandoPassarUmIdValido(){
        //Cenário
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        when(productRepository.findById(1l)).thenReturn(Optional.of(productEntity));
        when(itemVendaRepository.findByProduto(productEntity)).thenReturn(List.of());
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
        when(productRepository.findById(2l)).thenReturn(Optional.empty());
        // Validação
        assertThrows(EntityNotFoundException.class, () -> {
            service.delete(2L);
            verify(this.productRepository, times(1)).findAll();
        });
    }

    @Test
    @DisplayName("Buscar produto pelo Id")
    public void deveRetornarUmProductEntityQuandoPassarUmIdValido(){
        when(productRepository.findById(Mockito.anyLong())).thenReturn(optionalProduct);

        ProductEntity response = service.findById(ID);

        assertNotNull(response);
        assertEquals(ProductEntity.class, response.getClass());
        assertEquals(ID, response.getId());
        Assertions.assertEquals(NOME, response.getNome());
        Assertions.assertEquals(PRECO, response.getPreco_sugerido());
    }

    @Test
    @DisplayName("Id de Produto não encontrado")
    public void deveLancarExcecaoQuandoPassarUmIdInvalido(){
        when(productRepository.findById(anyLong())).thenThrow(new EntityNotFoundException("Não há nenhum produto com este Id"));

                try{
                    service.findById(ID);
                } catch (Exception ex){
                    assertEquals(EntityNotFoundException.class, ex.getClass());
                    assertEquals("Não há nenhum produto com este Id", ex.getMessage());
                }
    }

    @Test
    @DisplayName("Inserir novo produto")
    public void deveRetornarOIdDoProdutoQuandoInserirUmProdutoValido(){
        when(service.insert(productDTO)).thenReturn(anyLong());

        Long response = service.insert(productDTO);

        assertNotNull(response);
        assertEquals(Long.class, response.getClass());
    }


    @Test
    @DisplayName("Produto com nome inválido")
    public void deveLancarUmaExcecaoQuandoTentarAdicionarUmProdutoComNomeInvalido(){

        ProductDTO novoProduto = new ProductDTO();
        novoProduto.setPreco_sugerido(BigDecimal.valueOf(15.00));
        novoProduto.setNome("");

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        assertThrows(RequiredFieldMissingException.class, () -> {
            service.insert(novoProduto);
        });
    }

    @Test
    @DisplayName("Produto com preço inválido")
    public void deveLancarUmaExcecaoQuandoTentarAdicionarUmProdutoComPrecoInvalido(){

        ProductDTO novoProduto = new ProductDTO();
        novoProduto.setNome("Café em grãos");
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        Assertions.assertThrows(RequiredFieldMissingException.class, () -> {
            service.insert(novoProduto);
        });

    }

    @Test
    @DisplayName("Salvar no repository")
    public void deveRetornarUmProductEntityQuandoSalvarUmNovoProduto(){
        when(productRepository.save(any())).thenReturn(productEntity);

        ProductEntity response = productRepository.save(productEntity);

        assertNotNull(response);
        assertEquals(ProductEntity.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(PRECO, response.getPreco_sugerido());
    }



    private void startProduct() {
        productDTO = new ProductDTO();
        productDTO.setNome(NOME);
        productDTO.setPreco_sugerido(PRECO);
        productEntity = new ProductEntity(ID, NOME, PRECO);
        optionalProduct = Optional.of(new ProductEntity(ID, NOME, PRECO));
        this.productRepository.save(productEntity);
        service.insert(productDTO);
    }


}
