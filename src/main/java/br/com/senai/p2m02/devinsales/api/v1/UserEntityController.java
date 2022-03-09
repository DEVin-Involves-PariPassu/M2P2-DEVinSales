package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.service.UserService;
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
    UserService service;
/* Criar UserDTO com esses parâmetros:
Entrada: Body parameter (@RequestBody)
{
login: string,
senha: string,
nome: string,
dtNascimento: string (formato dd/mm/yyyy),
features:
[
{
feature: string,
read: boolean,
reawrite: boolean
}
]
}
*/

    @PostMapping
    public ResponseEntity<Long> post(@Valid @RequestBody UserEntity user//,
            /*@Valid @RequestBody UserDTO userDTO*/) {
        // usuário logado deve possuir write para a Feature de Usuario.
        // Caso não possua, deve-se retornar o Status de Erro 403 (Forbidden)
        if (!user.canWrite("usuario")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        //se não houver nenhum erro, deve-se criar um usuário na
        //tabela de USUARIO, bem como devem ser criados n registros de Usuario_Feature,
        //de acordo com as features enviadas

        Long userId = service.salvar(user);

        //O retorno em caso de sucesso deve ser o Status 201 (Created),
        //bem como deve-se retornar o Id do usuário criado.
        return new ResponseEntity<>(userId, HttpStatus.CREATED);


    }

}
