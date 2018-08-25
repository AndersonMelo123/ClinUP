package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Favoritos implements Serializable {

    String idUsuario, idCliente, nomeUsuario, nomeCliente, id, tipo;

    public Favoritos(String idUsuario, String idCliente, String nomeUsuario, String nomeCliente) {
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.nomeUsuario = nomeUsuario;
        this.nomeCliente = nomeCliente;
    }

    public  Favoritos(){

    }


    @Override
    public String toString() {
        return "nomeUsuario: " + nomeUsuario + '\n' +
                "nomeCliente: " + nomeCliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String id_Usuario) {
        this.idUsuario = id_Usuario;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String id_Cliente) {
        this.idCliente = id_Cliente;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}
