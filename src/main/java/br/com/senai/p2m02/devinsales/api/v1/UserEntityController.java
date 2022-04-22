package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.dto.UserDTO;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Repository
public class UserEntityController {
   @Autowired
    private UserEntityService service;

    @GetMapping
    public ResponseEntity<List<UserEntity>> get(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String dtNascimentoMin,
            @RequestParam(required = false) String dtNascimentoMax,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ) {
        if (!loggedUser.canRead("usuario")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<UserEntity> userEntities = service.listar(nome, dtNascimentoMin, dtNascimentoMax);
        if (userEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userEntities);
    }

    @PostMapping
    public ResponseEntity<Long> post(@Valid @RequestBody UserDTO userDTO) {
        Long userId = service.salvar(userDTO);

        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @PatchMapping ("/{id_user}/feature/{nome_feature}/permissao/{tipo_permissao}")
    public ResponseEntity<Void> patchPermissao(
                        @PathVariable (name="id_user") Long idUser,
                        @PathVariable (name="nome_feature") String nomeFeature,
                        @PathVariable (name="tipo_permissao") String tipoPermissao,
                        @RequestAttribute("loggedUser") UserEntity loggedUser) {
        if (!loggedUser.canWrite("usuario")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.patchPermissao(idUser, nomeFeature, tipoPermissao);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  
    @PutMapping ("/{id_user}")
    public ResponseEntity<Void> put(@RequestAttribute("loggedUser") UserEntity user,
                                    @PathVariable (name = "id_user") Long idUser,
                                    @Valid @RequestBody UserDTO userDTO) {
        if (!user.canWrite("usuario")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.atualizar(idUser, userDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id_user}")
    public ResponseEntity<List<UserEntity>> delete(@RequestAttribute("loggedUser") UserEntity user,
                                                   @PathVariable (name = "id_user") Long userId)  {

        if( !user.canWrite("usuario") ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.delete(userId);

        return ResponseEntity.noContent().build();
    }
}