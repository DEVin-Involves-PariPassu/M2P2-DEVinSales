package br.com.senai.p2m02.devinsales.model;


import jakarta.persistence.*;

import javax.validation.constraints.NotNull;

@Entity
@Table(name="item_venda")
public class ItemVendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemvendager")
    @SequenceGenerator(name = "itemvendager", sequenceName = "item_venda_id_ger", allocationSize = 1)
    private Long id;

    @NotNull
    //mapear FK para Entidade Venda
    private Integer idVenda;

    @NotNull
    //mapear FK para Entidade Produto
    private Integer idProduto;

    @NotNull
    private Integer precoUnitario;

    @NotNull
    private Integer quantidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Integer idVenda) {
        this.idVenda = idVenda;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
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
