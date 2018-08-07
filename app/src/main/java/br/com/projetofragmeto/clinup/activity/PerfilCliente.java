package br.com.projetofragmeto.clinup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Endereco;

public class PerfilCliente extends AppCompatActivity {

    private String id;
    private String telefone;
    private String nome;
    private String email;
    private String endereco;
    private String cnpj;
    private String cliente;
    private String classe;
    //private TextView tv_id;
    private TextView tv_endereco;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Perfil");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Button agendar = findViewById(R.id.botao_agendar);
        Button ligar = findViewById(R.id.botao_ligar);

        TextView tv_nome = (TextView) findViewById(R.id.edit_perfil_nomeID);
        tv_endereco = (TextView) findViewById(R.id.edit_perfil_enderecoID);
        TextView tv_especialidade = (TextView) findViewById(R.id.edit_perfil_especialidadeID);
        TextView tv_numRegistro = (TextView) findViewById(R.id.edit_perfil_numRegistroID);
        TextView tv_telefone = (TextView) findViewById(R.id.edit_perfil_numTelefoneID);
        TextView tv_horario = (TextView) findViewById(R.id.edit_perfil_horarioID);


        id = getIntent().getExtras().getString("id");

        telefone = getIntent().getExtras().getString("telefone");
        nome = getIntent().getExtras().getString("nome");
        String especialidade = getIntent().getExtras().getString("especialidade");
        String numRegistro = getIntent().getExtras().getString("num_registro");
        endereco = getIntent().getExtras().getString("endereco");
        email = getIntent().getExtras().getString("email");
        cnpj = getIntent().getExtras().getString("cnpj");

        String horaAbrir = getIntent().getExtras().getString("horaAbrir");
        String horaFechar = getIntent().getExtras().getString("horaFechar");


        Log.i("HORA", horaAbrir + " às "+ horaFechar + " hr");

        cliente = getIntent().getExtras().getString("cliente");
        classe = String.valueOf(getIntent().getSerializableExtra("classe").getClass());

        if(horaAbrir != null && horaFechar != null){
            tv_horario.setText(horaAbrir + " às "+ horaFechar + " hr");
            tv_horario.setEnabled(true);
        }

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
        /*if (email != null) {
            tv_email.setText(email);
            tv_email.setEnabled(true);

        }*/
        if (cnpj != null) {
            Log.i("Cnpj",cnpj);
        }

        if (cliente != null) {
            Log.i("cliente",cliente);

        }
        if (endereco != null) {

            DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebase().child("endereco").child(endereco);

            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Endereco userEndereco = dataSnapshot.getValue(Endereco.class);

                    tv_endereco.setText(String.valueOf(userEndereco.getLogradouro() + ", " + userEndereco.getNumero() + ", "+userEndereco.getBairro()));
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

        ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ligar(view);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
