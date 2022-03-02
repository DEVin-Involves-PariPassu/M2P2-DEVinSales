package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;

public class CidadeDTO {
    private Long id;

    @NotNull(message = "Id do estado é requerido")
    private Long estadoId;

    @NotNull(message = "Nome da cidade é requerido")
    private String nome;
}
