package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;


    //para inserir um novo produto - usado no PostMapping
    public Long insert(@Valid ProductDTO productDTO) {
        ProductEntity product = new ProductEntity(productDTO.getId(),
                productDTO.getNome(),
                productDTO.getPreco_sugerido());
        productRepository.produtos.add(product);
        return product.getId();
    }

    //busca um produto pelo Id e retorna ele
    public ProductEntity findById(Long productId) {
        for (ProductEntity product : productRepository.produtos) {
            if (product.getId() == productId)
                return product;
        }
        return null;
    }

    public void isUniqueNomeProduct(ProductDTO productDTO) {
        Optional<ProductEntity> optionalProduct = productRepository.findByNome(productDTO.getNome());
        if(optionalProduct.isPresent()){
            throw new EntityExistsException("Já existe um produto cadastrado com o nome " + productDTO.getNome());
        }
    }


}
