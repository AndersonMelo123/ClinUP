package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Endereco;
import br.com.projetofragmeto.clinup.model.Usuario;

public class PerfilActivity extends AppCompatActivity {
    private TextView nome, email, telefone, dataNascimento;
    private ImageView foto;
    private TextView cpf, end1, end2;
    private String idEndereco;
    private DatabaseReference enderecoReferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        foto = findViewById(R.id.imageView2);
        nome = findViewById(R.id.nome_id);
        email = findViewById(R.id.email_id);
        telefone = findViewById(R.id.telefone_id);
        dataNascimento = findViewById(R.id.data_nascimento_id);
        Button editarCadastro = findViewById(R.id.bt_editarCadastro);
        cpf = findViewById(R.id.cpf_id);
        end1 = findViewById(R.id.end1_id);
        end2 = findViewById(R.id.end2_id);

        Preferencias preferencesUser = new Preferencias(this);
        String idUsuarios = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado

        DatabaseReference usuarioReferencia = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("usuarios").child(idUsuarios);

        usuarioReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {// método chamado sempre que os dados forem alterados no banco

                if (dataSnapshot.exists()) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                    if (usuario != null) {
                        nome.setText(usuario.getNome());
                        email.setText(usuario.getEmail());
                        dataNascimento.setText(usuario.getDataNascimento());
                        telefone.setText(usuario.getNumTelefone());
                        cpf.setText(usuario.getCpf());

                        if (dataSnapshot.hasChild("endereco")) {

                            idEndereco = usuario.getEndereco();
                            enderecoReferencia = ConfiguracaoFirebase.getFirebase().child("endereco").child(idEndereco); //retornando nulo e tá dando erro

                            enderecoReferencia.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Endereco endereco = dataSnapshot.getValue(Endereco.class);

                                    if (endereco != null) {

                                        end1.setText(endereco.getLogradouro() + ", " + endereco.getNumero() + ", " + endereco.getBairro());
                                        end2.setText(endereco.getLocalidade() + ", " + endereco.getUf());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        if (dataSnapshot.hasChild("foto")) {
                            //Exibe a foto de perfil do usuário através do Glide
                            Glide.with(getApplicationContext()).asBitmap().load(usuario.getFoto()).into(new BitmapImageViewTarget(foto) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    //Transforma a foto em formato circular
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(PerfilActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    foto.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        } else {
                            foto.setImageResource(R.mipmap.foto_defau_round);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        editarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarCadastro();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void editarCadastro() {
        Intent intent = new Intent(getApplicationContext(), AlterarCadastroUsuario.class);
        startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}
