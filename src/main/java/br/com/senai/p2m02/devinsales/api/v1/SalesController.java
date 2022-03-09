package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import br.com.senai.p2m02.devinsales.service.ItemVendaEntityService;
import br.com.senai.p2m02.devinsales.service.VendaEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private VendaEntityService vendaService;

    @Autowired
    private ItemVendaEntityService itemVendaService;

    @Autowired
    private ItemVendaEntityRepository itemVendaRepository;

    @Autowired
    private VendaEntityRepository vendaRepository;

    @PatchMapping("/{id_venda}/item/{id_item}/price/{price}")
    public ResponseEntity<Object> updateItemVendaPrice(
            @PathVariable("id_venda") Long idVenda,
            @PathVariable("id_item") Long idItem,
            @PathVariable("price") BigDecimal price,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ) {

        if (!loggedUser.canWrite("venda")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<ItemVendaEntity> itemVendaToUpdateOpt = this.itemVendaRepository.findById(idItem);
        Optional<VendaEntity> vendaOpt = this.vendaRepository.findById(idVenda);

        if (vendaOpt.isEmpty() || itemVendaToUpdateOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ItemVendaEntity itemVendaToUpdate = itemVendaToUpdateOpt.get();
        VendaEntity venda = vendaOpt.get();
        if (!itemVendaToUpdate.getVenda().getId().equals(venda.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (price.intValue() <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        itemVendaToUpdate.setPrecoUnitario(price);
        itemVendaRepository.save(itemVendaToUpdate);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
