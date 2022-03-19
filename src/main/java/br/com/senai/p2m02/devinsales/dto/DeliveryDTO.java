package br.com.senai.p2m02.devinsales.dto;

import br.com.senai.p2m02.devinsales.model.EnderecoEntity;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class DeliveryDTO {
    private Long id;

    @NotNull(message = "Endereço é requerido")
    private EnderecoEntity endereco;

    @NotNull (message = "ID da Venda é requerido")
    private Long venda;

    @NotNull (message = "Previsao de entrega é requerido")
    private LocalDate previsaoEntrega;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnderecoEntity getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoEntity endereco) {
        this.endereco = endereco;
    }

    public Long getVenda() {
        return venda;
    }

    public void setVenda(Long venda) {
        this.venda = venda;
    }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }
}


