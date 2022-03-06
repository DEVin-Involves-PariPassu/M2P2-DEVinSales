package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.CidadeEntity;

import br.com.senai.p2m02.devinsales.model.EstadoEntity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeEntityRepository extends CrudRepository <CidadeEntity, Long>,
        JpaSpecificationExecutor<CidadeEntity> {
}
