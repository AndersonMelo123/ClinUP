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

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.PlanoDeSaudeImplements;
import br.com.projetofragmeto.clinup.helper.Base64Custom;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;
import br.com.projetofragmeto.clinup.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    // Atributos para serem utilizados nessa classe
    private Button botaoCadastrar;

    private EditText nome, email, senha, cpf, nomePlano, numPlano, dataNascimento, numTelefone;

    private PlanoDeSaude planoDeSaude;
    private PlanoDeSaudeImplements Plano;

    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        // Instanciando os ID do "activity_cadastro_usuario.xml"
        nome = findViewById(R.id.edit_cadastro_nomeID);
        email = findViewById(R.id.edit_cadastro_emailID);
        senha = findViewById(R.id.edit_cadastro_senhaID);
        cpf = findViewById(R.id.edit_cadastro_cpfID);
        dataNascimento = findViewById(R.id.edit_dataNascimentoID);
        numTelefone = findViewById(R.id.edit_numTelefoneID);

        nomePlano = findViewById(R.id.edit_nomePlanoID);
        numPlano = findViewById(R.id.edit_numPlanoID);

        botaoCadastrar = findViewById(R.id.bt_cadastrarID);

        // Criando as mascaras
        SimpleMaskFormatter nCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mCpf = new MaskTextWatcher(cpf,nCpf);
        cpf.addTextChangedListener(mCpf);
        //FIM DA MÁSCARA

        // Criando as mascaras

        MaskPattern mp03 = new MaskPattern("[0-3]");
        MaskPattern mp09 = new MaskPattern("[0-9]");
        MaskPattern mp01 = new MaskPattern("[0-1]");

        MaskFormatter mf = new MaskFormatter("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]");

        mf.registerPattern(mp01);
        mf.registerPattern(mp03);
        mf.registerPattern(mp09);

        dataNascimento.addTextChangedListener(new MaskTextWatcher(dataNascimento, mf));
        //FIM DA MÁSCARA

        // Criando as mascaras
        SimpleMaskFormatter nNumTelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mNumTelefone = new MaskTextWatcher(numTelefone,nNumTelefone);
        numTelefone.addTextChangedListener(mNumTelefone);
        //FIM DA MÁSCARA



        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { /* Criando o evento para esperar o clique no botão
                                             , caso clicado ele entra e executa o conteúdo*/
                usuario = new Usuario();
                planoDeSaude = new PlanoDeSaude();
                Plano = new PlanoDeSaudeImplements(getApplicationContext());

                // Coletando os dados inseridos no "layout" e setando no usuário e plano de saude
                usuario.setNome( nome.getText().toString() );
                usuario.setEmail( email.getText().toString() );
                usuario.setSenha( senha.getText().toString() );
                usuario.setCpf( cpf.getText().toString() );
                usuario.setNumTelefone( numTelefone.getText().toString() );
                usuario.setDataNascimento( dataNascimento.getText().toString());
                String idUsuarioLogado = Base64Custom.codificarBase64( usuario.getEmail() );
                usuario.setId( idUsuarioLogado );

                // Salvando no banco o plano de saúde
                Plano.inserirPlanodeSaude(planoDeSaude, usuario.getId(),nomePlano.getText().toString(), numPlano.getText().toString());

                // Salvando no banco o usuário
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

                    usuario.salvar(); // Salva no banco o usuário

                    // Salva o ID do usuárioLogado para ser consutado em outras telas
                    Preferencias preferencias = new Preferencias( CadastroUsuarioActivity.this );
                    preferencias.salvarDados( usuario.getId(), usuario.getNome() );
                    salvarPreferencias("id", usuario.getId());

                    abrirLogadoUsuario();

                }else { // Alguns testes

                    String erroExcecao = "";

                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        // Firebase solicita uma senha com mais de 6 digitos
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

    private void abrirLogadoUsuario(){ // Criando a atividade para ir para outra tela
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finalizando essa activity
    }

    //Método que salva o id do usuário nas preferências para login automático ao abrir aplicativo
    private void salvarPreferencias(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}

