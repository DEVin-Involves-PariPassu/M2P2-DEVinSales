package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import br.com.senai.p2m02.devinsales.service.UserEntityService;
import br.com.senai.p2m02.devinsales.service.VendaEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;


@RestController
@RequestMapping("/sales")
public class VendaEntityController {

    @Autowired
    private VendaEntityService service;

    @Autowired
    private VendaEntityRepository repository;

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

    @PostMapping("/user/{id_user}/buy")
    public ResponseEntity<Long> postVenda(
            @Valid @RequestBody VendaEntity vendaEntity,
            @RequestAttribute("loggedUser") UserEntity loggedUser,
            @PathVariable("id_user") Long id_user
    ){
        if(!loggedUser.canWrite("venda")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(!service.hasUserId(id_user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else if(vendaEntity.getVendedor() != null){
            if(!service.hasUserId(vendaEntity.getVendedor().getId())){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        UserEntity comprador = service.getUser(id_user);
        vendaEntity.setComprador(comprador); //OK

        service.salvar(vendaEntity);
        Long vendaId = vendaEntity.getId();
        return new ResponseEntity<>(vendaId, HttpStatus.CREATED);
    }


}
