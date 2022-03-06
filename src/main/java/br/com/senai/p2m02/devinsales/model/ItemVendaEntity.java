package br.com.senai.p2m02.devinsales.model;


import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name="item_venda")
public class ItemVendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemvendager")
    @SequenceGenerator(name = "itemvendager", sequenceName = "item_venda_id_ger", allocationSize = 1)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId("idVenda")
    @JoinColumn(name = "id_venda", referencedColumnName = "id")
    private VendaEntity venda;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId("idProduto")
    @JoinColumn(name = "id_produto", referencedColumnName = "id")
    private ProductEntity produto;

    @NotNull
    private BigDecimal precoUnitario;

    @NotNull
    private Integer quantidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VendaEntity getVenda() {
        return venda;
    }

    public void setVenda(VendaEntity venda) {
        this.venda = venda;
    }

    public ProductEntity getProduto() {
        return produto;
    }

    public void setProduto(ProductEntity produto) {
        this.produto = produto;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}
