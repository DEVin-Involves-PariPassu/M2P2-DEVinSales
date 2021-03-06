package br.com.senai.p2m02.devinsales.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductDTO {

    @NotBlank (message = "Nome do produto é requerido")
    private String nome;

    @NotNull (message = "O preço sugerido para o produto é requerido")
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
        return "ProductDTO{" +
                "nome='" + nome + '\'' +
                ", preco_sugerido=" + preco_sugerido +
                '}';
    }
}
