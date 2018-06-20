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

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase(){

        if ( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){

        if ( autenticacao == null ){
            autenticacao = FirebaseAuth.getInstance();

        }
        return autenticacao;
    }


}
