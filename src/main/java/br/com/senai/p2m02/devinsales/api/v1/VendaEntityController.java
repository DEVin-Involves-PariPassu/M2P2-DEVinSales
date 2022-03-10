package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.service.VendaEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

}
