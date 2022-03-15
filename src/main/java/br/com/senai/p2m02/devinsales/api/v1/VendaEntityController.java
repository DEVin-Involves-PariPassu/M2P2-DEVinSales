package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.DeliveryEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import br.com.senai.p2m02.devinsales.service.VendaEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class VendaEntityController {

    @Autowired
    private VendaEntityService service;

    @GetMapping("/{id_venda}")
    public ResponseEntity<VendaEntity> getById(
            @PathVariable(name = "id_venda") Long idVenda,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ) {
        if (!loggedUser.canRead("venda")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        VendaEntity vendaEntity = service.listarPorId(idVenda);

        return ResponseEntity.ok(vendaEntity);
    }

    @GetMapping("user/{id_user}/sales")
    public ResponseEntity<List<VendaEntity>> get (
            @PathVariable (name = "id_user") Long idVendedor,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ){
        if (!loggedUser.canRead("venda")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<VendaEntity> vendaEntities = service.listarVendas(idVendedor);
        if (vendaEntities.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vendaEntities);
    }

    @PostMapping("/user/{id_user}/buy")
    public ResponseEntity<Long> postVenda(
            @Valid @RequestBody VendaEntity vendaEntity,
            @RequestAttribute("loggedUser") UserEntity loggedUser,
            @PathVariable("id_user") Long idUser
    ){
        if(!loggedUser.canWrite("venda")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Long vendaId = service.salvar(idUser, vendaEntity);
        return new ResponseEntity<>(vendaId, HttpStatus.CREATED);
    }

    @PostMapping("/user/{id_user}/sales")
    public ResponseEntity<Long> post(
            @PathVariable("id_user") Long idUser,
            @RequestAttribute("loggedUser") UserEntity loggedUser,
            @RequestAttribute(required = false) String dataVenda
    ) {
        if (!loggedUser.canRead("venda")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(idUser, HttpStatus.CREATED);
    }

    @PostMapping("/{id_venda}/deliver")
    public ResponseEntity<Long> createDelivery(
            @RequestBody DeliveryEntity delivery,
            @PathVariable(name = "id_venda") Long idVenda,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    )  {
        if (!loggedUser.canWrite("entrega")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Long idEntrega = service.postEntrega(delivery, idVenda);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id_address}")
                .buildAndExpand(idEntrega).toUri();

        return ResponseEntity.created(location).body(idEntrega);
    }

}
