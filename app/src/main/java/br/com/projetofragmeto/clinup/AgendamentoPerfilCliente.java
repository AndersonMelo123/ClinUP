package br.com.projetofragmeto.clinup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Endereço;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;
import br.com.projetofragmeto.clinup.model.Usuario;

public class AgendamentoPerfilCliente extends AppCompatActivity {

    private TextView nome;
    private TextView endereco;
    private TextView telefone;
    private TextView dataNascimento;
    private TextView planoDeSaude;
    private TextView email;


    private DatabaseReference usuarioReferencia;
    private android.support.v7.widget.Toolbar toolbar;

    public AgendamentoPerfilCliente(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_perfil_cliente);

        nome = findViewById(R.id.nome);
        endereco = findViewById(R.id.endereco);
        telefone = findViewById(R.id.telefone);
        planoDeSaude = findViewById(R.id.planoSaud);
        dataNascimento = findViewById(R.id.dataNascimento);
        email = findViewById(R.id.email);

        Intent intent = getIntent(); //recebe os dados da activity principal

        Bundle bundle = intent.getExtras();

        Preferencias preferencesUser = new Preferencias(this);
        String idUsuarios = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado

        usuarioReferencia = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("usuarios").child(idUsuarios);

        usuarioReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {// método chamado sempre que os dados forem alterados no banco

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                String end = usuario.getEndereco();

                if(usuario != null) {
                    nome.setText(usuario.getNome());
                    //endereco.setText(usuario.getEndereco());
                    email.setText(usuario.getEmail());
                    planoDeSaude.setText(usuario.getPlanoDeSaude());
                    dataNascimento.setText(usuario.getDataNascimento());
                    telefone.setText(usuario.getTelefone());
                }

                DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebase().child("endereco").child(end);

                usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Endereço userEndereco = dataSnapshot.getValue(Endereço.class);

                        endereco.setText(String.valueOf(userEndereco.getRua()+", "+userEndereco.getNumero()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                String planoSaude = usuario.getPlanoDeSaude();

                DatabaseReference planoRef = ConfiguracaoFirebase.getFirebase().child("planodesaude").child(planoSaude);

                planoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        PlanoDeSaude userPlano = dataSnapshot.getValue(PlanoDeSaude.class);

                        planoDeSaude.setText(String.valueOf(userPlano.getNomePlano()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
