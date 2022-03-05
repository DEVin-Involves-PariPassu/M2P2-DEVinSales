package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository repository;

    @Autowired
    ProductService service;


    @PostMapping
    public ResponseEntity <Long> post(@RequestAttribute("loggedUser") UserEntity loggedUser,
                                      @Valid @RequestBody ProductDTO productDTO) {

            if(!loggedUser.canWrite("produto")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (productDTO.getNome().isBlank() || productDTO.getPreco_sugerido() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if(productDTO.getNome().isBlank() == false){
                service.isUniqueNomeProduct(productDTO);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (productDTO.getPreco_sugerido().compareTo(BigDecimal.ZERO) <= 0){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Long productId = service.insert(productDTO);
            return new ResponseEntity<>(productId, HttpStatus.CREATED);

    }

}
