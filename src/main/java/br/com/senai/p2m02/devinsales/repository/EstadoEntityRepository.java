package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoEntityRepository extends CrudRepository<EstadoEntity, Long>,
        JpaSpecificationExecutor<EstadoEntity> {
    //SELECT * FROM estado WHERE nome = :nome LIMIT 1
    Optional<EstadoEntity> findFirstByNome(String nome);
    //SELECT * FROM estado WHERE sigla = :sigla LIMIT 1
    Optional<EstadoEntity> findFirstBySigla(SiglaEstado sigla);
}
