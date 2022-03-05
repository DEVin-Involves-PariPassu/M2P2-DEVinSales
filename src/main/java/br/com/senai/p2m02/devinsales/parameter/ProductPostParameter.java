package br.com.senai.p2m02.devinsales.parameter;

import java.math.BigDecimal;

public class ProductPostParameter {

    private String nome;
    private BigDecimal preco_sugerido;

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
        return "ProductPostParameter{" +
                "nome='" + nome + '\'' +
                ", preco_sugerido=" + preco_sugerido +
                '}';
    }
}
