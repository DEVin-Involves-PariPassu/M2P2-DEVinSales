package br.com.senai.p2m02.devinsales.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Entity(name = "usuario_feature")
public class UserFeatureEntity implements Serializable, GrantedAuthority {

    @EmbeddedId
    private UserFeatureId id;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private UserEntity user;

    @ManyToOne
    @MapsId("idFeature")
    @JoinColumn(name = "id_feature")
    private FeatureEntity feature;

    private Boolean read;
    private Boolean write;

    public UserFeatureId getId() {
        return id;
    }

    public void setId(UserFeatureId id) {
        this.id = id;
    }

    @JsonIgnore
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public FeatureEntity getFeature() {
        return feature;
    }

    public void setFeature(FeatureEntity feature) {
        this.feature = feature;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getWrite() {
        return write;
    }

    public void setWrite(Boolean write) {
        this.write = write;
    }

    @Override
    public String toString() {
        return "UserFeatureEntity{" +
                "id=" + id +
                ", feature=" + feature.getNomeFeature() +
                ", read=" + read +
                ", write=" + write +
                '}';
    }

    @Override
    public String getAuthority() {
        return this.getFeature().getNomeFeature();
    }
}