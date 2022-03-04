package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationsEnderecoEntity {
    //WHERE nome LIKE '%:nome%'
    public static Specification<EnderecoEntity> rua(String rua){
        return (root, query, criteriaBuilder) -> {
            if(rua == null) {
                return criteriaBuilder.like(root.get("rua"), "%%");
            } else return criteriaBuilder.like(root.get("rua"), "%" + rua + "%");
        };
    }

    public static Specification<EnderecoEntity> numero(Integer numero){
        return (root, query, criteriaBuilder) -> {
            if (numero==null) {
                return criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
            }else
                return criteriaBuilder.equal(root.get("numero"), numero);
        };
    }

    public static Specification<EnderecoEntity> idCidade(Long idCidade){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cidade"),idCidade);
    }

    public static Specification<EnderecoEntity> idEstado(Long idEstado){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("cidade").get("estado"),idEstado);
    }

    public static Specification<EnderecoEntity> complemento(String complemento) {
        return (root, query, criteriaBuilder) -> {
            if(complemento == null) {
                return criteriaBuilder.like(root.get("complemento"), "%%");
            } else return criteriaBuilder.like(root.get("complemento"), "%" + complemento + "%");
        };
    }
}
