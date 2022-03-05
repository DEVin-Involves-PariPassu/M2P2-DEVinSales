package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductEntityRepository extends CrudRepository<ProductEntity, Long> {

}
