package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado");
public class EstadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estadoger")
    @SequenceGenerator(name = "estadoger", sequenceName = "estado_id_seq", allocationSize = 1)
    private Long id;

    private String nome;

    @Enumerated(value = EnumType.STRING)
    private SiglaEstado sigla;

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

    public SiglaEstado getSigla() {
        return sigla;
    }

    public void setSigla(SiglaEstado sigla) {
        this.sigla = sigla;
    }
}
