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

    @NotNull
    private Long id_endereço;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_venda", referencedColumnName = "id")
    private VendaEntity venda;

    @NotNull
    private LocalDate previsao_entrega;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_endereço() {
        return id_endereço;
    }

    public void setId_endereço(Long id_endereço) {
        this.id_endereço = id_endereço;
    }

    public LocalDate getPrevisao_entrega() {
        return previsao_entrega;
    }

    public void setPrevisao_entrega(LocalDate previsao_entrega) {
        this.previsao_entrega = previsao_entrega;
    }

    public VendaEntity getVenda() {
        return venda;
    }
    public Void setVenda(VendaEntity venda){
        this.venda = venda;
    }

    public void setVenda(VendaEntity venda) {
        this.venda = venda;
    }
}
