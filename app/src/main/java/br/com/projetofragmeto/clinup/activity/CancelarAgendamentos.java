package br.com.projetofragmeto.clinup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import br.com.projetofragmeto.clinup.model.Agendamento;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;

public class CancelarAgendamentos extends AppCompatActivity {

    private TextView nome;
    private TextView planoSaud;
    private TextView dataAgendamento;
    private TextView dataConsulta;

    private Button cancelar;

    private String getNome;
    private String getId;
    private String getDataAtual;
    private String getDataConsulta;
    private String getPlano;
    private String getIdCliente;

    private DatabaseReference firebase;

    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_agendamentos);

        toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Agendamento");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        nome = findViewById(R.id.nome_l);
        planoSaud = findViewById(R.id.planoSaud);
        dataAgendamento = findViewById(R.id.dataAgendamento);
        dataConsulta = findViewById(R.id.dataConsulta);
        cancelar = (Button) findViewById(R.id.cancelarAgendamento);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            getId = extras.getString("ID");
            getNome = extras.getString("nome");
            getDataAtual = extras.getString("dataAtual");
            getDataConsulta = extras.getString("dataConsulta");
            getPlano = extras.getString("plano");
            getIdCliente = extras.getString("cliente");
        }


        nome.setText(getNome);

        DatabaseReference planoRef = ConfiguracaoFirebase.getFirebase().child("planodesaude").child(getPlano);

        planoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    PlanoDeSaude userPlano = dataSnapshot.getValue(PlanoDeSaude.class);

                    if (dataSnapshot.hasChild("nomePlano"))
                        planoSaud.setText(String.valueOf(userPlano.getNomePlano()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dataAgendamento.setText(getDataAtual);
        dataConsulta.setText(getDataConsulta);


        final Agendamento ag = new Agendamento();

        ag.setId(getId);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelarAgendamento();
            }
        });
    }

    private void cancelarAgendamento(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_excluir_agendamento, null);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final DatabaseReference bancoDados = ConfiguracaoFirebase.getFirebase().child("agendamento").child(getId);

                bancoDados.removeValue();

                Toast.makeText(CancelarAgendamentos.this, "Agendamento cancelado com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                startActivity(intent);
                finish();


            }


        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(CancelarAgendamentos.this, "Cancelar", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}