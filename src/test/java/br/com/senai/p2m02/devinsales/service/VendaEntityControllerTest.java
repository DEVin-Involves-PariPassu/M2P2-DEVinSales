package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc

public class VendaEntityControllerTest {

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

    @LocalServerPort
    private int port;

    @Test
    public void getVenda1() throws Exception {

        when(vendaEntityRepository.findById(anyLong())).thenReturn(null);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/sales/1")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void getVenda2() throws Exception {

        when(vendaEntityRepository.findById(anyLong())).thenReturn(Optional.of(new VendaEntity()));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/sales/20")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void patchItensVenda1() throws Exception {
        ItemVendaEntity itemVendaEntity = new ItemVendaEntity();
        itemVendaEntity.setProduto(null);
        itemVendaEntity.setPrecoUnitario(null);
        itemVendaEntity.setQuantidade(12);

        List<ItemVendaEntity> list = List.of(itemVendaEntity);

        doThrow(EntityNotFoundException.class).when(itemVendaEntityService).patchQuantity(any(Long.class), any(Long.class), any(Integer.class));

        mockMvc
                .perform(MockMvcRequestBuilders.patch("/sales/10/venda/1")

                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")
                )
                .andExpect(status().isNotFound());

    }

    @Test
    public void patchItensVenda2() throws Exception {
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
                        .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void patchItensVenda3() throws Exception {
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
                        .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void patchItensVenda4() throws Exception {
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
                        .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void patchItensPrice() throws Exception {
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
                        .header("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")
                )
                .andExpect(status().isBadRequest());
    }
}
