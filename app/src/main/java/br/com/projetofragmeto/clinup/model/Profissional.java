package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Profissional implements Serializable {

    private String endereco;
    private String especialidade;
    private String formacao;
    private String nome;
    private String num_registro;
    private String id;
    private String telefone;
    private Dias dias;
    private String horaAbrir,horaFechar;


    public Profissional() {
    }

    public Profissional(String nome, String endereco, String especialidade, String formacao, String num_registro) {
        this.endereco = endereco;
        this.especialidade = especialidade;
        this.formacao = formacao;
        this.nome = nome;
        this.num_registro = num_registro;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + '\n' +
                "Especialidade: " + especialidade;
    }

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Dias getDias() {
        return dias;
    }

    public void setDias(Dias dias) {
        this.dias = dias;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNum_registro() {
        return num_registro;
    }

    public void setNum_registro(String num_registro) {
        this.num_registro = num_registro;
    }

}
