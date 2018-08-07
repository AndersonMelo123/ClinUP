package br.com.projetofragmeto.clinup.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;

/* Classe que implementa a interface para o acesso de dados no banco de dados*/

public class PlanoDeSaudeImplements implements PlanoDeSaudeDatabase {

    private DatabaseReference ReferenciaFirebase; //Referenciando o banco de dados do firebase
    private final Context Contexto; // Váriavel para coletar qual a classe que ele está atualmente

    public PlanoDeSaudeImplements(Context mContexto) { // Construtor
        this.ReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
        this.Contexto = mContexto;
    }

    // Método para inserir o plano de saúde no banco de dados
    @Override
    public void inserirPlanodeSaude(PlanoDeSaude planoDeSaude, String idUsuarioLogado, String nomePlano, String numPlano) {

        //Método para salvar endereço no banco de dados do Firebase

        planoDeSaude.setIdUsuario(idUsuarioLogado); //Setando o identificador do usuario logado como identificador no plano de saúde
        planoDeSaude.setNumPlano(numPlano);
        planoDeSaude.setNomePlano(nomePlano);
        planoDeSaude.setId(idUsuarioLogado);

        ReferenciaFirebase = ReferenciaFirebase.child("planodesaude").child(idUsuarioLogado); // Consultando o banco

        ReferenciaFirebase.setValue(planoDeSaude).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {  // "SetValue" enviando um valor ao banco se já existir apenas atualiza senão ele cria
                if (task.isSuccessful()) {
                    //Toast.makeText(Contexto, "Sucesso ao cadastrar plano de saúde", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Contexto, "Falha ao cadastrar plano de saúde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void updatePlanodeSaude(PlanoDeSaude planoDeSaude, String idUsuarioLogado, String nomePlano, String numPlano) {

        //Método para salvar endereço no banco de dados do Firebase

        planoDeSaude.setIdUsuario(idUsuarioLogado); //Setando o identificador do usuario logado como identificador no plano de saúde
        planoDeSaude.setNumPlano(numPlano);
        planoDeSaude.setNomePlano(nomePlano);
        planoDeSaude.setId(idUsuarioLogado);


        ReferenciaFirebase = ReferenciaFirebase.child("planodesaude").child(idUsuarioLogado); // Consultando o banco

        ReferenciaFirebase.setValue(planoDeSaude).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {  // "SetValue" enviando um valor ao banco se já existir apenas atualiza senão ele cria
                if (task.isSuccessful()) {
                    Toast.makeText(Contexto, "Plano de Saúde atualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Contexto, "Falha ao atualizar Plano de Saúde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

