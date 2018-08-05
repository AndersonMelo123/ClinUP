package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Endereco;

public class PerfilCliente extends AppCompatActivity {

    private String id, telefone, especialidade, nome, numRegistro, email, endereco, cnpj, cliente, classe;
    private EditText tv_id, tv_telefone, tv_especialidade, tv_nome, tv_numRegistro, tv_email, tv_endereco;

    private Button agendar;
    private Button ligar;

    private android.support.v7.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);

        toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Perfil");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        agendar = findViewById(R.id.botao_agendar);
        //ligar = findViewById(R.id.botao_agendar);

        tv_nome = findViewById(R.id.edit_perfil_nomeID);
        tv_email = findViewById(R.id.edit_cadastro_emailID);
        tv_endereco = findViewById(R.id.edit_perfil_enderecoID);
        tv_especialidade = findViewById(R.id.edit_perfil_especialidadeID);
        tv_numRegistro = findViewById(R.id.edit_perfil_numRegistroID);
        tv_telefone = findViewById(R.id.edit_perfil_numTelefoneID);


        id = getIntent().getExtras().getString("id");

        telefone = getIntent().getExtras().getString("telefone");
        nome = getIntent().getExtras().getString("nome");
        especialidade = getIntent().getExtras().getString("especialidade");
        numRegistro = getIntent().getExtras().getString("num_registro");
        endereco = getIntent().getExtras().getString("endereco");
        email = getIntent().getExtras().getString("email");
        cnpj = getIntent().getExtras().getString("cnpj");
        cliente = getIntent().getExtras().getString("cliente");
        classe = String.valueOf(getIntent().getSerializableExtra("classe").getClass());


        if (telefone != null) {
            tv_telefone.setText(telefone);
            tv_telefone.setEnabled(true);

        }
        if (nome != null) {
            tv_nome.setText(nome);
            tv_nome.setEnabled(false);
        }
        if (especialidade != null) {
            tv_especialidade.setText(especialidade);
            tv_especialidade.setEnabled(true);

        }
        if (numRegistro != null) {
            tv_numRegistro.setText(numRegistro);
            tv_numRegistro.setEnabled(true);

        }
        if (email != null) {
            tv_email.setText(email);
            tv_email.setEnabled(true);

        }
        if (cnpj != null) {
            Log.i("Cnpj", cnpj);
        }

        if (cliente != null) {
            Log.i("cliente", cliente);

        }
        if (endereco != null) {

            DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebase().child("endereco").child(endereco);

            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Endereco userEndereco = dataSnapshot.getValue(Endereco.class);

                    tv_endereco.setText(String.valueOf(userEndereco.getLogradouro() + ", " + userEndereco.getNumero()));
                    tv_endereco.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


        agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PerfilCliente.this, AgendarActivity.class);

                intent.putExtra("nome", nome);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                intent.putExtra("endereco", endereco);
                intent.putExtra("telefone", telefone);
                intent.putExtra("cnpj", cnpj);

                intent.putExtra("cliente", cliente);
                intent.putExtra("classe", classe);
                startActivity(intent);
            }
        });

    }

    public void ligar(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (telefone.trim().isEmpty()) {
            Toast.makeText(this, "Não possui número para ligar", Toast.LENGTH_SHORT).show();
        } else {
            intent.setData(Uri.parse("tel:" + telefone));
        }
        startActivity(intent);
    }
}
