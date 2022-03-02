package br.com.senai.p2m02.devinsales.model;

import jakarta.persistence.*;

@Entity(name = "endereco")
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enderecoger")
    @SequenceGenerator(name = "enderecoger", sequenceName = "endereco_id_seq", allocationSize = 1)
    private Long id;

    private String rua;
    private Integer numero;
    private String complemento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cidade", referencedColumnName = "id")
    private CidadeEntity cidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public CidadeEntity getCidade() {
        return cidade;
    }

    public void setCidade(CidadeEntity cidade) {
        this.cidade = cidade;
    }
}


