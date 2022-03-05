package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    List<ProductEntity> produtos = new ArrayList<>();

    Optional<ProductEntity> findByNome(String nome);
}
