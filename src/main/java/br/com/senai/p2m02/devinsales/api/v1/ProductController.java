package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository repository;

    @Autowired
    ProductService service;

    @PostMapping
    public ResponseEntity<Long> post(@RequestAttribute("loggedUser") UserEntity loggedUser,
                                              @Valid @RequestBody ProductDTO productDTO) {
            if(!loggedUser.canWrite("produto")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Long productId = service.insert(productDTO);
            return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id_produto}")
    public ResponseEntity delete(@NotNull @PathVariable Long id_produto,
                                 @RequestAttribute("loggedUser") UserEntity loggedUser
    ){
        if(!loggedUser.canWrite("produto")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        service.delete(id_produto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(id_produto);
    }
}
