package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository repository;

    @DeleteMapping("/{id_produto}")
    public ResponseEntity<Void> delete(@PathVariable Long id_produto,
                                       @RequestAttribute("loggedUser") UserEntity loggedUser) {
        if (!loggedUser.canWrite("produto")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<ProductEntity> optionalProduto = repository.findById(id_produto);
        if (optionalProduto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.delete(optionalProduto.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
