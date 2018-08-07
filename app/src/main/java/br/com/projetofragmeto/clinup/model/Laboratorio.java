package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Laboratorio implements Serializable {
    private int id;
    private String cnpj;
    private String email;
    private String endereco;
    private String nome;
    private String telefone;
    private String idAgendamentos;



    public Laboratorio(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getIdAgendamentos() {
        return idAgendamentos;
    }

    public void setIdAgendamentos(String idAgendamentos) {
        this.idAgendamentos = idAgendamentos;
    }
}
