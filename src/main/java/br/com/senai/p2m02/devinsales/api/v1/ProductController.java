package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.repository.ProductEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductEntityRepository repository;

}
