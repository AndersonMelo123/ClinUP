package br.com.projetofragmeto.clinup.config;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    private static FirebaseDatabase database;
    private static DatabaseReference referenciaFirebase; //Variável para acesso ao banco de dados do Firebase
    private static FirebaseAuth autenticacao; //Variável para acesso a autenticação de usuário no Firebase
    private static FirebaseUser user;

    public static DatabaseReference getFirebase(){ //Método para obter uma instância do banco de dados do Firebase

        if ( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        }
        return referenciaFirebase;
    }

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){ //Método para obter uma instância da Autenticação do Firebase

        if ( autenticacao == null ){
            autenticacao = FirebaseAuth.getInstance();

        }
        return autenticacao;
    }

    public static FirebaseUser getUsuarioLogado(){ // retorna o usuário que está logado no momento
         user = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser(); // retorna o usuário logado

        return user;

    }


}
