package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.fragments.HomeFragment;
import br.com.projetofragmeto.clinup.fragments.PerfilFragment;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Usuario;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nomeUser;
    private TextView emailUser;
    private String idUsuarios;
    private Usuario usuario = new Usuario();
    private DatabaseReference usuarioReferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToBuscaActivity();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        HomeFragment homeFragment = new HomeFragment(); // instancia o fragment home
        getSupportFragmentManager().beginTransaction().replace(R.id.conteudo_fragment,homeFragment).commit(); // exibe o fragmentHome
        //############################################################################################################################


        // pega o id do navigation view; com ele eu vou poder setar o nome, email e foto do usuário conectado
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout mParent = ( LinearLayout ) navigationView.getHeaderView( 0 );
        nomeUser = mParent.findViewById(R.id.nome_user_nav_drawer); // pega o id do nome_user do nav_header_principal
        emailUser = mParent.findViewById(R.id.email_user_nav_drawer);// pega o id do email_user do nav_header_principal

        // exibe o email e senha do usuário na navigation view

        Preferencias preferencesUser = new Preferencias(this);
        idUsuarios = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado




        usuarioReferencia = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("usuarios").child(idUsuarios);

        usuarioReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {// método chamado sempre que os dados forem alterados no banco

                //nomeUser.setText(dataSnapshot.child("nome").getValue().toString());
                //emailUser.setText(dataSnapshot.child("email").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {



        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                HomeFragment homeFragment = new HomeFragment(); // instancia o fragment home
                getSupportFragmentManager().beginTransaction().replace(R.id.conteudo_fragment,homeFragment).commit(); // exibe o fragmentHome


                break;
            case R.id.nav_Perfil:

                Intent intent = new Intent(getApplicationContext(),PerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_buscar:
                goToBuscaActivity();
                break;
            case R.id.nav_logout:

                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToBuscaActivity() {
        Intent intent = new Intent(getApplicationContext(),BuscaActivity.class);
        startActivity(intent);
    }
}
