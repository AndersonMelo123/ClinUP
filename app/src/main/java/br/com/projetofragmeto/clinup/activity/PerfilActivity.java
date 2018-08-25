package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import br.com.projetofragmeto.clinup.model.Usuario;

public class PerfilActivity extends AppCompatActivity {
    private TextView nome, email, telefone, dataNascimento;
    private ImageView foto;
    private Button editarCadastro;
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

        foto = findViewById(R.id.imageView2);
        nome = findViewById(R.id.nome_id);
        email = findViewById(R.id.email_id);
        telefone = findViewById(R.id.telefone_id);
        dataNascimento = findViewById(R.id.data_nascimento_id);
        editarCadastro = findViewById(R.id.bt_editarCadastro);

        Intent intent = getIntent(); //recebe os dados da activity principal

        Bundle bundle = intent.getExtras();

        Preferencias preferencesUser = new Preferencias(this);
        String idUsuarios = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado

        usuarioReferencia = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
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
                        telefone.setText(usuario.getTelefone());

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
                        }else{
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
