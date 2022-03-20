package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.ProductEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemVendaEntityRepository extends CrudRepository<ItemVendaEntity, Long> {

    List<ItemVendaEntity> findByProduto(ProductEntity produto);
    List<ItemVendaEntity> findByVenda (VendaEntity venda);

}
