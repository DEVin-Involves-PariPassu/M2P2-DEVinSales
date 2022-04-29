package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.service.EnderecoEntityService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EnderecoControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EnderecoEntityService enderecoEntityService;

    @Test
    @DisplayName("Listar Endereços por ID Autorizado")
    public void deveListarEnderecosPorIDQuandoForAutorizado() throws Exception {

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

        EnderecoEntity endereco = new EnderecoEntity();
        CidadeEntity cidade = new CidadeEntity();
        EstadoEntity estado = new EstadoEntity();
        cidade.setId(1L);
        cidade.setNome("Florianopolis");
        cidade.setEstado(estado);
        estado.setId(1L);
        estado.setNome("Santa Catarina");
        estado.setSigla(SiglaEstado.SC);
        endereco.setId(1L);
        endereco.setRua("Rua Principal");
        endereco.setNumero(123);
        endereco.setCidade(cidade);
        endereco.setComplemento("Primavera Garden");

        when(enderecoEntityService.listarPorId (
                1L,
                1L,
                1L)).thenReturn(endereco);

        MvcResult resultGet = mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_state}/city/{id_city}/address/{id_address}",1L, 1L, 1l)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseGet = resultGet.getResponse().getContentAsString();
        Assertions.assertNotEquals(responseGet, "");
        Assertions.assertEquals
                ("{\"id\":1,\"rua\":\"Rua Principal\"," +
                        "\"numero\":123,\"complemento\":\"Primavera Garden\"," +
                        "\"cidade\":{\"id\":1,\"nome\":\"Florianopolis\"," +
                        "\"estado\":{\"id\":1,\"nome\":\"Santa Catarina\"," +
                        "\"sigla\":\"SC\"}}}", responseGet);
    }

    @Test
    @DisplayName("Listar Endereços com ID Cidade Inválido")
    public void deveRetornarBadRequestQuandoIdCidadeInvalido() throws Exception {

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

        EnderecoEntity endereco = new EnderecoEntity();

        when(enderecoEntityService.listarPorId(1L, 1L,1L)).thenReturn(endereco);

        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_state}/city/{id_city}/address/{id_address}","a",1L,1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Listar Endereços com ID Estado Inválido")
    public void deveRetornarBadRequestQuandoIdEstadoInvalido() throws Exception {

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

        EnderecoEntity endereco = new EnderecoEntity();

        when(enderecoEntityService.listarPorId(1L, 1L,1L)).thenReturn(endereco);


        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_state}/city/{id_city}/address/{id_address}",1L,"a",1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Listar Endereços com ID Endereço Inválido")
    public void deveRetornarBadRequestQuandoIdEnderecoInvalido() throws Exception {

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

        EnderecoEntity endereco = new EnderecoEntity();

        when(enderecoEntityService.listarPorId(1L, 1L,1L)).thenReturn(endereco);


        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_state}/city/{id_city}/address/{id_address}",1L,1L,"a")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Listar Endereços por ID Sem Autorização")
    public void naoDeveListarEnderecosPorIDQuandoNaoForAutorizado() throws Exception {

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

        EnderecoEntity endereco = new EnderecoEntity();
        CidadeEntity cidade = new CidadeEntity();
        EstadoEntity estado = new EstadoEntity();
        cidade.setId(1L);
        cidade.setNome("Florianopolis");
        cidade.setEstado(estado);
        estado.setId(1L);
        estado.setNome("Santa Catarina");
        estado.setSigla(SiglaEstado.SC);
        endereco.setId(1L);
        endereco.setRua("Rua Principal");
        endereco.setNumero(123);
        endereco.setCidade(cidade);
        endereco.setComplemento("Primavera Garden");

        when(enderecoEntityService.listarPorId (
                1L,
                1L,
                1L)).thenReturn(endereco);

        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_state}/city/{id_city}/address/{id_address}",1L, 1L, 1l)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Listar Endereços Autorizado")
    public void deveListarEnderecosQuandoForAutorizado() throws Exception {

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

        EnderecoEntity endereco = new EnderecoEntity();
        CidadeEntity cidade = new CidadeEntity();
        EstadoEntity estado = new EstadoEntity();

        cidade.setId(1L);
        cidade.setNome("Florianopolis");
        cidade.setEstado(estado);

        estado.setId(1L);
        estado.setNome("Santa Catarina");
        estado.setSigla(SiglaEstado.SC);

        endereco.setId(1L);
        endereco.setRua("Rua Principal");
        endereco.setNumero(123);
        endereco.setCidade(cidade);
        endereco.setComplemento("Primavera Garden");

        when(enderecoEntityService.listar (
                1L,
                1L,
                "Rua Principal",
                123,
                "Primavera Garden"
        )).thenReturn(List.of(endereco));

        MvcResult resultGet = mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_state}/city/{id_city}/address",1L, 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("rua", "Rua Principal")
                        .param("numero","123")
                        .param("complemento", "Primavera Garden")
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseGet = resultGet.getResponse().getContentAsString();
        Assertions.assertNotEquals(responseGet, "");
        Assertions.assertEquals
                ("[{\"id\":1,\"rua\":\"Rua Principal\"," +
                        "\"numero\":123,\"complemento\":\"Primavera Garden\"," +
                        "\"cidade\":{\"id\":1,\"nome\":\"Florianopolis\"," +
                        "\"estado\":{\"id\":1,\"nome\":\"Santa Catarina\"," +
                        "\"sigla\":\"SC\"}}}]", responseGet);
    }

    @Test
    @DisplayName("Listar Endereços com Busca Vazia")
    public void deveRetornarNoContentQuandoNaoHouverEndereco() throws Exception {

        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json")
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);

        EnderecoEntity endereco = new EnderecoEntity();
        CidadeEntity cidade = new CidadeEntity();
        EstadoEntity estado = new EstadoEntity();

        cidade.setId(1L);
        cidade.setNome("Florianopolis");
        cidade.setEstado(estado);

        estado.setId(1L);
        estado.setNome("Santa Catarina");
        estado.setSigla(SiglaEstado.SC);

        endereco.setId(1L);
        endereco.setRua("Rua Principal");
        endereco.setNumero(123);
        endereco.setCidade(cidade);
        endereco.setComplemento("Primavera Garden");

        when(enderecoEntityService.listar(
                2L,
                2L,
                "Rua Principal",
                123,
                "Primavera Garden"
        )).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_state}/city/{id_city}/address", 1L, 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .param("rua", "Rua Principal")
                        .param("numero", "123")
                        .param("complemento", "Primavera Garden")
                )
                .andExpect(status().isNoContent());
    }
}