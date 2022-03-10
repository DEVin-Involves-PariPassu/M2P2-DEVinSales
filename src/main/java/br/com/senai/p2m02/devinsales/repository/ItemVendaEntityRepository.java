package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ItemVendaEntityRepository extends CrudRepository<ItemVendaEntity, Long> {

    Optional<ItemVendaEntity> findByProduto(ProductEntity produto);

}
