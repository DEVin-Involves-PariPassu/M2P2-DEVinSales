package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserEntityController {

    @PatchMapping ("/{id_user}/feature/{nome_feature}/permissao/{tipo_permissao}")
    public ResponseEntity<Void> patchPermissao(@PathVariable Integer id_user,
                        @PathVariable String nome_feature,
                        @PathVariable String tipo_permissao,
                        @RequestAttribute("loggedUser") UserEntity loggedUser) {
        if (!loggedUser.canWrite("usuario")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
