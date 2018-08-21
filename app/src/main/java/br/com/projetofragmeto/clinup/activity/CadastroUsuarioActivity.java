package br.com.projetofragmeto.clinup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
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

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.PlanoDeSaudeImplements;
import br.com.projetofragmeto.clinup.helper.Base64Custom;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;
import br.com.projetofragmeto.clinup.model.Usuario;
import br.com.projetofragmeto.clinup.utils.MaskUtil;

public class CadastroUsuarioActivity extends AppCompatActivity {

    // Atributos para serem utilizados nessa classe
    private Button botaoCadastrar;

    private EditText nome, email, senha, cpf, nomePlano, numPlano, dataNascimento, numTelefone;

    private TextInputLayout nomeIn, emailIn, senhaIn, cpfIn, nomePlanoIn, numPlanoIn, dataNascimentoIn, numTelefoneIn;

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

        // Instanciando os ID da "activity_cadastro_usuario.xml"
        nomeIn = findViewById(R.id.textInput_NomeID);
        emailIn = findViewById(R.id.textInput_EmailID);
        senhaIn = findViewById(R.id.textInput_SenhaID);
        cpfIn = findViewById(R.id.textInput_CpfID);
        dataNascimentoIn = findViewById(R.id.textInput_nascimentoID);
        numTelefoneIn = findViewById(R.id.textInput_TelefoneID);

        nomePlanoIn = findViewById(R.id.textInput_NomPlanoID);
        numPlanoIn = findViewById(R.id.textInput_NumPlanoID);

        // Máscara para os valores dos campos
        MaskUtil maskUtil = new MaskUtil();

        maskUtil.maskCPF(cpf);
        maskUtil.maskDATA(dataNascimento);
        maskUtil.maskTelefone(numTelefone);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { /* Criando o evento para esperar o clique no botão
                                             , caso clicado ele entra e executa o conteúdo*/
                if (submitForm()) {
                    usuario = new Usuario();
                    planoDeSaude = new PlanoDeSaude();
                    Plano = new PlanoDeSaudeImplements(getApplicationContext());

                    // Coletando os dados inseridos no "layout" e setando no usuário e plano de saude
                    usuario.setNome(nome.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    usuario.setCpf(cpf.getText().toString());
                    usuario.setNumTelefone(numTelefone.getText().toString());
                    usuario.setDataNascimento(dataNascimento.getText().toString());
                    String idUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(idUsuarioLogado);

                    // Salvando no banco o plano de saúde
                    Plano.inserirPlanodeSaude(planoDeSaude, usuario.getId(), nomePlano.getText().toString(), numPlano.getText().toString());

                    // Salvando no banco o usuário
                    cadastrarUsuario(usuario);

                    AlertaCadastroEndereco();

                }
            }
        });
    }

    private void cadastrarUsuario(final Usuario usuario) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();

                    usuario.salvar(); // Salva no banco o usuário

                    // Salva o ID do usuárioLogado para ser consutado em outras telas
                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDados(usuario.getId(), usuario.getNome());
                    salvarPreferencias("id", usuario.getId());

                } else { // Alguns testes

                    String erroExcecao = "";

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        // Firebase solicita uma senha com mais de 6 digitos
                        senha.setError("Digite uma senha mais forte, contento mais caracteres e com letras e números!");
                        erroExcecao = "Digite uma senha mais forte, contento mais caracteres e com letras e números!";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está sendo utilizado!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirLogadoUsuario() { // Criando a atividade para ir para outra tela
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

    //Validar Cadastro
    private Boolean validarCadastro() {

        Boolean valor = true;

        valor = campoVazioValidar(nome, nomeIn);
        valor = campoVazioValidar(email, emailIn);
        valor = campoVazioValidar(dataNascimento, dataNascimentoIn);
        valor = campoVazioValidar(cpf, cpfIn);
        valor = campoVazioValidar(senha, senhaIn);

        return valor;
    }

    private boolean campoVazioValidar(EditText EditTexto, final TextInputLayout campo) {

        final String nTexto = EditTexto.getText().toString().trim();

        boolean valor = true;
        // Checa se o campo está vázio
        if (TextUtils.isEmpty(nTexto)) {
            campo.setError("Campo obrigatório");
            valor = false;
        }

        return valor;

    }

    private boolean submitForm() {
        if (!validarNome()) {
            return false;
        }

        if (!validarEmail()) {
            return false;
        }

        if (!validarSenha()) {
            return false;
        }

        if (!validarCpf()) {
            return false;
        }
        return true;

    }

    private boolean validarNome() {
        if (nome.getText().toString().trim().isEmpty()) {
            nomeIn.setError(getString(R.string.err_msg_name));
            requestFocus(nomeIn);
            return false;
        } else {
            nomeIn.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validarEmail() {
        String nEmail = email.getText().toString().trim();

        if (nEmail.isEmpty() || !isValidEmail(nEmail)) {
            emailIn.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            emailIn.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validarSenha() {
        if (senha.getText().toString().trim().isEmpty()) {
            senhaIn.setError(getString(R.string.err_msg_password));
            requestFocus(senha);
            return false;
        } else {
            senhaIn.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validarCpf() {
        if (cpf.getText().toString().trim().isEmpty()) {
            cpfIn.setError(getString(R.string.err_msg_cpf));
            requestFocus(cpf);
            return false;
        } else {
            cpfIn.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_cadastro_nomeID:
                    validarNome();
                    break;
                case R.id.edit_cadastro_cpfID:
                    validarCpf();
                    break;
                case R.id.edit_cadastro_emailID:
                    validarEmail();
                    break;
                case R.id.edit_cadastro_senhaID:
                    validarSenha();
                    break;
            }
        }
    }

    private void AlertaCadastroEndereco() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cadastrar Endereço");
        //define a mensagem
        builder.setMessage("Precisamos do seu endereço para que você possa agendar. Deseja cadastrar agora o seu endereo?");

        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(CadastroUsuarioActivity.this, CadastroEndereco.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                abrirLogadoUsuario();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}



