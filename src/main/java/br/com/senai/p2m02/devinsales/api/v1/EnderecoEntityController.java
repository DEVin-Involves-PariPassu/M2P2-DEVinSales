package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.service.EnderecoEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/state/{id_state}/city/{id_city}/address")
public class EnderecoEntityController {

    @Autowired
    private EnderecoEntityService service;

    @GetMapping
    public ResponseEntity<List<EnderecoEntity>> get(
            @PathVariable(name = "id_state") Long idEstado,
            @PathVariable(name = "id_city") Long idCidade,
            @RequestParam(required = false) String rua,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String complemento,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ) {
        if (!loggedUser.canRead("endereco")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<EnderecoEntity> enderecoEntities = service.listar(idCidade, idEstado, rua, numero, complemento);
        if (enderecoEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enderecoEntities);
    }

    @GetMapping("/{id_address}")
    public ResponseEntity<EnderecoEntity> getById(
            @PathVariable(name = "id_state") Long idEstado,
            @PathVariable(name = "id_city") Long idCidade,
            @PathVariable (name = "id_address") Long idEndereco,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ){
        if(!loggedUser.canRead("endereco")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        EnderecoEntity endereco = service.listarPorId(idCidade, idEstado, idEndereco);

        return ResponseEntity.ok(endereco);
    }

    @DeleteMapping("/{id_address}")
    public ResponseEntity <Void> delete(
            @PathVariable(name = "id_state") Long idEstado,
            @PathVariable(name = "id_city") Long idCidade,
            @PathVariable (name = "id_address") Long idEndereco,
            @RequestAttribute("loggedUser") UserEntity loggedUser
    ){
        if (!loggedUser.canWrite("endereco")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.deletar(idEstado, idCidade, idEndereco);

        return ResponseEntity.noContent().build();
    }

}
