package br.com.projetofragmeto.clinup.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.PlanoDeSaudeImplements;
import br.com.projetofragmeto.clinup.helper.Base64Custom;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;
import br.com.projetofragmeto.clinup.model.Usuario;

public class AlterarCadastroUsuario extends AppCompatActivity {

    // Atributos para serem utilizados nessa classe
    private EditText nome, email, senha, cpf, nomePlano, numPlano, dataNascimento, numTelefone;

    private Button botaoSalvar;
    private FirebaseUser user;

    private DatabaseReference firebase;
    private DatabaseReference firebasePlano;
    private ValueEventListener valueEventListener;
    private ValueEventListener valueEventListenerPlano;
    private FirebaseAuth autenticacaoUsuario;

    private Usuario usuario;

    private PlanoDeSaude planoDeSaude;
    private PlanoDeSaudeImplements Plano;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_cadastro_usuario);

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

        // Criando as mascaras
        SimpleMaskFormatter nCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mCpf = new MaskTextWatcher(cpf,nCpf);
        cpf.addTextChangedListener(mCpf);
        //FIM DA MÁSCARA

        // Criando as mascaras

        MaskPattern mp03 = new MaskPattern("[0-9]");
        MaskPattern mp09 = new MaskPattern("[0-3]");
        MaskPattern mp01 = new MaskPattern("[0-1]");

        MaskFormatter mf = new MaskFormatter("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]");

        mf.registerPattern(mp01);
        mf.registerPattern(mp03);
        mf.registerPattern(mp09);

        dataNascimento.addTextChangedListener(new MaskTextWatcher(dataNascimento, mf));
        //FIM DA MÁSCARA

        // Criando as mascaras
        SimpleMaskFormatter nNumTelefone = new SimpleMaskFormatter("+NN(NN)NNNNN-NNNN");
        MaskTextWatcher mNumTelefone = new MaskTextWatcher(numTelefone,nNumTelefone);
        numTelefone.addTextChangedListener(mNumTelefone);
        //FIM DA MÁSCARA

        Preferencias preferencesUser = new Preferencias(AlterarCadastroUsuario.this);
        String idUsuarios = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado
        // Essa funcão pega o identificador salvo em outra activity(tela)

        firebase = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("usuarios")
                .child(idUsuarios);


        /* #################################################################################################################
            COLETANDO E OUVINDO OS DADOS DO USUÁRIO LOGADO
        */
        //OUVINTE: ele escuta os valores do banco, caso tenha alteração ele vai alterar os dados que foram coletados no conteúdo do ouvinte
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                    if (usuario != null ) {
                        nome.setText(usuario.getNome());
                        email.setText(usuario.getEmail());
                        cpf.setText(usuario.getCpf());
                        numTelefone.setText(usuario.getNumTelefone());
                        dataNascimento.setText(usuario.getDataNascimento());
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
                if (dataSnapshot != null){

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

                usuario = new Usuario();
                planoDeSaude = new PlanoDeSaude();
                Plano = new PlanoDeSaudeImplements(getApplicationContext());

                usuario.setNome( nome.getText().toString() );
                usuario.setEmail( email.getText().toString() );
                usuario.setCpf( cpf.getText().toString() );
                usuario.setNumTelefone( numTelefone.getText().toString() );
                usuario.setDataNascimento( dataNascimento.getText().toString());
                String idUsuarioLogado = Base64Custom.codificarBase64( usuario.getEmail() );
                usuario.setId( idUsuarioLogado );

                usuario.salvar();

                Plano.inserirPlanodeSaude(planoDeSaude, usuario.getId(),nomePlano.getText().toString(), numPlano.getText().toString());


            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        firebase.addValueEventListener(valueEventListener);
        firebasePlano.addValueEventListener(valueEventListenerPlano);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (valueEventListener != null) {
            firebase.removeEventListener(valueEventListener);
        }

        if (valueEventListenerPlano != null) {
            firebasePlano.removeEventListener(valueEventListenerPlano);
        }
    }

    //Método que salva o id do usuário nas preferências para login automático ao abrir aplicativo
    private void salvarPreferencias(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
