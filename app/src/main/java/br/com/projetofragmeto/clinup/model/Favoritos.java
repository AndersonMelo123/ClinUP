package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Favoritos implements Serializable {

    String id_Usuario, id_Cliente, nomeUsuario, nomeCliente;

    public Favoritos(String id_Usuario, String id_Cliente, String nomeUsuario, String nomeCliente) {
        this.id_Usuario = id_Usuario;
        this.id_Cliente = id_Cliente;
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
