package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.configuration.TokenService;
import br.com.senai.p2m02.devinsales.dto.ItemVendaDTO;
import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import br.com.senai.p2m02.devinsales.service.ItemVendaEntityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;



@RestController

@RequestMapping("/sales/{id_venda}/item")
public class ItemVendaEntityController {

    @Autowired
    private VendaEntityRepository vendaEntityRepository;
    @Autowired
    private ItemVendaEntityService service;
    @Autowired
    private ItemVendaEntityRepository itemVendaEntityRepository;
    @Autowired
    private ProductRepository productRepository;

    private ProductEntity productEntity;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @PostMapping
    public ResponseEntity<Long> post(

            @PathVariable(name = "id_venda") Integer idVenda,
            @Valid @RequestBody ItemVendaDTO itemVenda,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if (!loggedUser.canWrite("venda")) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        VendaEntity vendaEntity = vendaEntityRepository.findById(Long.valueOf(idVenda)).orElseThrow(
                () -> new EntityNotFoundException(HttpStatus.NOT_FOUND.name())
        );
        if(itemVenda.getId()==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ProductEntity productEntity = productRepository.findById(itemVenda.getId()).orElseThrow(
                () -> new EntityNotFoundException(HttpStatus.NOT_FOUND.name())
        );
        Integer idProduto = Math.toIntExact(itemVenda.getId());
        BigDecimal precoUnitario = itemVenda.getPrecoUnitario()!=null ? BigDecimal.valueOf(itemVenda.getPrecoUnitario()):productEntity.getPreco_sugerido();
        Integer quantidade = itemVenda.getQuantidade()!=null? itemVenda.getQuantidade() : 1;
        ItemVendaEntity item = itemVendaEntityRepository.findById(Long.valueOf(idProduto)).orElseThrow(
                () -> new EntityNotFoundException(HttpStatus.NOT_FOUND.name())
        );

        if(precoUnitario.intValue() <= 0| quantidade <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ItemVendaEntity itemVendaEntity = new ItemVendaEntity();
        itemVendaEntity.setVenda(vendaEntity);
        itemVendaEntity.setProduto(productEntity);
        itemVendaEntity.setQuantidade(quantidade);
        itemVendaEntity.setPrecoUnitario(precoUnitario);
        itemVendaEntityRepository.save(itemVendaEntity);
        return new ResponseEntity<>(itemVendaEntity.getId(), HttpStatus.CREATED);
    }

    @PatchMapping("/{id_item}/quantity/{quantity}")
    public ResponseEntity<Void> patchQuantidade(@PathVariable(name = "id_venda") Long idVenda,
                                                @PathVariable(name = "id_item") Long idItem,
                                                @PathVariable Integer quantity,
                                                @RequestHeader("Authorization") String auth) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if (!loggedUser.canWrite("venda")) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        service.patchQuantity(idVenda, idItem, quantity);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id_item}/price/{price}")
    public ResponseEntity<Object> updateItemVendaPrice(
            @PathVariable("id_venda") Long idVenda,
            @PathVariable("id_item") Long idItem,
            @PathVariable("price") BigDecimal price,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if (!loggedUser.canWrite("venda")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return service.patchPrice(idVenda, idItem, price);

    }
}
