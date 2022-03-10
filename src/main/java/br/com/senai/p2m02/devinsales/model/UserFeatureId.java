package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserFeatureId implements Serializable {
    private Long idUsuario;
    private Long idFeature;

    public UserFeatureId () {}

    public UserFeatureId(Long idUsuario, Long idFeature) {
        this.idUsuario = idUsuario;
        this.idFeature = idFeature;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFeatureId that = (UserFeatureId) o;
        return idUsuario.equals(that.idUsuario) && idFeature.equals(that.idFeature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idFeature);
    }
}
