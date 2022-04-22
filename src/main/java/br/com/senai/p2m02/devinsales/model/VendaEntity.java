package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity(name = "venda")
public class VendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendager")
    @SequenceGenerator(name = "vendager", sequenceName = "venda_id_seq", allocationSize = 1)

    private Long id;

    @Column (name = "dt_venda")
    private LocalDateTime dataVenda;

    @ManyToOne
    @JoinColumn(name = "id_comprador", referencedColumnName = "id")
    private UserEntity comprador;

    @ManyToOne
    @JoinColumn(name = "id_vendedor", referencedColumnName = "id")
    private UserEntity vendedor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venda", fetch = FetchType.EAGER)
    private Set<ItemVendaEntity> itens;

    public Set<ItemVendaEntity> getItens() {
        return itens;
    }

    public void setItens(Set<ItemVendaEntity> itens) {
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public UserEntity getComprador() {
        return comprador;
    }

    public void setComprador(UserEntity comprador) {
        this.comprador = comprador;
    }

    public UserEntity getVendedor() {
        return vendedor;
    }

    public void setVendedor(UserEntity vendedor) {
        this.vendedor = vendedor;
    }
}
