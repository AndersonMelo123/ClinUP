package br.com.projetofragmeto.clinup.model;

public class PlanoDeSaude {
    private String nomePlano;
    private String numPlano;

    public PlanoDeSaude(String nomePlano, String numPlano) {
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
}
