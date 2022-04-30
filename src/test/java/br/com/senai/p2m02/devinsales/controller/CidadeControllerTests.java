package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.dto.CidadeDTO;
import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.service.CidadeEntityService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CidadeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CidadeEntityService service;

    @Test
    @DisplayName("Listar Cidades Por Estado Com Autorização")
    public void deveListarCidadesPorEstadoQuandoForAutorizado() throws Exception {

        // gerando o token
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body)
                )
                .andExpect(status().isOk()).andReturn();


        // extraindo o token
        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Acre");
        estado.setSigla(SiglaEstado.AC);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Rio Branco");
        cidade.setEstado(estado);

        when(service.listar(estado.getNome(), estado.getId())).thenReturn((List<CidadeEntity>) cidade);

        //executando controller com o token
        MvcResult resultGet = mockMvc
                .perform(MockMvcRequestBuilders.get("/state/{id_estado}/city",1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseGet = resultGet.getResponse().getContentAsString();
        Assertions.assertNotEquals(responseGet, "");
        Assertions.assertEquals("{\"id\":1,\"nome\":\"Rio Branco\",\"estado\":{\"id\":1,\"nome\":\"Acre\",\"sigla\":\"AC\"}}", responseGet);
    }

    @Test
    @DisplayName("Não Listar Cidades Por Estado Sem Autorização")
    public void naoDeveListarCidadesPorEstadoQuandoNaoForAutorizado() throws Exception {

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
        estado.setNome("Acre");
        estado.setSigla(SiglaEstado.AC);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Rio Branco");
        cidade.setEstado(estado);

        when(service.listar(estado.getNome(), estado.getId())).thenReturn((List<CidadeEntity>) cidade);

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_estado}/city",1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Listar Cidade Por ID Autorizado ")
    public void deveListarCidadePorIdQuandoForAutorizado() throws Exception {
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

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Brasilia");
        cidade.setEstado(estado);

        when(service.listarPorId(cidade.getId(), estado.getId())).thenReturn(cidade);

        //executando controller com o token
        MvcResult resultGet = mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_estado}/city/{id_cidade}",1L,1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseGet = resultGet.getResponse().getContentAsString();
        Assertions.assertNotEquals(responseGet, "");
        Assertions.assertEquals
                ("{\"id\":1,\"nome\":\"Brasilia\",\"estado\":{\"id\":1,\"nome\":\"Distrito Federal\",\"sigla\":\"DF\"}}", responseGet);
    }

    @Test
    @DisplayName("Não Listar Cidade Por ID Sem Autorização")
    public void naoDeveListarCidadePorIdQuandoNaoForAutorizado() throws Exception {
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

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Brasilia");
        cidade.setEstado(estado);

        when(service.listarPorId(cidade.getId(), estado.getId())).thenReturn(cidade);

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_estado}/city/{id_cidade}",1L,1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Salvar cidade com autorização")
    public void deveSalvarCidadeQuandoForAutorizado() throws Exception {
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

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Brasilia");
        cidade.setEstado(estado);

        when(service.salvar(any(CidadeDTO.class), eq(estado.getId()))).thenReturn(1L);

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"Brasília\",\n" +
                "    \"estadoId\":1\n" +
                "}";

        MvcResult resultPost = mockMvc.perform(MockMvcRequestBuilders.post("/state/{id_estado}/city", 1L)
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
    @DisplayName("Não salvar cidade sem autorização")
    public void naoDeveSalvarCidadeQuandoNaoForAutorizado() throws Exception {
        String body = "{\"login\":\"camilla\",\"senha\":\"camilla123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        EstadoEntity estado = new EstadoEntity();
        estado.setId(1L);
        estado.setNome("Distrito Federal");
        estado.setSigla(SiglaEstado.DF);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Brasilia");
        cidade.setEstado(estado);

        when(service.salvar(any(CidadeDTO.class), eq(estado.getId()))).thenReturn(1L);

        String bodyRequisicao = "{\n" +
                "    \"nome\":\"Brasília\",\n" +
                "    \"estadoId\":1\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/state/{id_estado}/city", 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .content(bodyRequisicao)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deletar cidade autorizado")
    public void deveDeletarCidadeQuandoForAutorizado() throws Exception {
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

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Brasilia");
        cidade.setEstado(estado);

        doNothing().when(service).deletar(cidade.getId(), estado.getId());

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.delete("/state/{id_estado}/city/{id_cidade}",1L, 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("nome", "Distrito Federal")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Não deletar cidade sem autorização")
    public void naoDeveDeletarCidadeQuandoNaoForAutorizado() throws Exception {
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

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Brasilia");
        cidade.setEstado(estado);

        doNothing().when(service).deletar(cidade.getId(), estado.getId());

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.delete("/state/{id_estado}/city/{id_cidade}",1L, 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("nome", "Distrito Federal")
                )
                .andExpect(status().isForbidden());
    }
}
