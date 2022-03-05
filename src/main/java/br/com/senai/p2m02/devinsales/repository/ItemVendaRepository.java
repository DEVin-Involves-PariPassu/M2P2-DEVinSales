package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ItemVendaRepository extends CrudRepository<ItemVendaEntity, Long> {

    Optional<ItemVendaEntity> findItemVendaEntityByIdVenda(Integer id);
}