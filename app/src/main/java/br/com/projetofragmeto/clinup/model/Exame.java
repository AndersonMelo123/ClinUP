package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Exame implements Serializable {
    private String nome;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
