package br.com.projetofragmeto.clinup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Endereco;
import br.com.projetofragmeto.clinup.model.Favoritos;
import br.com.projetofragmeto.clinup.model.Usuario;

public class PerfilCliente extends AppCompatActivity {

    private String id, idFavorito;
    private String telefone;
    private String nome;
    private String email;
    private String endereco;
    private String enderecoAgendamento;
    private String cnpj;
    private String cliente;
    private String classe;
    private String nomeUser;
    //private TextView tv_id;
    private TextView tv_endereco;

    DatabaseReference firebase;
    Boolean verifica = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        final Button favoritos = findViewById(R.id.botao_favorito);

        TextView tv_nome = (TextView) findViewById(R.id.edit_perfil_nomeID);
        tv_endereco = (TextView) findViewById(R.id.edit_perfil_enderecoID);
        TextView tv_especialidade = (TextView) findViewById(R.id.edit_perfil_especialidadeID);
        TextView tv_numRegistro = (TextView) findViewById(R.id.edit_perfil_numRegistroID);
        TextView tv_telefone = (TextView) findViewById(R.id.edit_perfil_numTelefoneID);
        TextView tv_horario = (TextView) findViewById(R.id.edit_perfil_horarioID);


        id = getIntent().getExtras().getString("id");

        telefone = getIntent().getExtras().getString("telefone");
        nome = getIntent().getExtras().getString("nome");
        final String especialidade = getIntent().getExtras().getString("especialidade");
        String numRegistro = getIntent().getExtras().getString("numRegistro");
        endereco = getIntent().getExtras().getString("endereco");
        email = getIntent().getExtras().getString("email");
        cnpj = getIntent().getExtras().getString("cnpj");

        String horaAbrir = getIntent().getExtras().getString("horaAbrir");
        String horaFechar = getIntent().getExtras().getString("horaFechar");


        Log.i("HORA", horaAbrir + " às " + horaFechar + " hr");

        cliente = getIntent().getExtras().getString("cliente");
        classe = String.valueOf(getIntent().getSerializableExtra("classe").getClass());

        if (horaAbrir != null && horaFechar != null) {
            tv_horario.setText(horaAbrir + " às " + horaFechar + " hr");
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
            tv_especialidade.setEnabled(false);

        }
        if (numRegistro != null) {
            tv_numRegistro.setText(numRegistro);
            tv_numRegistro.setEnabled(false);

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

                    enderecoAgendamento = String.valueOf(userEndereco.getLogradouro() + ", " + userEndereco.getNumero() + ", " + userEndereco.getBairro());
                    tv_endereco.setText(enderecoAgendamento);
                    tv_endereco.setEnabled(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        Preferencias preferencesUser = new Preferencias(this);
        final String idUsuarios = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado

        DatabaseReference ref = ConfiguracaoFirebase.getFirebase().child("favoritos");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Favoritos fav = dados.getValue(Favoritos.class);

                    if (fav.getIdCliente().equals(id) && fav.getTipo().equals(cliente) && idUsuarios.equals(fav.getIdUsuario())) {

                        favoritos.setBackgroundResource(R.drawable.ic_favorite_red);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PerfilCliente.this, AgendarActivity.class);

                intent.putExtra("nome", nome);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                intent.putExtra("endereco", enderecoAgendamento);
                intent.putExtra("telefone", telefone);
                intent.putExtra("cnpj", cnpj);
                intent.putExtra("especialidade", especialidade);
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

        firebase = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("usuarios").child(idUsuarios);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);

                if (user != null) {
                    nomeUser = user.getNome();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("favoritos");

        favoritos.setOnClickListener(new View.OnClickListener() { //adiciona cliente aos favoritos
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                firebase.addListenerForSingleValueEvent(new ValueEventListener() {//faz a consulta no banco
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dados : dataSnapshot.getChildren()) {

                            Favoritos fav = dados.getValue(Favoritos.class);//retorna cada objeto da consulta em a

                            String idUsuarioFavoritos = fav.getIdUsuario();
                            String idFav = fav.getIdCliente();
                            String tipoFav = fav.getTipo();

                            if (idUsuarioFavoritos.equals(idUsuarios) && idFav.equals(id) && tipoFav.equals(cliente)) {
                                idFavorito = fav.getId();
                                verifica = true;
                            }
                        }

                        if (verifica) {

                            final DatabaseReference bancoDados = ConfiguracaoFirebase.getFirebase().child("favoritos").child(idFavorito);

                            bancoDados.removeValue();

                            favoritos.setBackgroundResource(R.drawable.ic_favorite);

                            Toast.makeText(PerfilCliente.this, "Removido dos favoritos", Toast.LENGTH_SHORT).show();
                            verifica = false;
                            //Toast.makeText(PerfilCliente.this, "Usuário já está em Favoritos", Toast.LENGTH_SHORT).show();

                        } else {
                            DatabaseReference bancoDeDados = ConfiguracaoFirebase.getFirebase();

                            bancoDeDados = bancoDeDados.child("favoritos").push();

                            final String idkey = bancoDeDados.getKey();

                            final Favoritos favorito = new Favoritos();

                            favorito.setNomeCliente(nome);
                            favorito.setIdCliente(id);
                            favorito.setIdUsuario(idUsuarios);
                            favorito.setTipo(cliente);
                            favorito.setId(String.valueOf(idkey));

                            bancoDeDados.setValue(favorito).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    favoritos.setBackgroundResource(R.drawable.ic_favorite_red);
                                    Toast.makeText(PerfilCliente.this, "Adicionado aos Favoritos", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

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
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}