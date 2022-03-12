package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SpecificationsUserEntity {

    public static Specification<UserEntity> nome(String nome){
        return (root, query, criteriaBuilder) -> {
            if(nome == null) {
                return criteriaBuilder.like(root.get("nome"), "%%");
            } else return criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
        };
    }

    public static Specification<UserEntity> dtNascimentoMin(LocalDate dtNascimentoMin){
        return (root, query, criteriaBuilder) -> {
            if(dtNascimentoMin == null) {
                return criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
            } else return criteriaBuilder.greaterThan(root.get("dtNascimento"),dtNascimentoMin);
        };
    }

    public static Specification<UserEntity> dtNascimentoMax(LocalDate dtNascimentoMax){
        return (root, query, criteriaBuilder) -> {
            if(dtNascimentoMax == null) {
                return criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
            } else return criteriaBuilder.lessThan(root.get("dtNascimento"),dtNascimentoMax);
        };
    }
}
