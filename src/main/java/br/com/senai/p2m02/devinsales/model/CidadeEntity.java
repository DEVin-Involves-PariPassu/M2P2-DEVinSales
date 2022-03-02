package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.*;

@Entity(name = "cidade")
public class CidadeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cidadeger")
    @SequenceGenerator(name = "cidadeger", sequenceName = "cidade_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_estado", referencedColumnName = "id")
    private EstadoEntity estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }
}
