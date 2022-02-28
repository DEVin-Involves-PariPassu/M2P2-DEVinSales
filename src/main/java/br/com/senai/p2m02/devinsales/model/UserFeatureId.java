package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserFeatureId implements Serializable {
    private Long idUsuario;
    private Long idFeature;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdFeature() {
        return idFeature;
    }

    public void setIdFeature(Long idFeature) {
        this.idFeature = idFeature;
    }

    @Override
    public String toString() {
        return "UserFeatureId{" +
                "idUsuario=" + idUsuario +
                ", idFeature=" + idFeature +
                '}';
    }
}
