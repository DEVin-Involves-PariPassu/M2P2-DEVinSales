package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.configuration.TokenService;
import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository repository;

    @Autowired
    ProductService service;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserEntityRepository userEntityRepository;

    @PostMapping
    public ResponseEntity<Long> post(@RequestHeader("Authorization") String auth,
                                              @Valid @RequestBody ProductDTO productDTO) {

        if (capturaUsuarioLogadoPeloToken(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

           Long productId = service.insert(productDTO);

           return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/{id_produto}")
    public ResponseEntity delete(@NotNull @PathVariable Long id_produto,
                                 @RequestHeader("Authorization") String auth
    ){
        if (capturaUsuarioLogadoPeloToken(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        service.delete(id_produto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id_produto);
    }

    @PutMapping(value = "/{id_produto}")
    public ResponseEntity<Long> put(@NotNull @PathVariable Long id_produto,
                                    @RequestHeader("Authorization") String auth,
                                    @RequestBody ProductDTO productDTO) {

        if (capturaUsuarioLogadoPeloToken(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(id_produto == null){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        service.updateDoPut(id_produto, productDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PatchMapping("/{id_produto}")
    public ResponseEntity patch(@PathVariable Long id_produto,
                                @RequestHeader("Authorization") String auth,
                                @RequestBody ProductDTO productDTO) {

        if (capturaUsuarioLogadoPeloToken(auth)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(id_produto == null){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        service.updateDoPatch(id_produto, productDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private boolean capturaUsuarioLogadoPeloToken(@RequestHeader("Authorization") String auth) {
        String token = auth.substring(7);
        Long idUsuario = tokenService.getIdUsuario(token);
        UserEntity loggedUser = userEntityRepository.findById(idUsuario)
                .orElseThrow(
                        ()-> new IllegalArgumentException()
                );
        if(!loggedUser.canWrite("produto")){
            return true;
        }
        return false;
    }
}
