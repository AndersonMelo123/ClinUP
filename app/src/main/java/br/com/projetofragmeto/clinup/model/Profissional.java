package br.com.projetofragmeto.clinup.model;

public class Profissional {

    private String endereco;
    private String especialidade;
    private String formacao;
    private String nome;
    private String num_registro;
    private String id;
    private Dias dias;
    private String idAgendamentos;


    public Profissional(){}
    public Profissional(String nome, String endereco, String especialidade, String formacao, String num_registro) {
        this.endereco = endereco;
        this.especialidade = especialidade;
        this.formacao = formacao;
        this.nome = nome;
        this.num_registro = num_registro;
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

    public String getIdAgendamentos() {
        return idAgendamentos;
    }

    public void setIdAgendamentos(String idAgendamentos) {
        this.idAgendamentos = idAgendamentos;
    }

}
