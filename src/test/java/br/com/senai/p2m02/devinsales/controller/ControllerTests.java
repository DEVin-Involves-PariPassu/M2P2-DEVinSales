package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.dto.CidadeDTO;
import br.com.senai.p2m02.devinsales.dto.EnderecoDTO;
import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.*;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import br.com.senai.p2m02.devinsales.service.*;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EstadoEntityService estadoEntityService;

    @MockBean
    CidadeEntityService cidadeEntityService;

    @MockBean
    EnderecoEntityService enderecoEntityService;

    @MockBean
    ProductService productService;

    @Mock
    private VendaEntityService vendaEntityService;

    @Mock
    private ItemVendaEntityService itemVendaEntityService;

    @Mock
    private VendaEntityRepository vendaEntityRepository;

    @Mock
    private ItemVendaEntityRepository itemVendaEntityRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void initDB() {
        ProductEntity produto = new ProductEntity();
        produto.setNome("batata");
        produto.setPreco_sugerido(BigDecimal.valueOf(3.50));
        produto.setId(1L);
        productRepository.save(produto);
    }

    /*
    *
    * EstadoControllerTests
    *
     */

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

    /*
    *
    * CidadeControllerTests
    *
     */

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

        when(cidadeEntityService.listar(cidade.getNome(), estado.getId())).thenReturn((List.of(cidade)));

        //executando controller com o token
        MvcResult resultGet = mockMvc
                .perform(MockMvcRequestBuilders.get("/state/{id_estado}/city",1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("nome", cidade.getNome())
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseGet = resultGet.getResponse().getContentAsString();
        Assertions.assertNotEquals(responseGet, "");
        Assertions.assertEquals("[{\"id\":1,\"nome\":\"Rio Branco\",\"estado\":{\"id\":1,\"nome\":\"Acre\",\"sigla\":\"AC\"}}]", responseGet);
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

        when(cidadeEntityService.listar(cidade.getNome(), estado.getId())).thenReturn((List.of(cidade)));

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_estado}/city",1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("nome", cidade.getNome())
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

        when(cidadeEntityService.listarPorId(cidade.getId(), estado.getId())).thenReturn(cidade);

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

        when(cidadeEntityService.listarPorId(cidade.getId(), estado.getId())).thenReturn(cidade);

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

        when(cidadeEntityService.salvar(any(CidadeDTO.class), eq(estado.getId()))).thenReturn(1L);

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

        when(cidadeEntityService.salvar(any(CidadeDTO.class), eq(estado.getId()))).thenReturn(1L);

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

        doNothing().when(cidadeEntityService).deletar(cidade.getId(), estado.getId());

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

        doNothing().when(cidadeEntityService).deletar(cidade.getId(), estado.getId());

        //executando controller com o token
        mockMvc.perform(MockMvcRequestBuilders.delete("/state/{id_estado}/city/{id_cidade}",1L, 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                        .param("nome", "Distrito Federal")
                )
                .andExpect(status().isForbidden());
    }

    /*
    *
    * EnderecoControllerTests
    *
     */

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

    @Test
    @DisplayName("Listar Endereços sem Autorização")
    public void naoDeveListarEnderecoQuandoNaoForAutorizado() throws Exception {

        String body = "{\"login\":\"camilla\",\"senha\":\"camilla123\"}";

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
        )).thenReturn(List.of(endereco));

        mockMvc.perform(MockMvcRequestBuilders.get("/state/{id_state}/city/{id_city}/address", 1L, 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .param("rua", "Rua Principal")
                        .param("numero", "123")
                        .param("complemento", "Primavera Garden")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Salvar endereço com autorização")
    public void deveSalvarEnderecoQuandoForAutorizado() throws Exception {
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
        estado.setNome("Santa Catarina");
        estado.setSigla(SiglaEstado.SC);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Florianopolis");
        cidade.setEstado(estado);

        EnderecoEntity endereco = new EnderecoEntity();
        endereco.setId(1L);
        endereco.setRua("Rua Principal");
        endereco.setNumero(123);
        endereco.setComplemento("Primavera Garden");
        endereco.setCidade(cidade);

        when(enderecoEntityService.salvar(any(EnderecoDTO.class), eq(cidade.getId()), eq(estado.getId()))).thenReturn(1L);

        String bodyRequisicao = "{\"rua\":\"Rua Principal\"," +
                "\"numero\":123,\"complemento\":\"Primavera Garden\"," +
                "\"cidade\":{\"id\":1,\"nome\":\"Florianopolis\"," +
                "\"estado\":{\"id\":1,\"nome\":\"Santa Catarina\"," +
                "\"sigla\":\"SC\"}}}";

        MvcResult resultPost = mockMvc.perform(MockMvcRequestBuilders.post("/state/{id_estado}/city/{id_city}/address", 1L, 1L)
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
    @DisplayName("Não salvar endereço sem autorização")
    public void naoDeveSalvarEnderecoQuandoNaoForAutorizado() throws Exception {
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
        estado.setNome("Santa Catarina");
        estado.setSigla(SiglaEstado.SC);

        CidadeEntity cidade = new CidadeEntity();
        cidade.setId(1L);
        cidade.setNome("Florianopolis");
        cidade.setEstado(estado);

        EnderecoEntity endereco = new EnderecoEntity();
        endereco.setId(1L);
        endereco.setRua("Rua Principal");
        endereco.setNumero(123);
        endereco.setComplemento("Primavera Garden");
        endereco.setCidade(cidade);

        when(enderecoEntityService.salvar(any(EnderecoDTO.class), eq(cidade.getId()), eq(estado.getId()))).thenReturn(1L);

        String bodyRequisicao = "{\"rua\":\"Rua Principal\"," +
                "\"numero\":123,\"complemento\":\"Primavera Garden\"," +
                "\"cidade\":{\"id\":1,\"nome\":\"Florianopolis\"," +
                "\"estado\":{\"id\":1,\"nome\":\"Santa Catarina\"," +
                "\"sigla\":\"SC\"}}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/state/{id_estado}/city/{id_city}/address", 1L, 1L)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json" )
                .content(bodyRequisicao)
        ).andExpect(status().isForbidden());
    }

    /*
    *
    * ProductControllerTests
    *
     */

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

        when(productService.insert(any(ProductDTO.class))).thenReturn(1L);

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
        when(productService.insert(any(ProductDTO.class))).thenThrow(new RequiredFieldMissingException("O nome do produto é obrigatório."));

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

        when(productService.insert(any(ProductDTO.class))).thenThrow(new RequiredFieldMissingException("O valor do produto é obrigatório."));

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
        when(productService.insert(any(ProductDTO.class))).thenThrow(new IllegalArgumentException("Valor do produto inválido."));

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
        when(productService.insert(any(ProductDTO.class))).thenThrow(new IllegalArgumentException("Valor do produto inválido."));

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

        when(productService.updateDoPut(produto.getId(), productDTO)).thenReturn(1L);

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

        when(productService.updateDoPatch(produto.getId(), productDTO)).thenReturn(1L);

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


        doThrow(EntityNotFoundException.class).when(productService).updateDoPut(null, productDTO);

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

        doNothing().when(productService).delete(produto.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id_produto}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /*
    *
    * VendaEntityControllerTests
    *
     */

    @Test
    @DisplayName("Caso exista a venda deve-se retornar o status de sucesso")
    public void casoExistaVendaDeveRetornarStatusSucesso() throws Exception {
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

        when(vendaEntityRepository.findById(anyLong())).thenReturn(null);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/sales/1")
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Caso não exista venda com id_venda e retorna exceção")
    public void casoNaoExistaVendaIdVendaRetornaExcecao() throws Exception {
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

        when(vendaEntityRepository.findById(anyLong())).thenReturn(Optional.of(new VendaEntity()));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/sales/20")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Não atualiza caso não exista uma venda com id_venda ou item_venda com id_item enviado")
    public void naoAtualizaCasoNaoExistaUmaVendaComIdVendaOuItem_vendaComIdItemEnviado() throws Exception {
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

        ItemVendaEntity itemVendaEntity = new ItemVendaEntity();
        itemVendaEntity.setProduto(null);
        itemVendaEntity.setPrecoUnitario(null);
        itemVendaEntity.setQuantidade(12);

        List<ItemVendaEntity> list = List.of(itemVendaEntity);

        doThrow(EntityNotFoundException.class).when(itemVendaEntityService).patchQuantity(any(Long.class), any(Long.class), any(Integer.class));

        mockMvc
                .perform(MockMvcRequestBuilders.patch("/sales/10/venda/1")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Não atualiza caso seja enviado um id_item cuja venda seja diferente do id_venda enviado e retorna exceção")
    public void naoAtualizaCasoSejaEnviadoUmIdItemCujaVendaSejaDiferenteDoIdVendaEnviadoRetornaExcecao() throws Exception {
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

        ItemVendaEntity itemVendaEntity = new ItemVendaEntity();
        VendaEntity vendaEntity = new VendaEntity();
        VendaEntity vendaEntity2 = new VendaEntity();

        vendaEntity.setId(1l);
        vendaEntity2.setId(2l);

        itemVendaEntity.setId(1l);
        itemVendaEntity.setProduto(null);
        itemVendaEntity.setPrecoUnitario(null);
        itemVendaEntity.setQuantidade(12);
        itemVendaEntity.setVenda(vendaEntity2);

        List<ItemVendaEntity> list = List.of(itemVendaEntity);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new ProductEntity()));
        when(itemVendaEntityRepository.findById((anyLong()))).thenReturn(Optional.of(itemVendaEntity));
        when(vendaEntityRepository.findById(anyLong())).thenReturn(Optional.of(vendaEntity));


        mockMvc
                .perform(MockMvcRequestBuilders.patch("/sales/2/item/1/quantity/2")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Não atualiza caso quantidade dos itens seja <= 0 e retorna exceção")
    public void naoAtualizaCasoQuantidadeDosItensSejaMenorIgualZeroRetornaExcecao() throws Exception {
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

        ItemVendaEntity itemVendaEntity = new ItemVendaEntity();
        VendaEntity vendaEntity = new VendaEntity();
        VendaEntity vendaEntity2 = new VendaEntity();

        vendaEntity.setId(1l);
        vendaEntity2.setId(2l);

        itemVendaEntity.setId(1l);
        itemVendaEntity.setProduto(null);
        itemVendaEntity.setPrecoUnitario(null);
        itemVendaEntity.setQuantidade(12);
        itemVendaEntity.setVenda(vendaEntity2);

        List<ItemVendaEntity> list = List.of(itemVendaEntity);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new ProductEntity()));
        when(itemVendaEntityRepository.findById((anyLong()))).thenReturn(Optional.of(new ItemVendaEntity()));
        when(vendaEntityRepository.findById(anyLong())).thenReturn(Optional.of(new VendaEntity()));

        mockMvc
                .perform(MockMvcRequestBuilders.patch("/sales/2/item/1/quantity/-2")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)

                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Caso a atualização ocorra com sucesso")
    public void casoAtualizacaoOcorraComSucesso() throws Exception {
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

        ItemVendaEntity itemVendaEntity = new ItemVendaEntity();
        VendaEntity vendaEntity = new VendaEntity();
        VendaEntity vendaEntity2 = new VendaEntity();

        vendaEntity.setId(1l);
        vendaEntity2.setId(2l);

        itemVendaEntity.setId(1l);
        itemVendaEntity.setProduto(null);
        itemVendaEntity.setPrecoUnitario(null);
        itemVendaEntity.setQuantidade(12);
        itemVendaEntity.setVenda(vendaEntity2);

        List<ItemVendaEntity> list = List.of(itemVendaEntity);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new ProductEntity()));
        when(itemVendaEntityRepository.findById((anyLong()))).thenReturn(Optional.of(itemVendaEntity));
        when(vendaEntityRepository.findById(anyLong())).thenReturn(Optional.of(vendaEntity));

        mockMvc
                .perform(MockMvcRequestBuilders.patch("/sales/1/item/1/quantity/2")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Não atualiza caso preço dos itens seja <= 0 e retorna exceção")
    public void naoAtualizaCasoPrecoDosItensSejaMenorIgualZeroRetornaExcecao() throws Exception {
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

        ItemVendaEntity itemVendaEntity = new ItemVendaEntity();
        VendaEntity vendaEntity = new VendaEntity();
        VendaEntity vendaEntity2 = new VendaEntity();

        vendaEntity.setId(1l);
        vendaEntity2.setId(2l);

        itemVendaEntity.setId(1l);
        itemVendaEntity.setProduto(null);
        itemVendaEntity.setPrecoUnitario(null);
        itemVendaEntity.setQuantidade(12);
        itemVendaEntity.setVenda(vendaEntity2);

        List<ItemVendaEntity> list = List.of(itemVendaEntity);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new ProductEntity()));
        when(itemVendaEntityRepository.findById((anyLong()))).thenReturn(Optional.of(new ItemVendaEntity()));
        when(vendaEntityRepository.findById(anyLong())).thenReturn(Optional.of(new VendaEntity()));

        mockMvc
                .perform(MockMvcRequestBuilders.patch("/sales/2/item/1/price/-2")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isBadRequest());
    }
}
