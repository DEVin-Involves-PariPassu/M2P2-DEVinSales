package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Long insert(ProductDTO productDTO){
        ProductEntity product = validationsPost(productDTO);
        productRepository.save(product);
        return product.getId();
    }

    private ProductEntity validationsPost(ProductDTO productDTO){
        existsNome(productDTO);
        isUniqueNomeProduct(productDTO);
        existsPreco(productDTO);
        precoValido(productDTO);

        ProductEntity product = new ProductEntity();
        product.setNome(productDTO.getNome());
        product.setPreco_sugerido(productDTO.getPreco_sugerido());

        return product;
    }

    private void precoValido(ProductDTO productDTO){
        if(productDTO.getPreco_sugerido().compareTo(BigDecimal.ZERO) <= 0){
            throw new RequiredFieldMissingException("Valor do produto inválido.");
        }
    }

    private void existsPreco(ProductDTO productDTO){
        if(productDTO.getPreco_sugerido() == null){
            throw new RequiredFieldMissingException("O valor do produto é obrigatório.");
        }
    }

    private void isUniqueNomeProduct(ProductDTO productDTO) {
        Optional<ProductEntity> optionalProduct = productRepository.findByNome(productDTO.getNome());
        if(optionalProduct.isPresent()){
            throw new EntityExistsException("Já existe um produto cadastrado com o nome " + productDTO.getNome());
        }
    }

    private void existsNome(ProductDTO productDTO){
        if(productDTO.getNome().isBlank()){
            throw new RequiredFieldMissingException("O nome do produto é obrigatório.");
        }
    }

}
