package br.com.senai.p2m02.devinsales.dto;

import javax.validation.constraints.NotNull;

public class ItemVendaDTO {
    private Long id;

    @NotNull(message = "Id da venda é requerido")
    private Long vendaId;

    @NotNull(message = "Id do item é requerido")
    private Long itemId;

    @NotNull(message = "Preço unitário é requerido")
    private Integer precoUnitario;

    @NotNull(message = "Quantidade é requerida")
    private Integer quantidade;

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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
}
