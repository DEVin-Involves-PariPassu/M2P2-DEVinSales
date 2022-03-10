package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.ProductDTO;
import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.ProductRepository;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ItemVendaEntityRepository itemVendaRepository;

    public Long insert(ProductDTO productDTO){
        ProductEntity product = validationsPost(productDTO);
        productRepository.save(product);
        return product.getId();
    }

    public ProductEntity findById(Long productId) {
        for (ProductEntity product : productRepository.findAll()) {
            if (product.getId() == productId)
                return product;
        }
        return null;
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

    public void existsById(Long id_produto){
        ProductEntity product = findById(id_produto);
        if (product == null) {
            throw new EntityNotFoundException("Não há nenhum produto com este id");
        }
    }

    public void existsItemVenda(ProductEntity product){
        Optional<ItemVendaEntity> item_venda = itemVendaRepository.findByProduto(product);
        if (item_venda.isPresent())
            throw new EntityExistsException("Há itens de venda com o id requisitado");

    }

    @Transactional
    public void delete(Long id_produto){
        ProductEntity product = findById(id_produto);
        if (product == null) {
            existsById(id_produto);
        }

        existsItemVenda(product);

        productRepository.delete(product);

    }

    @Transactional
    public Long updateDoPut(Long id_produto,
                            ProductDTO productDTO) {
        ProductEntity product = validationsPut(id_produto, productDTO);
        product.setNome(productDTO.getNome());
        product.setPreco_sugerido(productDTO.getPreco_sugerido());

        return id_produto;
    }

    @Transactional
    public Long updateDoPatch(Long id_produto,
                              ProductDTO productDTO) {
        ProductEntity product = validationsPatch(id_produto, productDTO);

        if (productDTO.getNome() != null)
            product.setNome(productDTO.getNome());
        if(productDTO.getPreco_sugerido() != null)
            product.setPreco_sugerido(productDTO.getPreco_sugerido());
        return id_produto;
    }

    public ProductEntity validationsPut(Long id_produto,
                                        ProductDTO productDTO){
        ProductEntity product = findById(id_produto);
        if (product == null) {
            existsById(id_produto);
        }
        if (productDTO.getNome().isBlank()){
            existsNome(productDTO);
        }
        if (productDTO.getPreco_sugerido() == null){
            existsPreco(productDTO);
        }
        if(!productDTO.getNome().isBlank()){
            isUniqueNomeProduct(productDTO);
        }
        if (productDTO.getPreco_sugerido().compareTo(BigDecimal.ZERO) <= 0){
            precoValido(productDTO);
        }

        return  product;
    }

    public ProductEntity validationsPatch(Long id_produto,
                                        ProductDTO productDTO){
        ProductEntity product = findById(id_produto);
        if (product == null) {
            existsById(id_produto);
        }
        if(productDTO.getNome() != null) {
            if (!productDTO.getNome().isBlank()) {
                isUniqueNomeProduct(productDTO);
            }
        }
        if (productDTO.getPreco_sugerido() != null){
            if (productDTO.getPreco_sugerido().compareTo(BigDecimal.ZERO) <= 0) {
                precoValido(productDTO);
            }
        }

        return product;
    }


}
