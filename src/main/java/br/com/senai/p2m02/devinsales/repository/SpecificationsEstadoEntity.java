package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationsEstadoEntity {
    //WHERE nome LIKE '%:nome%'
    public static Specification<EstadoEntity> nome(String nome){
        return (root, query, criteriaBuilder) -> {
            if(nome == null) {
                return criteriaBuilder.like(root.get("nome"), "%%");
            } else return criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
        };
    }
}
