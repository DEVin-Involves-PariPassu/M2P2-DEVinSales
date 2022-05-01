package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import br.com.senai.p2m02.devinsales.service.ItemVendaEntityService;
import br.com.senai.p2m02.devinsales.service.VendaEntityService;
import jakarta.persistence.EntityNotFoundException;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class VendaEntityControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

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
