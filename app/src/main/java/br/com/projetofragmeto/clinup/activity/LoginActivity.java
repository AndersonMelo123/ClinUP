package br.com.projetofragmeto.clinup.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Base64Custom;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Button mLoginButtonFacebook;

    private LoginButton botaoLoginFacebook;
    private CallbackManager callbackManager;

    private Usuario usuario;
    private FirebaseAuth autenticacao;//autenticação do firebase
    private String identificadorUsuarioLogado;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference firebase;

    //private static Boolean loginAutomatico = false;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;

    private SignInButton mLoginGoogle;
    // Login com Facebook
    private FirebaseAuth.AuthStateListener firebaseAuthListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();

        verificarUsuarioLogado(); /* Verifica se o usuario está logado,
                                     se estiver ele redireciona para a tela principal*/

        firebase = ConfiguracaoFirebase.getFirebase();

        email = findViewById(R.id.edit_login_emailID);
        senha = findViewById(R.id.edit_login_senhaID);
        botaoLogar = findViewById(R.id.botao_logarID);
        mLoginButtonFacebook = (LoginButton)findViewById(R.id.login_facebook_button);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                validarLogin();

            }
        });

        mLoginGoogle = findViewById(R.id.btnLoginGoogle);

        //Configuração do Google Sign In para login pelo Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, R.string.erro_login_invalido_Toast, Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        //Obter as permissões do perfil público e email do usuário logado pelo facebbok
        mLoginButtonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });

        //Registrar um callback para realizar um login pelo Facebook
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject,
                                                    GraphResponse response) {

                                //Obter os dados do usuário do facebook
                                Bundle facebookData = getFacebookData(jsonObject);

                                if(getPreferencesKeyConsumidorFacebook(LoginActivity.this)){
                                    salvarPreferencias("id", facebookData.getString("idFacebook"));
                                    alert("Sucesso ao fazer login");
                                    abrirTelaPrincipal();
                                }
                                else{
                                    salvarPreferencias("idFacebook", facebookData.getString("idFacebook"));
                                    salvarUsuarioFacebook(facebookData);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                alert("Operação cancelada");
            }

            @Override
            public void onError(FacebookException error) {
                alert("Erro de autenticação com Firebase");
            }
        });

    }

    private void verificarUsuarioLogado() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Se já estiver logado pelo Google
        if (autenticacao.getCurrentUser() != null && getPreferencesKeyConsumidorVerificarLogado(this)) {
            abrirTelaPrincipal();
        }
        //Se já estiver logado pelo Facebook
        if(isLoggedIn()) abrirTelaPrincipal();
    }

    //Método para válidar o login e-mail e senha
    private void validarLogin() {

        String Email = email.getText().toString().trim();
        String password = senha.getText().toString().trim();

        //Checando se o e-mail e senha está vazio na interface
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Por favor entre com o e-mail", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor entre com a senha", Toast.LENGTH_LONG).show();
            return;
        }

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(identificadorUsuarioLogado);

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getNome());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent(valueEventListenerUsuario); // Única consulta

                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                    abrirTelaPrincipal();
                } else {
                    Toast.makeText(LoginActivity.this, "Falha ao fazer login!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.i("Callbackk", String.valueOf(callbackManager));
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            //Se o login pelo Google foi realizado com sucesso pela segunda vez na mesma sessão
            if (result.isSuccess() && getPreferencesKeyConsumidorGoogle(this)) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                salvarPreferencias("id", account.getId());
                Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                abrirTelaPrincipal();

                //Se o login pelo Google foi realizado com sucesso pela primeira vez na sessão
            } else if (result.isSuccess() && !getPreferencesKeyConsumidorGoogle(this)) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                salvarPreferencias("idGoogle", account.getId());
                salvarUsuarioGoogle(account);
                abrirTelaPrincipal();

            } else {
                Toast.makeText(LoginActivity.this, "Falha ao fazer login!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //vazio
                    }
                });
    }

    //Método que recupera o id do usuário logado para acessar seus dados e salvar suas informações no banco
    public static boolean getPreferencesKeyConsumidorVerificarLogado(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("id");
    }

    //Método que recupera o id do usuário logado pelo Google
    public static boolean getPreferencesKeyConsumidorGoogle(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("idGoogle");
    }

    //Método que salva o id do usuário nas preferências para login automático ao abrir aplicativo
    private void salvarPreferencias(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //Método que salva o usuário logado pelo Google no banco
    private boolean salvarUsuarioGoogle(GoogleSignInAccount account) {
        try {

            usuario = new Usuario(account.getId(), account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());

            salvarPreferencias("id", account.getId());

            //Chamada do DAO para salvar no banco
            inserirUsuario(usuario, account.getId());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Método para salvar usuário no banco de dados do Firebase
    public void inserirUsuario(final Usuario usuario, String idUsuarioLogado) {

        idUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

        final String finalIdUsuarioLogado = idUsuarioLogado;
        firebase.child("usuarios").child(finalIdUsuarioLogado).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Verifica se o usuário já está salvo no banco de dados
                //Se já estiver, exibe apenas mensagem de login realizado com sucesso

                if (dataSnapshot.exists()) {

                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                }
                //Senão, salva o novo usuário no banco
                else {
                    firebase.child("usuarios").child(finalIdUsuarioLogado).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sucesso ao fazer login", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //vazio
            }
        });
    }

    private void alert(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    //Método que verifica se o usuário está logado pelo Facebook
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    //Método que recupera o id do usuário logado pelo Facebook
    public static boolean getPreferencesKeyConsumidorFacebook(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("idFacebook");
    }

    //Método que salva o usuário logado pelo Facebook no banco
    private boolean salvarUsuarioFacebook(Bundle facebookData) {
        try {

            usuario = new Usuario(facebookData.getString("idFacebook"), facebookData.getString("first_name") + " " + facebookData.getString("last_name")
                    ,facebookData.getString("email"), facebookData.getString("profile_pic"));
            salvarPreferencias("id", facebookData.getString("idFacebook"));

            //Salvar no banco do firebase
            inserirUsuario(usuario, facebookData.getString("idFacebook"));
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Metodo que recupera os dados do usuário logado pelo Facebook
    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                //Obter a foto de perfil
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

        } catch (Exception e) {
            Log.d("VER", "BUNDLE Exception : "+e.toString());
        }

        return bundle;
    }


}