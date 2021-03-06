package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;
import br.com.projetofragmeto.clinup.model.Usuario;
import br.com.projetofragmeto.clinup.utils.MaskUtil;

public class AlterarCadastroUsuario extends AppCompatActivity {

    // Atributos para serem utilizados nessa classe
    private EditText nome, email, cpf, nomePlano, numPlano, dataNascimento, numTelefone;
    private TextInputLayout nomeIn, emailIn, cpfIn, nomePlanoIn, numPlanoIn, dataNascimentoIn, numTelefoneIn;

    private Button botaoSalvar;
    private FirebaseUser user;

    private DatabaseReference firebase;
    private DatabaseReference firebasePlano;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerPlano;
    private FirebaseAuth autenticacaoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_cadastro_usuario);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Editar");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        autenticacaoUsuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        user = ConfiguracaoFirebase.getUsuarioLogado(); // retorna o usuário que está logado no momento

        // Instanciando os ID do "activity_alterar_cadastro_usuario.xml"
        botaoSalvar = findViewById(R.id.botao_alterar);

        nome = findViewById(R.id.edit_cadastro_nomeID);
        email = findViewById(R.id.edit_cadastro_emailID);
        cpf = findViewById(R.id.edit_cadastro_cpfID);
        dataNascimento = findViewById(R.id.edit_dataNascimentoID);
        numTelefone = findViewById(R.id.edit_numTelefoneID);

        nomePlano = findViewById(R.id.edit_nomePlanoID);
        numPlano = findViewById(R.id.edit_numPlanoID);


        // Instanciando os ID da "activity_cadastro_usuario.xml"
        nomeIn = findViewById(R.id.textInput_NomeID);
        emailIn = findViewById(R.id.textInput_EmailID);
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

        Preferencias preferencesUser = new Preferencias(AlterarCadastroUsuario.this);
        final String idUsuarios = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado
        // Essa funcão pega o identificador salvo em outra activity(tela)

        firebase = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("usuarios")
                .child(idUsuarios);


        /* #################################################################################################################
            COLETANDO E OUVINDO OS DADOS DO USUÁRIO LOGADO
        */
        //OUVINTE: ele escuta os valores do banco, caso tenha alteração ele vai alterar os dados que foram coletados no conteúdo do ouvinte
        valueEventListenerUsuario = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    Usuario nUsuario = dataSnapshot.getValue(Usuario.class);

                    if (nUsuario != null) {
                        nome.setText(nUsuario.getNome());
                        email.setText(nUsuario.getEmail());
                        cpf.setText(nUsuario.getCpf());
                        numTelefone.setText(nUsuario.getNumTelefone());
                        dataNascimento.setText(nUsuario.getDataNascimento());
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

        firebase.addValueEventListener(valueEventListenerUsuario);

        /* #################################################################################################################
            COLETANDO E OUVINDO OS DADOS DO PLANO DE SAÚDE DO USUÁRIO SE TIVER
        */
        firebasePlano = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("planodesaude")
                .child(idUsuarios);

        //OUVINTE: ele escuta os valores do banco, caso tenha alteração ele vai alterar os dados que foram coletados no conteúdo do ouvinte
        valueEventListenerPlano = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    PlanoDeSaude plano = dataSnapshot.getValue(PlanoDeSaude.class);

                    if (plano != null) {
                        nomePlano.setText(plano.getNomePlano());
                        numPlano.setText(plano.getNumPlano());
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

        firebasePlano.addValueEventListener(valueEventListenerPlano);


        botaoSalvar.setOnClickListener(new View.OnClickListener() {/* Criando o evento para esperar o clique no botão
                                                                    , caso clicado ele entra e executa o conteúdo*/
            @Override
            public void onClick(View view) {

                if (submitForm()) {

                    if (nomePlano.getText().toString().isEmpty() && !numPlano.getText().toString().isEmpty()) {
                        nomePlanoValida();
                        return;
                    }
                    if (!nomePlano.getText().toString().isEmpty() && numPlano.getText().toString().isEmpty()) {
                        numPlanoValida();
                        return;
                    }

                    // Atualização do banco do usuários
                    Map<String, Object> usuarioUpdates = new HashMap<>();
                    usuarioUpdates.put("nome", nome.getText().toString());
                    usuarioUpdates.put("dataNascimento", dataNascimento.getText().toString());
                    usuarioUpdates.put("email", email.getText().toString());
                    usuarioUpdates.put("numTelefone", numTelefone.getText().toString());
                    usuarioUpdates.put("cpf", cpf.getText().toString());

                    // Atualização do banco dos planos de saúde
                    DatabaseReference planoSaudeBanco = ConfiguracaoFirebase.getFirebase().child("planodesaude").child(idUsuarios);
                    Map<String, Object> planoDeSaudeUpdate = new HashMap<>();
                    planoDeSaudeUpdate.put("id", idUsuarios);
                    planoDeSaudeUpdate.put("idUsuario", idUsuarios);
                    planoDeSaudeUpdate.put("nomePlano", nomePlano.getText().toString());
                    planoDeSaudeUpdate.put("numPlano", numPlano.getText().toString());

                    usuarioUpdates.put("planoDeSaude", idUsuarios);

                    planoSaudeBanco.updateChildren(planoDeSaudeUpdate);
                    firebase.updateChildren(usuarioUpdates);

                    irParaPerfilCliente();
                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        firebase.addValueEventListener(valueEventListenerUsuario);
        firebasePlano.addValueEventListener(valueEventListenerPlano);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (valueEventListenerUsuario != null) {
            firebase.removeEventListener(valueEventListenerUsuario);
        }

        if (valueEventListenerPlano != null) {
            firebasePlano.removeEventListener(valueEventListenerPlano);
        }
    }

    private boolean submitForm() {
        return validarNome() && validarEmail() && validarCpf();

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

    private boolean nomePlanoValida() {
        if (nomePlano.getText().toString().trim().isEmpty()) {
            nomePlanoIn.setError("Se preencheu o número do plano é preciso dizer qual é o nome do plano");
            requestFocus(nomePlano);
            return false;
        } else {
            nomePlanoIn.setErrorEnabled(false);
        }

        return true;
    }

    private boolean numPlanoValida() {
        if (numPlano.getText().toString().trim().isEmpty()) {
            numPlanoIn.setError("Se preencheu o nome do plano é preciso dizer qual é o número do plano");
            requestFocus(numPlano);
            return false;
        } else {
            numPlanoIn.setErrorEnabled(false);
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
            }
        }
    }

    public void irParaPerfilCliente() {
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if (item.getItemId() == android.R.id.home)
            irParaPerfilCliente();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlterarCadastroUsuario.this, PerfilActivity.class);
        startActivity(intent);
        finish();
    }
}
