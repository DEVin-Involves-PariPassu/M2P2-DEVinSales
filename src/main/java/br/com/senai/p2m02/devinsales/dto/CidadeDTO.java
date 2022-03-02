package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;

public class CidadeDTO {
    private Long id;

    @NotNull(message = "Id do estado é requerido")
    private Long estadoId;

    @NotNull(message = "Nome da cidade é requerido")
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
