package br.com.senai.p2m02.devinsales.repository;


import br.com.senai.p2m02.devinsales.model.DeliveryEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryRepository extends CrudRepository<DeliveryEntity, Long> ,
        JpaSpecificationExecutor<DeliveryEntity>{

    List<DeliveryEntity> findDeliveryEntityByEndereco(EnderecoEntity endereco);
    List<DeliveryEntity> findDeliveryEntityByVenda(VendaEntity venda);
    List<DeliveryEntity> findDeliveryEntityByEnderecoAndVenda(EnderecoEntity endereco, VendaEntity venda);

}

