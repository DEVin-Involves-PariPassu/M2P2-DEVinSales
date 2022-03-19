package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemVendaDTO {
    private Long id;

    @NotNull(message = "Nome do produto é requerido")
    private String nomeProduto;

    @NotNull(message = "Preço unitário é requerido")
    private Integer precoUnitario;

    @NotNull(message = "Quantidade é requerida")
    private Integer quantidade;

    @NotNull(message = "Total dos itens de venda é requerida")
    private BigDecimal totalItensVenda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Integer getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Integer precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getTotalItensVenda() {
        return totalItensVenda;
    }

    public void setTotalItensVenda(BigDecimal totalItensVenda) {
        this.totalItensVenda = totalItensVenda;
    }
}
