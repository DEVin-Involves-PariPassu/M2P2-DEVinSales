package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Entity(name = "usuario")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userger")
    @SequenceGenerator(name = "userger", sequenceName = "usuario_id_seq", allocationSize = 1)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserFeatureEntity> userFeatureEntities;

    private String login;
    private String senha;
    private String nome;
    private LocalDate dtNascimento;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<UserFeatureEntity> getUserFeatureEntities() {
        return userFeatureEntities;
    }

    public void setUserFeatureEntities(Set<UserFeatureEntity> userFeatureEntities) {
        this.userFeatureEntities = userFeatureEntities;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", nome='" + nome + '\'' +
                ", dtNascimento=" + dtNascimento +
                ", features=" + userFeatureEntities +
                '}';
    }

    public boolean canRead(String feature) {
        Optional<UserFeatureEntity> featureInfo = getFeatureInfo(feature);
        if (featureInfo.isEmpty())
            return false;
        return featureInfo.get().getRead();
    }

    public boolean canWrite(String feature) {
        Optional<UserFeatureEntity> featureInfo = getFeatureInfo(feature);
        if (featureInfo.isEmpty())
            return false;
        return featureInfo.get().getWrite();
    }

    private Optional<UserFeatureEntity> getFeatureInfo(String feature) {
        Optional<UserFeatureEntity> featureInfo = getUserFeatureEntities().stream()
                .filter(userFeatureEntity -> feature.equalsIgnoreCase(userFeatureEntity.getFeature().getNomeFeature()))
                .findFirst();
        return featureInfo;
    }
}
