package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Agendamento implements Serializable {
    private String id_Usuario;
    private String id_Cliente;
    private String dataConsulta;
    private String dataAtual;
    private String nomeUsuario;
    private String id_Plano;

    public Agendamento() {
    }

    public Agendamento(String id_Usuario, String id_Cliente, String dataConsulta, String dataAtual, String nomeUsuario) {
        this.id_Usuario = id_Usuario;
        this.id_Cliente = id_Cliente;
        this.dataConsulta = dataConsulta;
        this.dataAtual = dataAtual;
        this.nomeUsuario = nomeUsuario;
    }

    public Agendamento(String id_Usuario, String id_Cliente, String dataConsulta, String dataAtual, String nomeUsuario, String id_Plano) {
        this.id_Usuario = id_Usuario;
        this.id_Cliente = id_Cliente;
        this.dataConsulta = dataConsulta;
        this.dataAtual = dataAtual;
        this.nomeUsuario = nomeUsuario;
        this.id_Plano = id_Plano;
    }

    public String getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(String id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public String getId_Cliente() {
        return id_Cliente;
    }

    public void setId_Cliente(String id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    public String getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(String dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(String dataAtual) {
        this.dataAtual = dataAtual;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getId_Plano() {
        return id_Plano;
    }

    public void setId_Plano(String id_Plano) {
        this.id_Plano = id_Plano;
    }
}
