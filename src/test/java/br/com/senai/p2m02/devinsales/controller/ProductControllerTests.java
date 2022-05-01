package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.service.ProductService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Test
    public void deveCriarUmProdutoQuandoForAutenticado() throws Exception {
        // gerando o token
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();


        // extraindo o token
        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        ProductEntity produto = new ProductEntity();
        produto.setNome("batata");
        produto.setPreco_sugerido(BigDecimal.valueOf(0.50));
        produto.setId(1L);

        when(productService.insert(any(ProductDTO.class))).thenReturn(1L);

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"batata\",\n" +
                "    \"preco_sugerido\":0.50\n" +
                "}";

        //executando controller com o token
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
}
