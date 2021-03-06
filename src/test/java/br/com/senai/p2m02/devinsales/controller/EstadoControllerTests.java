package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.service.EstadoEntityService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
public class EstadoControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EstadoEntityService estadoEntityService;

    @Test
    @DisplayName("Sem Autenticação")
    public void naoDeveProsseguirQuandoNaoForAutenticado() throws Exception {
        // gerando o token
        String body = "{\"login\":\"naoexiste\",\"senha\":\"naoexiste123\"}";

        mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Listar Estados Autorizado")
    public void deveListarEstadosQuandoForAutorizado() throws Exception {
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
        estado.setId(27L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        when(estadoEntityService.listar("Distrito Federal")).thenReturn(List.of(estado));

        //executando controller com o token
        MvcResult resultGet = mockMvc.perform(MockMvcRequestBuilders.get("/state")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("nome", "Distrito Federal")
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseGet = resultGet.getResponse().getContentAsString();
        Assertions.assertNotEquals(responseGet, "");
        Assertions.assertEquals
                ("[{\"id\":27,\"nome\":\"Distrito Federal\",\"sigla\":\"DF\"}]", responseGet);
    }

    @Test
    @DisplayName("Listar Estados Com Busca Vazia")

    public void deveRetornarNoContentQuandoNaoHouverEstados() throws Exception {
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

        when(estadoEntityService.listar("asdfg")).thenReturn(new ArrayList<>());

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.get("/state")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("nome", "Distrito Federal")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Listar Estados Sem Autorização")
    public void naoDeveListarEstadosQuandoNaoForAutorizado() throws Exception {
        // gerando o token
        String body = "{\"login\":\"camilla\",\"senha\":\"camilla123\"}";

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
        estado.setId(27L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        when(estadoEntityService.listar("Distrito Federal")).thenReturn(List.of(estado));

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.get("/state")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("nome", "Distrito Federal")
                )
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Listar Estado Por ID Autorizado ")
    public void deveListarEstadoPorIdQuandoForAutorizado() throws Exception {
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

        when(estadoEntityService.listarPorId(1L)).thenReturn(estado);

        //executando controller com o token
        MvcResult resultGet = mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_estado}",1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseGet = resultGet.getResponse().getContentAsString();
        Assertions.assertNotEquals(responseGet, "");
        Assertions.assertEquals
                ("{\"id\":1,\"nome\":\"Distrito Federal\",\"sigla\":\"DF\"}", responseGet);
    }

    @Test
    @DisplayName("Não Listar Estado Por ID Sem Autorização ")
    public void naoDeveListarEstadoPorIdQuandoNaoForAutorizado() throws Exception {
        // gerando o token
        String body = "{\"login\":\"camilla\",\"senha\":\"camilla123\"}";

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

        when(estadoEntityService.listarPorId(1L)).thenReturn(estado);

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_estado}",1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isForbidden());
    }

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

        when(estadoEntityService.salvar(any(EstadoDTO.class))).thenReturn(1L);

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
        String body = "{\"login\":\"camilla\",\"senha\":\"camilla123\"}";

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

        when(estadoEntityService.salvar(any(EstadoDTO.class))).thenReturn(1L);

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
    @DisplayName("Deletar estado com autorização")
    public void deveDeletarEstadoQuandoForAutorizado() throws Exception{
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

        doNothing().when(estadoEntityService).deletar(estado.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/state/{id}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deletar estado sem autorização")
    public void naoDeveDeletarEstadoQuandoNaoForAutorizado() throws Exception{
        // gerando o token
        String body = "{\"login\":\"camilla\",\"senha\":\"camilla123\"}";

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

        doNothing().when(estadoEntityService).deletar(estado.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/state/{id}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isForbidden());
    }
}