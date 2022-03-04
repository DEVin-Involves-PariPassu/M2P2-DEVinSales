package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;

public class EnderecoDTO {

    private Long id;

    @NotNull(message = "Id da cidade é requerido")
    private Long cidadeId;

    @NotNull(message = "Id do estado é requerido")
    private Long estadoId;

    @NotNull(message = "Nome da rua é requerido")
    private String rua;

    @NotNull(message = "Numero da rua é requerido")
    private Integer numero;

    private String complemento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCidadeId() {
        return cidadeId;
    }

    public void setCidadeId(Long cidadeId) {
        this.cidadeId = cidadeId;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }
}
