package br.com.senai.p2m02.devinsales.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity(name = "usuario")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userger")
    @SequenceGenerator(name = "userger", sequenceName = "usuario_id_seq", allocationSize = 1)
    private Long id;

    private String login;
    private String senha;
    private String nome;
    private LocalDate dtNascimento;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserFeatureEntity> userFeatureEntities;

//    @ManyToMany(fetch = FetchType.EAGER)
//    public Set<FeatureEntity> features = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @JsonIgnore
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

    @JsonIgnore
    public Set<UserFeatureEntity> getUserFeatureEntities() {
        return userFeatureEntities;
    }

    public void setUserFeatureEntities(Set<UserFeatureEntity> userFeatureEntities) {
        this.userFeatureEntities = userFeatureEntities;
    }

//    public Set<FeatureEntity> getFeatures() {
//        return features;
//    }
//
//    public void setFeatures(Set<FeatureEntity> features) {
//        this.features = features;
//    }

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userFeatureEntities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}