package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private Button botaoDelete;
    private TextView texto;
    private TextView texto2;

    private FirebaseAuth autenticacaoUsuario;
    private GoogleApiClient googleApiClient;
    private FirebaseUser user;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacaoUsuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        user = ConfiguracaoFirebase.getUsuarioLogado(); // retorna o usuário que está logado no momento

        logout = findViewById(R.id.logoutID);
        botaoDelete = findViewById(R.id.excluirContaButton);
        texto = findViewById(R.id.idUser);
        texto2 = findViewById(R.id.emailUser);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity
                                .this, "Login inválido",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                autenticacaoUsuario.signOut();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut(); // Faz o logOut do facebook
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        texto2.setText("Email User: " + user.getEmail());
        texto.setText("Id User: " + user.getUid());
        botaoDelete.setOnClickListener(new View.OnClickListener() { // Botão para deletar contas
            @Override
            public void onClick(View view) {

                excluirConta(user);


            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                }
            });
        }
    }


    public void logOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Sessão não foi fechada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void revoke(View view) {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi revogado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void excluirConta(final FirebaseUser user){ //método que valida o email e senha e exclui a conta do usuário
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_excluir_conta, null);
        final EditText email = view.findViewById(R.id.dialog_email);
        final EditText senha = view.findViewById(R.id.dialog_senha);
        Button validar = view.findViewById(R.id.dialog_bt_validar);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().toString().isEmpty() && !senha.getText().toString().isEmpty()){//se o email e senha forem preenchidos



                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email.getText().toString(), senha.getText().toString());//cria credencial com o email e senha informados no dialog

                    user.reauthenticate(credential) // reautentica o usuário utilizando o email e senha para verificar se é o dono da conta
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    if(task.isSuccessful()){ // o usuário foi reautenticado e é possível deletar a conta

                                        user.delete()// deleta a conta do usuário
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Conta deletada com sucesso", Toast.LENGTH_SHORT).show();
                                                            goLogInScreen();
                                                        }
                                                        else {
                                                            Toast.makeText(getApplicationContext(), "Não foi possível deletar a conta", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "E-mail ou senha incorreto!", Toast.LENGTH_SHORT).show();//msg de erro

                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Digite o Email e Senha", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
