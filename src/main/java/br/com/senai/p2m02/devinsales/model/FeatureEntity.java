package br.com.senai.p2m02.devinsales.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity(name = "feature")
public class FeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feature_ger")
    @SequenceGenerator(name = "feature_ger", sequenceName = "feature_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nome_feature")
    private String nomeFeature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFeature() {
        return nomeFeature;
    }

    public void setNomeFeature(String nomeFeature) {
        this.nomeFeature = nomeFeature;
    }
}
