package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

public class VendaDTO {
    private Long id;

    @NotNull(message = "Id da venda é requerido")
    private Long vendaId;

    @NotNull(message = "Nome do vendedor é requerido")
    private Long vendedor;

    @NotNull(message = "Nome do comprador é requerido")
    private Long comprador;

    @NotNull(message = "Data da venda é requerida")
    private LocalDate dataVenda;
}
