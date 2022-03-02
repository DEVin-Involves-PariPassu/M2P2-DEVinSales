package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationsEstadoEntity {
    public static Specification<EstadoEntity> nome(String nome){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
    }
}
