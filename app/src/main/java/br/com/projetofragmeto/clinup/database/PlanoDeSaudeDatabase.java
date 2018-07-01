package br.com.projetofragmeto.clinup.database;

import br.com.projetofragmeto.clinup.model.PlanoDeSaude;

public interface PlanoDeSaudeDatabase {
     void inserirPlanodeSaude(PlanoDeSaude planoDeSaude, String idUsuarioLogado, String nomePlano, String numPlano);
}

