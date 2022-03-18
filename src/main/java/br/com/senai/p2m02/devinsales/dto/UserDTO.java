package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class UserDTO {

    @NotBlank(message = "O login é obrigatório")
    private String login;

    @NotBlank(message = "Informe uma senha")
    private String senha;

    @NotBlank(message = "Informe o nome")
    private String nome;

    @NotBlank(message = "Informe a data de nascimento")
    private String dtNascimento;

    @NotBlank
    private List<FeatureDTO> features;

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

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public List<FeatureDTO> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureDTO> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", nome='" + nome + '\'' +
                ", dtNascimento='" + dtNascimento + '\'' +
                ", features=" + features +
                '}';
    }
}