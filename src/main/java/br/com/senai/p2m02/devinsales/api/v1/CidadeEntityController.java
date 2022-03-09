package br.com.senai.p2m02.devinsales.api.v1;


import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.service.CidadeEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/state/{id_state}/city")
public class CidadeEntityController {

    @Autowired
    private CidadeEntityService service;

    @GetMapping
    public ResponseEntity<List<CidadeEntity>> get(
            @PathVariable(name = "id_state") Long idEstado,
            @RequestParam(required = false) String nome,
            @RequestAttribute("loggedUser") UserEntity loggedUser) {
        if (!loggedUser.canRead("cidade")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<CidadeEntity> cidadeEntities = service.listar(nome, idEstado);
        if(cidadeEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cidadeEntities);

    }

    @DeleteMapping("/{id_city}")
    public  ResponseEntity<Void> delete(
            @PathVariable(name = "id_state") Long idEstado,
            @PathVariable(name = "id_city") Long idCidade,
            @RequestAttribute("loggedUser") UserEntity loggedUser) {
        if (!loggedUser.canWrite("cidade")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.deletar(idCidade, idEstado);

        return ResponseEntity.noContent().build();
    }


}
