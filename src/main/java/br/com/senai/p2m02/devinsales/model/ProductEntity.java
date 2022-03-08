package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name="produto")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;



    private String nome;
    private BigDecimal preco_sugerido;

    public ProductEntity(long id, String nome, BigDecimal preco_sugerido) {
        this.id = id;
        this.nome = nome;
        this.preco_sugerido = preco_sugerido;
    }

    public ProductEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco_sugerido() {
        return preco_sugerido;
    }

    public void setPreco_sugerido(BigDecimal preco_sugerido) {
        this.preco_sugerido = preco_sugerido;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco_sugerido=" + preco_sugerido +
                '}';
    }


}
