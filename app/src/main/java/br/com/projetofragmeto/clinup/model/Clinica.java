package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Clinica implements Serializable {

    private String cnpj, email, endereco, nome, telefone, horaAbrir, horaFechar, id;

    public String getHoraAbrir() {
        return horaAbrir;
    }

    public void setHoraAbrir(String horaAbrir) {
        this.horaAbrir = horaAbrir;
    }

    public String getHoraFechar() {
        return horaFechar;
    }

    public void setHoraFechar(String horaFechar) {
        this.horaFechar = horaFechar;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
}
