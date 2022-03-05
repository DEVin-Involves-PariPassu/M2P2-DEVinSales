package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationsCidadeEntity {
    public static Specification<CidadeEntity> nome(String nome){
        return (root, query, criteriaBuilder) -> {
            if(nome == null) {
                return criteriaBuilder.like(root.get("nome"), "%%");
            } else return criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
        };
    }

    public static Specification<CidadeEntity> idEstado(Long idEstado){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id_estado"), idEstado);
    }
}
