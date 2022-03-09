package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.service.ItemVendaEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/sales/{id_venda}/item/{id_item}/")
public class ItemVendaEntityController {

    @Autowired
    private ItemVendaEntityService service;

    @PatchMapping("/quantity/{quantity}")
    public ResponseEntity<Void> patchQuantidade(@PathVariable(name = "id_venda") Long idVenda,
                                                @PathVariable(name = "id_item") Long idItem,
                                                @PathVariable Integer quantity,
                                                @RequestAttribute("loggedUser") UserEntity loggedUser) {
        if (!loggedUser.canWrite("venda")) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        service.patchQuantity(idVenda, idItem, quantity);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/price/{price}")
    public ResponseEntity<Object> updateItemVendaPrice(
            @PathVariable("id_venda") Long idVenda,
            @PathVariable("id_item") Long idItem,
            @PathVariable("price") BigDecimal price,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ) {

        if (!loggedUser.canWrite("venda")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return service.patchPrice(idVenda, idItem, price);

    }
}
