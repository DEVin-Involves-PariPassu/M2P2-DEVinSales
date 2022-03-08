package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public class VendaDTO {
    private Long id;

    @NotNull(message = "Id da venda é requerido")
    private Long vendaId;

    @NotNull(message = "Nome do vendedor é requerido")
    private Long vendedor;

    @NotNull(message = "Nome do comprador é requerido")
    private Long comprador;

    @NotNull(message = "Data da venda é requerida")
    private String dataVenda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVendaId() {
        return vendaId;
    }

    public void setVendaId(Long vendaId) {
        this.vendaId = vendaId;
    }

    public Long getVendedor() {
        return vendedor;
    }

    public void setVendedor(Long vendedor) {
        this.vendedor = vendedor;
    }

    public Long getComprador() {
        return comprador;
    }

    public void setComprador(Long comprador) {
        this.comprador = comprador;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }
}
