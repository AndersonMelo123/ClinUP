package br.com.projetofragmeto.clinup.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Base64Custom;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Usuario;

public class LoginActivity extends AppCompatActivity{

    private EditText email;
    private EditText senha;
    private Button botaoLogar;

    private LoginButton botaoLoginFacebook;
    private CallbackManager callbackManager;



    private Usuario usuario;
    private FirebaseAuth autenticacao;//autenticação do firebase
    private String identificadorUsuarioLogado;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference firebase;

    // Login com Facebook
    private FirebaseAuth.AuthStateListener firebaseAuthListner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado(); /* Verifica se o usuario está logado,
                                     se estiver ele redireciona para a tela principal*/

        email = findViewById(R.id.edit_login_emailID);
        senha = findViewById(R.id.edit_login_senhaID);
        botaoLogar = findViewById(R.id.botao_logarID);


        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail( email.getText().toString() );
                usuario.setSenha( senha.getText().toString() );

                validarLogin();

            }
        });

        // Initialize Facebook Login button
        //botaoLoginFacebook = findViewById(R.id.botao_loginFacebookID);
        inicializarBotaoFacebook();
        //inicializa callback
        callbackManager = CallbackManager.Factory.create();
        clickBotaoFacebook();

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "br.com.projetofragmeto.clinup.activity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }




    }




    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }


    private void validarLogin(){

        String Email = email.getText().toString().trim();
        String password  = senha.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Por favor entre com o e-mail",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Por favor entre com a senha",Toast.LENGTH_LONG).show();
            return;
        }

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    identificadorUsuarioLogado = Base64Custom.codificarBase64( usuario.getEmail() );

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child( identificadorUsuarioLogado );

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue( Usuario.class );

                            Preferencias preferencias = new Preferencias( LoginActivity.this );
                            preferencias.salvarDados( identificadorUsuarioLogado, usuarioRecuperado.getNome() );

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent( valueEventListenerUsuario ); // Única consulta


                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                    abrirTelaPrincipal();
                }else{
                    Toast.makeText(LoginActivity.this, "Falha ao fazer login!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity( intent );
        finish();
    }

    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }


    // Login com Facebook
    private void inicializarBotaoFacebook() {
        botaoLoginFacebook = findViewById(R.id.login_facebook_button);
        botaoLoginFacebook.setReadPermissions("email","public_profile");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    private void clickBotaoFacebook() {
        botaoLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                alert("Operação cancelada");
            }

            @Override
            public void onError(FacebookException error) {
                alert("Erro ao fazer login com Facebook");
            }
        });
    }

    private void firebaseLogin(AccessToken accessToken) {
        AuthCredential credencial = FacebookAuthProvider.getCredential(accessToken.getToken());
        autenticacao.signInWithCredential(credencial)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            abrirTelaPrincipal();
                        }else {
                            alert("Erro de autenticação com Firebase");
                        }

                    }
                });

    }

    private void alert(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}




