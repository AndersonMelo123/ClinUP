package br.com.projetofragmeto.clinup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public CancelarAgendamentos() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_agendamentos);

        toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Agendamento");
        setSupportActionBar(toolbar);


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

                PlanoDeSaude userPlano = dataSnapshot.getValue(PlanoDeSaude.class);

                planoSaud.setText(String.valueOf(userPlano.getNomePlano()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //planoSaud.setText(getPlano);
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

                try {
                    String dia = diaSemana(getDataConsulta);
                    Log.i("DIA", dia);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
/*
                final DatabaseReference bd = ConfiguracaoFirebase.getFirebase().child("profissionais").child(getIdCliente);

                bd.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Profissional pro = dataSnapshot.getValue(Profissional.class);

                        int d = pro.getDias().getDomingo();
                        //Log.i("DATA", String.valueOf(d));
                        pro.getDias().setDomingo(d+1);
                        int f = pro.getDias().getDomingo();
                        //Log.i("DATADADATA", String.valueOf(f));

                        //firebase.child("profissionais").child(ge)
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
*/

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

    // Pega o dia da semana atual
    private String diaSemana(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date ontem = sdf.parse(strDate);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(ontem);
        int diaDaSemana = gc.get(GregorianCalendar.DAY_OF_WEEK);
        String dia = null;
        switch (diaDaSemana) {
            case 1:
                return dia = "domingo";
            case 2:
                return dia = "segunda";
            case 3:
                return dia = "terca";
            case 4:
                return dia = "quarta";
            case 5:
                return dia = "quinta";
            case 6:
                return dia = "sexta";
            case 7:
                return dia = "sabado";

        }
        return dia;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}
