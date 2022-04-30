package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.service.ProductService;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    public void initDB() {
        ProductEntity produto = new ProductEntity();
        produto.setNome("batata");
        produto.setPreco_sugerido(BigDecimal.valueOf(3.50));
        produto.setId(1L);
        productRepository.save(produto);
    }

    @Test
    @DisplayName("Criar novo produto")
    public void deveCriarUmProdutoQuandoEstiverAutenticado() throws Exception {
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        when(service.insert(any(ProductDTO.class))).thenReturn(1L);

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"batata\",\n" +
                "    \"preco_sugerido\":0.50\n" +
                "}";

        MvcResult resultPost = mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isCreated())
                .andReturn();
        String responsePost = resultPost.getResponse().getContentAsString();
        Assertions.assertNotEquals(responsePost, "");
        Assertions.assertEquals
                ("1", responsePost);
    }

    @Test
    @DisplayName("Nome produto incorreto - Bad Request")
    public void deveRetornarBadRequestQuandoInformarUmProdutoComNomeInvalido() throws Exception {
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setNome("");
        productDTO.setPreco_sugerido(BigDecimal.valueOf(50.00));
        when(service.insert(any(ProductDTO.class))).thenThrow(new RequiredFieldMissingException("O nome do produto é obrigatório."));

        String bodyRequisicao = "{\n" +
                "    \"preco_sugerido\":50.00\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Preço do produto não informado - Bad Request")
    public void deveRetornarBadRequestQuandoInformarUmProdutoSemPreco() throws Exception {
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setNome("Coffee");

        when(service.insert(any(ProductDTO.class))).thenThrow(new RequiredFieldMissingException("O valor do produto é obrigatório."));

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"Coffee\",\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Preço do produto igual a zero - Bad Request")
    public void deveRetornarBadRequestQuandoInformarUmProdutoComPrecoIgualAZero() throws Exception {
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setNome("Coffee");
        productDTO.setPreco_sugerido(BigDecimal.valueOf(0.00));
        when(service.insert(any(ProductDTO.class))).thenThrow(new IllegalArgumentException("Valor do produto inválido."));

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"Coffee\",\n" +
                "    \"preco_sugerido\":0.00\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Preço do produto invalido - Bad Request")
    public void deveRetornarBadRequestQuandoInformarUmProdutoComPrecoInvalido() throws Exception {
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setNome("Coffee");
        productDTO.setPreco_sugerido(BigDecimal.valueOf(-1.00));
        when(service.insert(any(ProductDTO.class))).thenThrow(new IllegalArgumentException("Valor do produto inválido."));

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"Coffee\",\n" +
                "    \"preco_sugerido\":-1.00\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isBadRequest());

    }


    @Test
    @DisplayName("Post/Product sem autenticacao")
    public void naoDeveAdicionarUmNovoProdutoQuandoNaoForAutenticado() throws Exception{
            String body = "{\"login\":\"naoexiste\",\"senha\":\"naoexiste123\"}";

            mockMvc
                    .perform(MockMvcRequestBuilders.post("/product")
                            .header("Content-Type", "application/json" )
                            .content(body))
                    .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Atualizar produto")
    public void deveAtualizarOProdutoQuandoPassarOsDadosCorretamente() throws Exception {
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductEntity produto = new ProductEntity();
        produto.setNome("batata");
        produto.setPreco_sugerido(BigDecimal.valueOf(3.50));
        produto.setId(1L);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setPreco_sugerido(BigDecimal.valueOf(3.55));
        productDTO.setNome("batatas");

        when(service.updateDoPut(produto.getId(), productDTO)).thenReturn(1L);

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"batatas\",\n" +
                "    \"preco_sugerido\":3.55\n" +
                "}";

        MvcResult resultPut = mockMvc.perform(MockMvcRequestBuilders.put("/product/{id_produto}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        String responsePut = resultPut.getResponse().getContentAsString();
        Assertions.assertNotEquals(responsePut, "{\\n\" +\n" +
                "                \"    \\\"nome\\\":\\\"batata\\\",\\n\" +\n" +
                "                \"    \\\"preco_sugerido\\\":3.50\\n\" +\n" +
                "                \"}");
    }

    @Test
    @DisplayName("Atualizar produto - patch")
    public void deveAtualizarOProdutoQuandoPassarOsDados() throws Exception {
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductEntity produto = new ProductEntity();
        produto.setNome("Uva");
        produto.setPreco_sugerido(BigDecimal.valueOf(10.50));
        produto.setId(1L);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setPreco_sugerido(BigDecimal.valueOf(10.55));

        when(service.updateDoPatch(produto.getId(), productDTO)).thenReturn(1L);

        String bodyRequisicao = "{\n" +
                "    \"preco_sugerido\":10.55\n" +
                "}";

        MvcResult resultPatch = mockMvc.perform(MockMvcRequestBuilders.patch("/product/{id_produto}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        String responsePatch = resultPatch.getResponse().getContentAsString();
        Assertions.assertNotEquals(responsePatch, "{\\n\" +\n" +
                "                \"    \\\"nome\\\":\\\"Uva\\\",\\n\" +\n" +
                "                \"    \\\"preco_sugerido\\\":10.50\\n\" +\n" +
                "                \"}");
    }

    @Test
    @DisplayName("Produto inexistente - nulo")
    public void deveRetornarBadRequestQuandoOIdDoProdutoForNulo() throws Exception {
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();


        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductEntity produto = new ProductEntity();
        produto.setNome("Maçã");
        produto.setPreco_sugerido(BigDecimal.valueOf(4.50));
        produto.setId(1L);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setNome("Morango");
        productDTO.setPreco_sugerido(BigDecimal.valueOf(7.00));


        doThrow(EntityNotFoundException.class).when(service).updateDoPut(null, productDTO);

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"CAFÉ\",\n" +
                "    \"preco_sugerido\":55.50\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id_produto}", "null")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Put/patch sem autenticacao")
    public void naoDeveAtualizarUmProdutoQuandoNaoForAutenticado() throws Exception{
        String body = "{\"login\":\"naoexiste\",\"senha\":\"naoexiste123\"}";

        mockMvc
                .perform(MockMvcRequestBuilders.put("/product/1")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isForbidden());
    }

     @Test
    @DisplayName("Deletar produto pelo Id")
    public void deveDeletarProdutoQuandoNaoExistirItemVendaComOIdDoProdutoInformado() throws Exception{
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductEntity produto = new ProductEntity();
        produto.setId(1L);
        produto.setNome("Almofada");
        produto.setPreco_sugerido(BigDecimal.valueOf(55.00));

         doNothing().when(service).delete(produto.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id_produto}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    }

