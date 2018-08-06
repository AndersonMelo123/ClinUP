package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

/*Adicionando a interface serializable será possível transformar o objeto num formato que poderá ser salvo num arquivo.
 Por exemplo, para utilizar um ObjectOutputStream e salvar um objeto num arquivo do disco será necessário implementar essa interface.*/

public class Endereco implements Serializable {

    //Atributos

    private String idUsuario;
    private String id;

    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String cep;

    private double Latitude;
    private double Longitude;

    public Endereco(String rua, String bairro, String localidade, String uf) {
        this.rua = rua;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
    }

    public Endereco() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}

