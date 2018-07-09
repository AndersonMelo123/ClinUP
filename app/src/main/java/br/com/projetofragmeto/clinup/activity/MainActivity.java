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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Usuario;


public class MainActivity extends AppCompatActivity {

    private Button logout;
    private Button botaoDelete;
    private TextView texto;
    private TextView texto2;
    private Button alterarConta;

    private FirebaseAuth autenticacaoUsuario;
    private GoogleApiClient googleApiClient;
    private FirebaseUser user;

    private TextView nomeText;
    private TextView emailText;
    private TextView idUsuario;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacaoUsuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        user = ConfiguracaoFirebase.getUsuarioLogado(); // retorna o usuário que está logado no momento

        // Instanciando os ID do "activity_main.xml"
        logout = findViewById(R.id.logoutID);
        alterarConta = findViewById(R.id.AlterarContaButton);
        botaoDelete = findViewById(R.id.excluirContaButton);

        nomeText = findViewById(R.id.text_nomeID);
        emailText = findViewById(R.id.emailUser);
        idUsuario = findViewById(R.id.idUser);

        Preferencias preferencesUser = new Preferencias(MainActivity.this);
        String idUsuarios = preferencesUser.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios")
                .child(idUsuarios);

        Log.i("IdUsuario",idUsuarios);

        //OUVINTE: ele escuta os valores do banco, caso tenha alteração ele vai alterar os dados que foram coletados no conteúdo do ouvinte
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                     if (usuario.getNome() != null && usuario.getEmail() != null) {

                         String nome = usuario.getNome();
                         String email = usuario.getEmail();

                         Log.i("MEUNOME",nome);
                         Log.i("MEUEMAIL",email);

                         nomeText.setText(nome);
                         emailText.setText(email);
                     }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Ouvinte Cancelado", "loadUsuario:onCancelled", databaseError.toException());
                // ...
            }
        };
        firebase.addValueEventListener(valueEventListener);


    //#################################################################################
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

        //#################################################################################

        alterarConta.setOnClickListener(new View.OnClickListener() { // Botão para ir para a tela de alterar dados do usuario
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlterarCadastroUsuario.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        firebase.addValueEventListener(valueEventListener);

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

    @Override
    public void onStop() {
        super.onStop();
        if (valueEventListener != null) {
            firebase.removeEventListener(valueEventListener);
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
