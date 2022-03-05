package br.com.senai.p2m02.devinsales.repository;


import br.com.senai.p2m02.devinsales.model.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;



public interface DeliveryRepository extends CrudRepository<DeliveryEntity, Long> ,
        JpaSpecificationExecutor<DeliveryEntity>{

};
