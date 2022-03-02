package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoEntityRepository extends CrudRepository <EnderecoEntity, Long> {
}
