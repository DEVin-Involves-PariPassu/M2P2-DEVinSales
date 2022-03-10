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

@RestController
@RequestMapping("/user")
@Repository
public class UserEntityController {
    @Autowired
    UserEntityService service;

    @PostMapping
    public ResponseEntity<Long> post(@RequestAttribute("loggedUser") UserEntity user,
                                     @Valid @RequestBody UserDTO userDTO) {
        if (!user.canWrite("usuario")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Long userId = service.salvar(userDTO);

        return new ResponseEntity<>(userId, HttpStatus.CREATED);
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
}
