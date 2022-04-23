package br.com.senai.p2m02.devinsales.api.v1;


import br.com.senai.p2m02.devinsales.configuration.TokenService;
import br.com.senai.p2m02.devinsales.dto.CidadeDTO;
import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.service.CidadeEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/state/{id_state}/city")
public class CidadeEntityController {

    @Autowired
    private CidadeEntityService service;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @GetMapping
    public ResponseEntity<List<CidadeEntity>> get(
            @PathVariable(name = "id_state") Long idEstado,
            @RequestParam(required = false) String nome,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario).orElseThrow(
                () -> new IllegalArgumentException()
        );

        if (!loggedUser.canRead("cidade")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<CidadeEntity> cidadeEntities = service.listar(nome, idEstado);
        if(cidadeEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cidadeEntities);

    }

    @GetMapping("/{id_city}")
    public ResponseEntity<CidadeEntity> getById(
            @PathVariable(name = "id_state") Long idEstado,
            @PathVariable(name = "id_city") Long idCidade,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario).orElseThrow(
                () -> new IllegalArgumentException()
        );

        if (!loggedUser.canRead("cidade")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        CidadeEntity cidadeEntity = service.listarPorId(idCidade,idEstado);

        return ResponseEntity.ok(cidadeEntity);
    }

    @PostMapping
    public ResponseEntity<Long> post(
            @Valid @RequestBody CidadeDTO cidade,
            @PathVariable(name = "id_state") Long idEstado,
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario).orElseThrow(
                () -> new IllegalArgumentException()
        );

        if (!loggedUser.canWrite("cidade")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Long idCidade = service.salvar(cidade, idEstado);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idCidade).toUri();

        return ResponseEntity.created(location).body(idCidade);

    }

        @DeleteMapping("/{id_city}")
        public ResponseEntity<Void> delete (
                @PathVariable(name = "id_state") Long idEstado,
                @PathVariable(name = "id_city") Long idCidade,
                @RequestHeader("Authorization") String auth
        ){

            String token = auth.substring(7);
            Long idUsuario = tokenService.getIdUsuario(token);
            UserEntity loggedUser = userEntityRepository.findById(idUsuario).orElseThrow(
                    () -> new IllegalArgumentException()
            );

            if (!loggedUser.canWrite("cidade")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            service.deletar(idCidade, idEstado);

            return ResponseEntity.noContent().build();
        }
}
