package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth autenticacaoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacaoUsuario = ConfiguracaoFirebase.getFirebaseAutenticacao();

        logout = findViewById(R.id.logoutID);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                autenticacaoUsuario.signOut(); // Faz o logOut do firebase
                LoginManager.getInstance().logOut(); // Faz o logOut do facebook


                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
