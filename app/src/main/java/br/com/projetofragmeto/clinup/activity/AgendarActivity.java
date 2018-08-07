package br.com.projetofragmeto.clinup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Agendamento;
import br.com.projetofragmeto.clinup.model.Endereco;
import br.com.projetofragmeto.clinup.model.PlanoDeSaude;
import br.com.projetofragmeto.clinup.model.Usuario;

public class AgendarActivity extends FragmentActivity {

    private ArrayList<Date> DesativarDatas = new ArrayList<>();
    private ArrayList<Date> AtivarDatas = new ArrayList<>();

    private String id, cliente;

    private TextView nome, endereco, telefone, dataNascimento, planoDeSaude, email, dataAgendamento;

    private DatabaseReference usuarioReferencia;

    private Button bt_agendar;

    Object classe;

    DatabaseReference firebase;

    private String dataSelecionada;

    private Agendamento agendamento = new Agendamento();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar);

        nome = findViewById(R.id.nome);
        endereco = findViewById(R.id.endereco);
        telefone = findViewById(R.id.telefone);
        planoDeSaude = findViewById(R.id.planoSaud);
        dataNascimento = findViewById(R.id.dataNascimento);
        email = findViewById(R.id.email);

        dataAgendamento = findViewById(R.id.tv_DataID);

        bt_agendar = findViewById(R.id.bt_agendarID);

        final ArrayList diasDaSemana = new ArrayList();

        Calendar calendar = Calendar.getInstance();

        final CaldroidFragment caldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));

        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Create a hash map
        Map<Date, Drawable> hm = new HashMap<>();


        id = getIntent().getExtras().getString("id"); // Pegar o ID do cliente do fragment_Buscar...
        cliente = getIntent().getExtras().getString("cliente");
        classe = getIntent().getSerializableExtra("classe").getClass();

        firebase = ConfiguracaoFirebase.getFirebase().child(cliente).child("001").child("dias");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                diasDaSemana.clear();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot dias : dataSnapshot.getChildren()) {

                        diasDaSemana.add(dias);

                    }

                }
                try {
                    AtivarDatas = setCalendario(diasDaSemana);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DesativarDatas = datasDesativadas();
                caldroidFragment.setDisableDates(DesativarDatas);

                caldroidFragment.setBackgroundDrawableForDates(colorirDatasAtivas(AtivarDatas));

                caldroidFragment.refreshView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        caldroidFragment.setMinDate(calendar.getTime()); //Setando a data minima

        caldroidFragment.refreshView(); // Refresh no calendário


        CaldroidListener calendario = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                dataSelecionada = format.format(date);
                Toast.makeText(AgendarActivity.this, dataSelecionada, Toast.LENGTH_SHORT).show();
                dataAgendamento.setText(dataSelecionada);


            }

        };

        caldroidFragment.setCaldroidListener(calendario);


        Preferencias preferencesUser = new Preferencias(this);
        final String idUsuarios = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado

        usuarioReferencia = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("usuarios").child(idUsuarios);

        usuarioReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {// método chamado sempre que os dados forem alterados no banco

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                String end = usuario.getEndereco();

                if (usuario != null) {
                    nome.setText(usuario.getNome());
                    //endereco.setText(usuario.getEndereco());
                    email.setText(usuario.getEmail());
                    planoDeSaude.setText(usuario.getPlanoDeSaude());
                    dataNascimento.setText(usuario.getDataNascimento());
                    telefone.setText(usuario.getTelefone());

                    agendamento.setNomeUsuario(usuario.getNome());
                    agendamento.setId_Plano(String.valueOf(usuario.getPlanoDeSaude()));

                }

                DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebase().child("endereco").child(end);

                usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Endereco userEndereco = dataSnapshot.getValue(Endereco.class);

                        endereco.setText(String.valueOf(userEndereco.getRua() + ", " + userEndereco.getNumero()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                String planoSaude = usuario.getPlanoDeSaude();

                DatabaseReference planoRef = ConfiguracaoFirebase.getFirebase().child("planodesaude").child(planoSaude);

                planoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        PlanoDeSaude userPlano = dataSnapshot.getValue(PlanoDeSaude.class);

                        planoDeSaude.setText(String.valueOf(userPlano.getNomePlano()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bt_agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataSelecionada != null) {
                    marcarAgendamento(idUsuarios, dataSelecionada, id, agendamento, diasDaSemana);

                } else {
                    Snackbar.make(findViewById(R.id.agendar_id), "Para agendar é preciso selecionar uma data", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        // month start at 1. Need to minus 1 to get javaMonth
        calendar.set(year, month - 1, day);

        return calendar.getTime();
    }

    private Date dataAtual(Date data) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(String.valueOf(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.setTime(date);

        return calendar.getTime();

    }

    private Date getDate(String strDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse(strDate);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.setTime(date);

        return calendar.getTime();
    }

    // Pega o dia da semana atual
    private int mesAtual() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
               /*  O valor do mês, vale respectivamente:
            Janeiro = 0
            Fevereiro = 1
            Março = 2
            Abrir = 3
            Maio = 4
            Junho = 5
            Julho = 6
            Agosto = 7
            Setembro = 8
            Outubro = 9
            Novembro = 10
            Dezembro = 11
     */

    }

    private int anoAtual() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        return ano;
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

    // Pega o último dia do MÊS atual
    private int ultimoDiaMes(Calendar c) {

        c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH) + 1;
        c.set(Calendar.MONTH, mes); //setando o mês para janeiro

        return c.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    private ArrayList<Date> datasDesativadas() {

        ArrayList<Date> Desativar = new ArrayList<Date>();

        Calendar calendario = Calendar.getInstance();

        int mes = calendario.get(Calendar.MONTH) + 1;
        int Dia = diaAtual();

        for (; mes < 13; mes++) {
            calendario.set(Calendar.MONTH, mes);
            int ultimoDiaMes = ultimoDiaMes(calendario);
            int anoAtual = anoAtual();
            if (Dia + 1 > ultimoDiaMes) {
                Dia = 1;
            }

            for (; ultimoDiaMes > Dia + 1; Dia++) {

                if (!AtivarDatas.contains(getDate(anoAtual, mes, Dia))) {
                    Desativar.add(getDate(anoAtual, mes, Dia));
                }
            }
            Dia = 1;
        }

        return Desativar;
    }

    private int diaAtual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);

    }

    private String diaDaSemana(Calendar cal) {
        return new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
    }

    private ArrayList percorrerAno(int mesAtual, int diaAtual, int anoAtual) {

        ArrayList<String> datas = new ArrayList<String>();

        int intervalo = 0;
        int i = 0;

        Calendar periodo1 = Calendar.getInstance();
        periodo1.set(anoAtual, mesAtual, diaAtual - 1);
        Calendar periodo2 = Calendar.getInstance();
        periodo2.set(anoAtual, 11, 31);

        intervalo = (periodo2.get(periodo2.DAY_OF_YEAR) - periodo1.get(periodo1.DAY_OF_YEAR));

        for (; i < intervalo; i++) {
            periodo1.add(periodo1.DATE, 1);

            datas.add(periodo1.get(periodo1.DATE) + "/" + (periodo1.get(periodo1.MONTH) + 1) + "/" + periodo1.get(periodo1.YEAR));
        }

        return datas;
    }

    private ArrayList<Date> setCalendario(ArrayList<DataSnapshot> datas) throws ParseException {

        ArrayList<Date> Ativas = new ArrayList<Date>();

        for (DataSnapshot data : datas) {

            String diaDaSemana = String.valueOf(data.getKey());
            int vagas = Integer.parseInt(String.valueOf(data.getValue()));

            ArrayList<String> dias = percorrerAno(mesAtual() - 1, diaAtual(), anoAtual());

            for (String days : dias) {

                if (diaDaSemana.equals("segunda") && vagas != 0) {

                    String diaAtual = diaSemana(days);
                    if (diaAtual.equals("segunda")) {
                        Ativas.add(getDate(days));
                    }
                } else if (diaDaSemana.equals("terca") && vagas != 0) {

                    String diaAtual = diaSemana(days);
                    if (diaAtual.equals("terca")) {
                        Ativas.add(getDate(days));
                    }
                } else if (diaDaSemana.equals("quarta") && vagas != 0) {

                    String diaAtual = diaSemana(days);
                    if (diaAtual.equals("quarta")) {
                        Ativas.add(getDate(days));
                    }
                } else if (diaDaSemana.equals("quinta") && vagas != 0) {

                    String diaAtual = diaSemana(days);
                    if (diaAtual.equals("quinta")) {
                        Ativas.add(getDate(days));
                    }
                } else if (diaDaSemana.equals("sexta") && vagas != 0) {

                    String diaAtual = diaSemana(days);
                    if (diaAtual.equals("sexta")) {
                        Ativas.add(getDate(days));
                    }
                } else if (diaDaSemana.equals("sabado") && vagas != 0) {

                    String diaAtual = diaSemana(days);
                    if (diaAtual.equals("sabado")) {
                        Ativas.add(getDate(days));
                    }
                } else if (diaDaSemana.equals("domingo") && vagas != 0) {

                    String diaAtual = diaSemana(days);
                    if (diaAtual.equals("domingo")) {
                        Ativas.add(getDate(days));
                    }
                }
            }
        }
        return Ativas;
    }

    private Map<Date, Drawable> colorirDatasAtivas(ArrayList<Date> arrayList) {

        Map<Date, Drawable> hm = new HashMap<>();

        for (Date data : arrayList) {
            hm.put(data, getDrawable(R.color.Ativas));
        }
        return hm;
    }

    private void marcarAgendamento(final String idUsuarios,
                                   final String dataSelecionada,
                                   final String id,
                                   final Agendamento agendamento,
                                   final ArrayList<DataSnapshot> datas) { //método que valida o email e senha e exclui a conta do usuário

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_confirmar_agendamento, null);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DatabaseReference bancoDeDados = ConfiguracaoFirebase.getFirebase();

                Calendar calendario = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String dataAtual = format.format(calendario.getTime());

                agendamento.setDataConsulta(dataSelecionada);
                agendamento.setDataAtual(dataAtual);
                agendamento.setId_Cliente(String.valueOf(id));
                agendamento.setId_Usuario(String.valueOf(idUsuarios));

                bancoDeDados.child("agendamento")
                        .push()
                        .setValue(agendamento).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        for (DataSnapshot data : datas) {

                            String diaSemanaAgendado = new String();

                            try {
                                diaSemanaAgendado = diaSemana(dataSelecionada);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String diaDaSemanaDoBanco = String.valueOf(data.getKey());
                            int vagas = Integer.parseInt(String.valueOf(data.getValue()));

                            if (diaSemanaAgendado.equals(diaDaSemanaDoBanco)) {
                                vagas = vagas - 1;
                                firebase.child(diaDaSemanaDoBanco).setValue(vagas);
                            }


                        }
                        Toast.makeText(AgendarActivity.this, "Agendamento marcado com sucesso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AgendarActivity.this, "Cancelar", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}