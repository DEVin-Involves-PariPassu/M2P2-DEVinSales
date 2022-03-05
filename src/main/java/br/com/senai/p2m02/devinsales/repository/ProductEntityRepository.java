package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEntityRepository extends CrudRepository<ProductEntity, Long> {

}
