package br.com.senai.p2m02.devinsales.repository;


import br.com.senai.p2m02.devinsales.model.VendaEntity;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationsVendaEntity {
    //WHERE vendedor = :idVendedor
    public static Specification<VendaEntity> idVendedor(Long idVendedor){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vendedor"), idVendedor);
    }
}
