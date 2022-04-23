package br.com.senai.p2m02.devinsales.api.v1;


import br.com.senai.p2m02.devinsales.configuration.TokenService;
import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
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

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @GetMapping
    public ResponseEntity<List<EstadoEntity>> get(
            @RequestParam(required = false) String nome,
            @RequestHeader("Authorization") String auth
    ) {
        if (capturaTokenUsuarioEValidaLeitura(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        List<EstadoEntity> estadoEntities = service.listar(nome);
        if (estadoEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadoEntities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoEntity> getById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth
    ){
        if (capturaTokenUsuarioEValidaLeitura(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        EstadoEntity estado = service.listarPorId(id);

        return ResponseEntity.ok(estado);
    }



//    @PostMapping
//    public ResponseEntity<Long> post(
//            @Valid @RequestBody EstadoDTO estado,
//            @RequestAttribute("loggedUser") UserEntity loggedUser
//    ) throws RequiredFieldMissingException {
//        if (!loggedUser.canWrite("estado")) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        Long idEstado = service.salvar(estado);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(idEstado).toUri();
//
//        return ResponseEntity.created(location).body(idEstado);
//    }

    @PostMapping
    public ResponseEntity<Long> post(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody EstadoDTO estado
    ) {
        //pega usuario logado
        if (capturaTokenUsuarioEValidaEscrita(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Long idEstado = service.salvar(estado);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idEstado).toUri();

        return ResponseEntity.created(location).body(idEstado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth
    ){
        if (capturaTokenUsuarioEValidaEscrita(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    private boolean capturaTokenUsuarioEValidaLeitura(@RequestHeader("Authorization") String auth) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if(!loggedUser.canRead("estado")){
            return true;
        }
        return false;
    }

    private boolean capturaTokenUsuarioEValidaEscrita(@RequestHeader("Authorization") String auth) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if (!loggedUser.canWrite("estado")) {
            return true;
        }
        return false;
    }
}
