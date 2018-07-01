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

    private DatabaseReference ReferenciaFirebase;
    private final Context Contexto;

    public PlanoDeSaudeImplements(Context mContexto) {
            this.ReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
            this.Contexto = mContexto;
        }

    // Método para inserir o plano de saúde no banco de dados
    @Override
    public void inserirPlanodeSaude(PlanoDeSaude planoDeSaude, String idUsuarioLogado, String nomePlano, String numPlano) {

            //Método para salvar endereço no banco de dados do Firebase

            planoDeSaude.setIdUsuario( idUsuarioLogado );
            planoDeSaude.setNumPlano( numPlano );
            planoDeSaude.setNomePlano( nomePlano );

            //O método push() cria uma chave exclusiva para cada endereço cadastrado

            ReferenciaFirebase = ReferenciaFirebase.child("planodesaude").push();
            final String id = ReferenciaFirebase.getKey();
            planoDeSaude.setId(id);

            ReferenciaFirebase.setValue(planoDeSaude).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Contexto, "Sucesso ao cadastrar plano de saúde", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Contexto, "Falha ao cadastrar plano de saúde", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        private Context getContexto(){
            return this.Contexto;

        }
    }

