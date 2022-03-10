package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.FeatureEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FeatureEntityRepository extends CrudRepository<FeatureEntity, Long>,
        JpaSpecificationExecutor<FeatureEntity> {
    Optional<FeatureEntity> findFirstByNomeFeature(String nomeFeature);
}
