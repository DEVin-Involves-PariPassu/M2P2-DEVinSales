package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;

public class EstadoDTO {
    private Long id;

    @NotNull(message = "Nome do estado é requerido")
    private String nome;

    @NotNull(message = "Sigla é requerido")
    private String sigla;
}
