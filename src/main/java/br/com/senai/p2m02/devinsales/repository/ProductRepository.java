package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByNome(String nome);
}
