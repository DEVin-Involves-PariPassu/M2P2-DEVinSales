package br.com.senai.p2m02.devinsales.api.v1;


import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.service.EstadoEntityService;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/state")
public class EstadoEntityController {

    @Autowired
    private EstadoEntityService service;

    @GetMapping
    public ResponseEntity<List<EstadoEntity>> get(
            @RequestParam(required = false) String nome,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ) {
        if (!loggedUser.canRead("estado")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<EstadoEntity> estadoEntities = service.listar(nome);
        if (estadoEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadoEntities);
    }

    @PostMapping
    public ResponseEntity<Long> post(
            @Valid @RequestBody EstadoDTO estado,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ) throws RequiredFieldMissingException {
        if (!loggedUser.canWrite("estado")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Long idEstado = service.salvar(estado);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idEstado).toUri();

        return ResponseEntity.created(location).body(idEstado);
    }

}
