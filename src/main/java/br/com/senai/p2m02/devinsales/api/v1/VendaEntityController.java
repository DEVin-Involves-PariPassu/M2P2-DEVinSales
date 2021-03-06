package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.configuration.TokenService;
import br.com.senai.p2m02.devinsales.dto.DeliveryDTO;
import br.com.senai.p2m02.devinsales.dto.VendaDTO;
import br.com.senai.p2m02.devinsales.model.DeliveryEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.service.DeliveryEntityService;
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

    @Autowired
    private DeliveryEntityService deliveryService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @GetMapping("/{id_venda}")
    public ResponseEntity<VendaDTO> getById(
            @PathVariable(name = "id_venda") Long idVenda,
            @RequestHeader("Authorization") String auth
    ) {
        if (capturaTokenUsuarioEValidaLeitura(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        VendaDTO vendaDTO = service.listarPorId(idVenda);

        return ResponseEntity.ok(vendaDTO);
    }


    @GetMapping("user/{id_user}/sales")
    public ResponseEntity<List<VendaEntity>> get(
            @PathVariable(name = "id_user") Long idVendedor,
            @RequestHeader("Authorization") String auth
    ) {
        if (capturaTokenUsuarioEValidaLeitura(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        List<VendaEntity> vendaEntities = service.listarVendas(idVendedor);
        if (vendaEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vendaEntities);
    }


    @GetMapping("user/{id_user}/buy")
    public ResponseEntity<List<VendaEntity>> getmap(
            @PathVariable(name = "id_user") Long idComprador,
            @RequestHeader("Authorization") String auth
    ) {
        if (capturaTokenUsuarioEValidaLeitura(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        List<VendaEntity> vendaEntities = service.listarComprador(idComprador);
        if (vendaEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vendaEntities);
    }

    @PostMapping("/user/{id_user}/buy")
    public ResponseEntity<Long> postVenda(
            @Valid @RequestBody VendaEntity vendaEntity,
            @RequestHeader("Authorization") String auth,
            @PathVariable("id_user") Long idUser
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
        Long vendaId = service.salvarBuy(idUser, vendaEntity);
        return new ResponseEntity<>(vendaId, HttpStatus.CREATED);
    }

    @PostMapping("/user/{id_user}/sales")
    public ResponseEntity<Long> post(
            @PathVariable("id_user") Long idUser,
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody VendaEntity vendaEntity
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
        if (vendaEntity.getComprador() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long vendaId = service.salvarSale(idUser, vendaEntity);
        return new ResponseEntity<>(vendaId, HttpStatus.CREATED);
    }

    @GetMapping("/deliver")
    public ResponseEntity<List<DeliveryDTO>> getDeliveryList(
            @RequestParam(value = "id_endereco", required = false) Long idEndereco,
            @RequestParam(value = "id_venda", required = false) Long idVenda,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if (!loggedUser.canRead("vendas")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<DeliveryDTO> listDelivery = deliveryService.listar(idEndereco, idVenda);

        if(listDelivery.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(listDelivery);
    }


    @PostMapping("/{id_venda}/deliver")
    public ResponseEntity<Long> createDelivery(
            @RequestBody DeliveryEntity delivery,
            @PathVariable(name = "id_venda") Long idVenda,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if (!loggedUser.canWrite("entrega")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Long idEntrega = deliveryService.postEntrega(delivery, idVenda);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id_address}")
                .buildAndExpand(idEntrega).toUri();

        return ResponseEntity.created(location).body(idEntrega);
    }

    private boolean capturaTokenUsuarioEValidaLeitura(@RequestHeader("Authorization") String auth) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if (!loggedUser.canRead("venda")) {
            return true;
        }
        return false;
    }
}
