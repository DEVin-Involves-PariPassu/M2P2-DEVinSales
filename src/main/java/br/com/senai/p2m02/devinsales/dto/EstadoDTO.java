package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;

public class EstadoDTO {
    private Long id;

    @NotNull(message = "Nome do estado é requerido")
    private String nome;

    @NotNull(message = "Sigla é requerido")
    private String sigla;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
