package br.com.senai.p2m02.devinsales.model;


import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity(name = "entrega")
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entregager")
    @SequenceGenerator(name = "entregager", sequenceName = "entrega_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_endereco", referencedColumnName = "id")
    private EnderecoEntity endereco;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_venda", referencedColumnName = "id")
    private VendaEntity venda;

    @NotNull
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

    public void setEndereço(EnderecoEntity endereco) {
        this.endereco = endereco;
    }

    public VendaEntity getVenda() { return venda; }

    public void setVenda(VendaEntity venda) { this.venda = venda; }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

}
