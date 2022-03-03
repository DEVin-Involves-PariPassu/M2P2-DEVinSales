package br.com.senai.p2m02.devinsales.api.v1;


import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.service.EstadoEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/state")
public class EstadoEntityController {

@Autowired
private EstadoEntityService service;

@GetMapping
public ResponseEntity<List<EstadoEntity>> get(
        @RequestParam(required = false) String nome,
        @RequestAttribute("loggedUser") UserEntity loggedUser)
{
    if(!loggedUser.canRead("estado")){
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    List<EstadoEntity> estadoEntities = service.listar(nome, loggedUser);
    return ResponseEntity.ok(estadoEntities);
}

}
