package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Usuario;

public class PerfilActivity extends AppCompatActivity {
    private TextView nome;
    private TextView email;
    private TextView telefone;
    private TextView dataNascimento;
    private DatabaseReference usuarioReferencia;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        nome = findViewById(R.id.nome_id);
        email = findViewById(R.id.email_id);
        telefone = findViewById(R.id.telefone_id);
        dataNascimento = findViewById(R.id.data_nascimento_id);

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

                if (usuario != null) {
                    nome.setText(usuario.getNome());
                    email.setText(usuario.getEmail());
                    dataNascimento.setText(usuario.getDataNascimento());
                    telefone.setText(usuario.getNumTelefone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
