package br.com.projetofragmeto.clinup.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.projetofragmeto.clinup.R;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Base64Custom;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;
import br.com.projetofragmeto.clinup.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText cpf;
    private EditText nomePlano;
    private EditText numPlano;
    private EditText dataNascimento;
    private Button botaoCadastrar;
    private Usuario usuario;
    private PlanoDeSaude planoDeSaude;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = findViewById(R.id.edit_cadastro_nomeID);
        email = findViewById(R.id.edit_cadastro_emailID);
        senha = findViewById(R.id.edit_cadastro_senhaID);
        cpf = findViewById(R.id.edit_cadastro_cpfID);
        nomePlano = findViewById(R.id.edit_nomePlanoID);
        numPlano = findViewById(R.id.edit_numPlanoID);
        dataNascimento = findViewById(R.id.edit_dataNascimentoID);
        botaoCadastrar = findViewById(R.id.bt_cadastrarID);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                planoDeSaude = new PlanoDeSaude();
                usuario.setNome( nome.getText().toString() );
                usuario.setEmail( email.getText().toString());
                usuario.setSenha( senha.getText().toString());
                usuario.setCpf( cpf.getText().toString());
                usuario.setNomePlano( nomePlano.getText().toString());
                usuario.setNumPlano( numPlano.getText().toString());


                usuario.setDataNascimento( dataNascimento.getText().toString());


                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG ).show();

                    /* FIREBASE OBS: senha deve conter no minimo 6 caracteres
                                     E-mail tem que ser um e-mail valido
                     */

                    String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );

                    usuario.setId( identificadorUsuario );
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias( CadastroUsuarioActivity.this );
                    preferencias.salvarDados( identificadorUsuario, usuario.getNome() );
                    salvarPreferencias("id", identificadorUsuario);

                    abrirLogadoUsuario();

                }else {

                    String erroExcecao = "";

                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, contento mais caracteres e com letras e números!";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está sendo utilizado!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG ).show();
                }
            }
        });
    }

    private void abrirLogadoUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //Método que salva o id do usuário nas preferências para login automático ao abrir aplicativo
    private void salvarPreferencias(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}

