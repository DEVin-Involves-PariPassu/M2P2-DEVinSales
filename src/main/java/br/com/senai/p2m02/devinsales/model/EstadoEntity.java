package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.*;

@Entity(name = "estado")
public class EstadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estadoger")
    @SequenceGenerator(name = "estadoger", sequenceName = "estado_id_seq", allocationSize = 1)
    private Long id;

    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
