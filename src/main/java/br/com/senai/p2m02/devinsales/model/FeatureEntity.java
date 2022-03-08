package br.com.senai.p2m02.devinsales.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity(name = "feature")
public class FeatureEntity {

    @Id
    private Long id;

    @OneToMany(mappedBy = "feature")
    private Set<UserFeatureEntity> userFeatureEntities;

    private String nomeFeature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @JsonIgnore
    public Set<UserFeatureEntity> getUserFeatureEntities() {
        return userFeatureEntities;
    }

    public void setUserFeatureEntities(Set<UserFeatureEntity> userFeatureEntities) {
        this.userFeatureEntities = userFeatureEntities;
    }

    public String getNomeFeature() {
        return nomeFeature;
    }

    public void setNomeFeature(String nomeFeature) {
        this.nomeFeature = nomeFeature;
    }
}
