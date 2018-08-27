package br.com.projetofragmeto.clinup.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Base64Custom;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    // Atributos para serem utilizados nessa classe
    private EditText email, senha;

    private String identificadorUsuarioLogado;

    private LoginButton botaoLoginFacebook;
    private CallbackManager callbackManager;

    private Usuario usuario;
    private ValueEventListener valueEventListenerUsuario;

    private DatabaseReference firebase;
    private FirebaseAuth autenticacao;//autenticação do firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado(); /* Verifica se o usuario está logado,
                                     se estiver ele redireciona para a tela principal*/

        // Instanciando os ID do "activity_login.xml"
        email = findViewById(R.id.edit_login_emailID);
        senha = findViewById(R.id.edit_login_senhaID);
        Button botaoLogar = findViewById(R.id.botao_logarID);
        botaoLoginFacebook = findViewById(R.id.login_facebook_button);

        firebase = FirebaseDatabase.getInstance().getReference();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/* Criando o evento para esperar o clique no botão
                                             , caso clicado ele entra e executa o conteúdo*/
                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                validarLogin();
            }
        });

        // Inicializa o botão do Facebook
        botaoLoginFacebook.setReadPermissions("email", "public_profile");
        // Inicializa callback
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
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("TAG", String.valueOf(e));
        } catch (NoSuchAlgorithmException e) {
            Log.i("TAG", String.valueOf(e));
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (AccessToken.getCurrentAccessToken() != null && user != null) {

                    //Se o login pelo Facebook foi realizado com sucesso pela segunda vez na mesma sessão
                    if (!getPreferencesKeyUsuarioFacebook(LoginActivity.this)) {

                        String idUsuario = Base64Custom.codificarBase64(user.getEmail());
                        Preferencias preferencias = new Preferencias(LoginActivity.this);
                        preferencias.salvarDados(idUsuario, user.getDisplayName());
                        salvarPreferencias("id", idUsuario);

                        Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show(); // Mensagem na tela do usuário
                        abrirTelaPrincipal();


                        //Se o login pelo Facebook foi realizado com sucesso pela primeira vez na sessão
                    } else if (getPreferencesKeyUsuarioFacebook(LoginActivity.this)) {

                        String userId = AccessToken.getCurrentAccessToken().getUserId(); // Pegar o id do facebook do usuário para coletar a imagem do perfil
                        usuario = new Usuario(user.getDisplayName(), user.getEmail(), "https://graph.facebook.com/" + userId + "/picture?type=large");
                        String idUsuario = Base64Custom.codificarBase64(user.getEmail());
                        usuario.setId(idUsuario);

                        Preferencias preferencias = new Preferencias(LoginActivity.this);
                        preferencias.salvarDados(idUsuario, usuario.getNome());
                        salvarPreferencias("idFacebook", idUsuario);

                        confirmarCadastroEnderecoFacebook(usuario);

                    }
                }
            }
        };


        SignInButton mLoginGoogle = findViewById(R.id.btnLoginGoogle);

        //Configuração do Google Sign In para login pelo Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Login inválido", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se clicar no botão do facebook ele é direcionado para fazer o login
                signIn();
            }
        });

    }

    // Método chamado assim que é inicializado essa activity
    @Override
    public void onStart() {
        super.onStart();
        autenticacao.addAuthStateListener(mAuthListener); // Atualiza e começa a ouvir se a alguma mudança no usuário atual
    }

    // Método chamado assim que saí do aplicativo sem fecha-lo
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            autenticacao.removeAuthStateListener(mAuthListener); // Para de ouvir se a alguma mudança no usuário atual
        }
    }

    private void verificarUsuarioLogado() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() != null && getPreferencesKeyVerificarLogado(this)) { //Verificando se existe usuário logado
            abrirTelaPrincipal();
        }
        //Se já estiver logado pelo Facebook
        if (isLoggedIn())
            abrirTelaPrincipal(); // Verificando se existe algum usuário logado com o facebook
    }

    private void validarLogin() {

        String Email = email.getText().toString().trim();
        String password = senha.getText().toString().trim();

        // checando se email e senhas estão vazios
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

                            if (dataSnapshot.exists()) {
                                Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                                Preferencias preferencias = new Preferencias(LoginActivity.this);
                                preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getNome());
                            }
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

    // Abrir tela principal
    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    // Abrir tela para cadastrar o usuário EMAIL e SENHA
    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }

    // Login do Google
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


            //Se o login pelo Google foi realizado com sucesso pela segunda vez na mesma sessão
            if (result.isSuccess() && !getPreferencesKeyUsuarioGoogle(LoginActivity.this)) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                String idUsuario = Base64Custom.codificarBase64(account.getEmail());
                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.salvarDados(idUsuario, account.getDisplayName());
                salvarPreferencias("id", idUsuario);

                Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show(); // Mensagem na tela do usuário
                abrirTelaPrincipal();

                //Se o login pelo Google foi realizado com sucesso pela primeira vez na sessão
            } else if (result.isSuccess() && getPreferencesKeyUsuarioGoogle(LoginActivity.this)) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                String idUsuario = Base64Custom.codificarBase64(account.getEmail());
                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.salvarDados(idUsuario, account.getDisplayName());
                salvarPreferencias("id", idUsuario);

                confirmarCadastroEnderecoGoogle(account);

            } else {
                Toast.makeText(LoginActivity.this, "Falha ao fazer login!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Autenticando o usuário do Google com o firebase
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

    // Coleta o perfil do facebook logado no app do FACEBOOK ou entrar com um novo usuário do FACEBOOK
    private void clickBotaoFacebook() {
        botaoLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLogin(loginResult.getAccessToken()); // Verificando se existe um token do facebook
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Algo de errado aconteceu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Login no facebook
    private void firebaseLogin(AccessToken accessToken) {
        AuthCredential credencial = FacebookAuthProvider.getCredential(accessToken.getToken());
        autenticacao.signInWithCredential(credencial)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //vazio
                        } else {
                            Toast.makeText(LoginActivity.this, "Erro de autenticação com Firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Método que recupera o id do usuário logado pelo Google
    public static boolean getPreferencesKeyUsuarioGoogle(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("idGoogle");
    }

    //Método que recupera o id do usuário logado pelo Google
    public static boolean getPreferencesKeyUsuarioFacebook(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("idFacebook");
    }

    //Método que salva o id do usuário nas preferências para login automático ao abrir aplicativo
    private void salvarPreferencias(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //Método que recupera o id do usuário logado para acessar seus dados e salvar suas informações no banco
    public static boolean getPreferencesKeyVerificarLogado(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains("id");
    }

    //Método que salva o usuário logado pelo Google no banco
    private void salvarUsuarioGoogle(GoogleSignInAccount account) {
        try {

            usuario = new Usuario(account.getId(), account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());

            //Chamada do DAO para salvar no banco
            inserirUsuario(usuario);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Método para salvar usuário no banco de dados do Firebase
    public void inserirUsuario(final Usuario usuario) {

        // Criando uma segurança para salvar o usuário no banco de dados
        final String idUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

        firebase.child("usuarios").child(idUsuarioLogado).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Verifica se o usuário já está salvo no banco de dados
                //Se já estiver, exibe apenas mensagem de login realizado com sucesso

                if (dataSnapshot.exists()) {

                    // Mensagem na tela do celular
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                }
                //Senão, salva o novo usuário no banco
                else {
                    firebase.child("usuarios").child(idUsuarioLogado).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    //Método que verifica se o usuário está logado pelo Facebook
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    //Método que exibe pergunta de confirmação ao usuário logado pelo Google se o mesmo deseja cadastrar um endereço
    public void confirmarCadastroEnderecoGoogle(final GoogleSignInAccount account) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Cadastrar Endereço");
        //define a mensagem
        builder.setMessage("Precisamos do seu endereço para que você possa agendar. Deseja cadastrar agora o seu endereo?");
        builder.setCancelable(false);

        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Botão sim foi clicado
                salvarUsuarioGoogle(account);

                Intent intent = new Intent(LoginActivity.this, CadastroEndereco.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Botão não foi clicado
                salvarUsuarioGoogle(account);
                abrirTelaPrincipal();
            }
        });


        alerta = builder.create();
        alerta.show();

    }

    //Método que exibe pergunta de confirmação ao usuário logado pelo Facebook se o mesmo deseja cadastrar um endereço
    public void confirmarCadastroEnderecoFacebook(final Usuario usuario) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Cadastrar Endereço");
        //define a mensagem
        builder.setMessage("Precisamos do seu endereço para que você possa agendar. Deseja cadastrar agora o seu endereo?");
        builder.setCancelable(false);

        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Botão sim foi clicado

                inserirUsuario(usuario);

                Intent intent = new Intent(LoginActivity.this, CadastroEndereco.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Botão não foi clicado
                inserirUsuario(usuario);

                abrirTelaPrincipal();
            }
        });


        alerta = builder.create();
        alerta.show();

    }

}