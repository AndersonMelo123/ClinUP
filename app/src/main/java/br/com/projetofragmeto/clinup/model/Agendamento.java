package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Agendamento implements Serializable {

    private String id_Usuario, id_Cliente, dataConsulta, dataAtual, nomeUsuario, id_Plano, id, nome_Cliente;

    public Agendamento() {
    }


    public Agendamento(String id_Usuario, String id_Cliente, String dataConsulta, String dataAtual, String nomeUsuario, String id_Plano) {
        this.id_Usuario = id_Usuario;
        this.id_Cliente = id_Cliente;
        this.dataConsulta = dataConsulta;
        this.dataAtual = dataAtual;
        this.nomeUsuario = nomeUsuario;
        this.id_Plano = id_Plano;
    }

    @Override
    public String toString() {
        return "Nome: " + nomeUsuario + '\n' +
                "Data da Consulta: " + dataConsulta;
    }

    public String getNomeCliente() {
        return nome_Cliente;
    }

    public void setNomeCliente(String nome_Cliente) {
        this.nome_Cliente = nome_Cliente;
    }

    public String getIdUsuario() {
        return id_Usuario;
    }

    public void setIdUsuario(String id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public String getIdCliente() {
        return id_Cliente;
    }

    public void setIdCliente(String id_Cliente) {
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

    public String getIdPlano() {
        return id_Plano;
    }

    public void setIdPlano(String id_Plano) {
        this.id_Plano = id_Plano;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
