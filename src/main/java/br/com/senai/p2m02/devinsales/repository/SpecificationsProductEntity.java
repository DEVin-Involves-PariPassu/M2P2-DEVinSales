package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

public class SpecificationsProductEntity {
    public static Specification<ProductEntity> nome(String nome){
        return (root, query, criteriaBuilder) -> {
            if(nome == null) {
                return criteriaBuilder.like(root.get("nome"), "%%");
            } else return criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
        };
    }

    public static Specification<ProductEntity> productValueMin(BigDecimal productValueMin){
        return (root, query, criteriaBuilder) -> {
            if(productValueMin == null) {
                return criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
            } else return criteriaBuilder.greaterThan(root.get("preco_sugerido"),productValueMin);
        };
    }

    public static Specification<ProductEntity> productValueMax(BigDecimal productValueMax){
        return (root, query, criteriaBuilder) -> {
            if(productValueMax == null) {
                return criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
            } else return criteriaBuilder.lessThan(root.get("preco_sugerido"),productValueMax);
        };
    }
}
