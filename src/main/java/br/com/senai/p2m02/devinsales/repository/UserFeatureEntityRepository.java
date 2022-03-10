package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.UserFeatureEntity;
import br.com.senai.p2m02.devinsales.model.UserFeatureId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeatureEntityRepository extends CrudRepository<UserFeatureEntity, UserFeatureId>,
        JpaSpecificationExecutor<UserFeatureEntity> {
}
