package br.com.senai.p2m02.devinsales.dto;

import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;


public class VendaDTO {

    @NotNull(message = "Id da venda é requerido")
    private Long id;

    @NotNull(message = "Nome do vendedor é requerido")
    private String nomeVendedor;

    @NotNull(message = "Nome do comprador é requerido")
    private String nomeComprador;

    @NotNull(message = "Data da venda é requerida")
    private LocalDateTime dataVenda;

    @NotNull(message = "Total da venda é requerida")
    private BigDecimal totalVenda;

    @NotNull(message = "Item é requerido")
    private List<ItemVendaEntity> listaItens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }

    public BigDecimal getTotalVenda() {
        return totalVenda;
    }

    public void setTotalVenda(BigDecimal totalVenda) {
        this.totalVenda = totalVenda;
    }

    public List<ItemVendaEntity> getListaItens() {
        return listaItens;
    }

    public void setListaItens(List<ItemVendaEntity> listaItens) {
        this.listaItens = listaItens;
    }
}
