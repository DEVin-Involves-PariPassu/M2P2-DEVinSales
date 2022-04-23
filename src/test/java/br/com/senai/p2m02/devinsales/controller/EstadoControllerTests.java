package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.service.EstadoEntityService;
import br.com.senai.p2m02.devinsales.service.ProductService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EstadoControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EstadoEntityService service;

    @Test
    @DisplayName("Criar Estado Autorizado")
    public void deveCriarEstadoQuandoForAutorizado() throws Exception {
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

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        when(service.salvar(any(EstadoDTO.class))).thenReturn(1L);

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"Distrito Federal\",\n" +
                "    \"sigla\":\"DF\"\n" +
                "}";

        //executando controller com o token
        MvcResult resultPost = mockMvc.perform(MockMvcRequestBuilders.post("/state")
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
    @DisplayName("Criar Estado Sem Autorização")
    public void naoDeveCriarEstadoQuandoNaoForAutorizado() throws Exception {
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

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        when(service.salvar(any(EstadoDTO.class))).thenReturn(1L);

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"Distrito Federal\",\n" +
                "    \"sigla\":\"DF\"\n" +
                "}";

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.post("/state")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Criar Estado Sem Autenticação")
    public void naoDeveCriarEstadoQuandoNaoForAutenticado() throws Exception {
        // gerando o token
        String body = "{\"login\":\"naoexiste\",\"senha\":\"naoexiste123\"}";

        mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isUnauthorized());
    }
}