package br.com.projetofragmeto.clinup.model;

import java.io.Serializable;

public class Dias implements Serializable {
    private int segunda, terça, quarta, quinta, sexta, sabado, domingo;

    public int getSegunda() {
        return segunda;
    }

    public void setSegunda(int segunda) {
        this.segunda = segunda;
    }

    public int getTerça() {
        return terça;
    }

    public void setTerça(int terça) {
        this.terça = terça;
    }

    public int getQuarta() {
        return quarta;
    }

    public void setQuarta(int quarta) {
        this.quarta = quarta;
    }

    public int getQuinta() {
        return quinta;
    }

    public void setQuinta(int quinta) {
        this.quinta = quinta;
    }

    public int getSexta() {
        return sexta;
    }

    public void setSexta(int sexta) {
        this.sexta = sexta;
    }

    public int getSabado() {
        return sabado;
    }

    public void setSabado(int sabado) {
        this.sabado = sabado;
    }

    public int getDomingo() {
        return domingo;
    }

    public void setDomingo(int domingo) {
        this.domingo = domingo;
    }
}
