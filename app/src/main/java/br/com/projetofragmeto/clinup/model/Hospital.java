package br.com.projetofragmeto.clinup.model;

public class Hospital {

    private String cnpj;
    private String email;
    private String endereco;
    private String nome;
    private String telefone;
    private String idAgendamentos;



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

    public String getIdAgendamentos() {
        return idAgendamentos;
    }

    public void setIdAgendamentos(String idAgendamentos) {
        this.idAgendamentos = idAgendamentos;
    }
}
