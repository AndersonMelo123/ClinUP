package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

/*Adicionando a interface serializable será possível transformar o objeto num formato que poderá ser salvo num arquivo.
 Por exemplo, para utilizar um ObjectOutputStream e salvar um objeto num arquivo do disco será necessário implementar essa interface.*/

public class PlanoDeSaude implements Serializable {

    private String idUsuario;
    private String id;

    private String nomePlano;
    private String numPlano;

    public PlanoDeSaude() {
    }

    public PlanoDeSaude(String idUsuario, String id, String nomePlano, String numPlano) {
        this.idUsuario = idUsuario;
        this.id = id;
        this.nomePlano = nomePlano;
        this.numPlano = numPlano;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public String getNumPlano() {
        return numPlano;
    }

    public void setNumPlano(String numPlano) {
        this.numPlano = numPlano;
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
}
