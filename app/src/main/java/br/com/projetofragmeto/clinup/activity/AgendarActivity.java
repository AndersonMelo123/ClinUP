package br.com.projetofragmeto.clinup.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.roomorama.caldroid.CaldroidFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.projetofragmeto.clinup.R;

public class AgendarActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar);

        Calendar calendar = Calendar.getInstance();

        CaldroidFragment caldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));

        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();


        ArrayList<Date> datasDesativadas = new ArrayList<Date>();
        ArrayList<Date> datasAtivadas = new ArrayList<Date>();

        try {
            datasAtivadas.add(getDate("31/07/2018"));
            datasAtivadas.add(getDate("28/07/2018"));
            datasAtivadas.add(getDate("25/07/2018"));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {
            String anoAtual = String.valueOf(anoAtual());
            String mesAtual = String.valueOf(mesAtual());
            if (mesAtual()<10){
                mesAtual = "0"+mesAtual;
            }
            int Dia;
            int ultimoDiaMes = ultimoDiaMes();
            Log.i("MesAtual",mesAtual);
            Log.i("AnoAtual",anoAtual);

            for(Dia = 1; Dia == ultimoDiaMes + 1; Dia++){
                String data = String.valueOf(Dia)+"/"+mesAtual+"/"+anoAtual;

                Log.i("DataAtual", data);
                if (datasAtivadas.contains(data)){
                    Log.i("DataAtivda", String.valueOf(Dia));
                }else {
                    datasDesativadas.add(getDate(data));

                    Log.i("Dias", String.valueOf(Dia));
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }



        caldroidFragment.setMinDate(calendar.getTime()); //Setando a data minima

        int i = ultimoDiaMes();
        Log.i("UltimoDIa", String.valueOf(i));

        // Customize
        caldroidFragment.setDisableDates(datasDesativadas);
        // Refresh
        caldroidFragment.refreshView();


    }

    private Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        // month start at 1. Need to minus 1 to get javaMonth
        calendar.set(year, month - 1, day);

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
        return c.get(Calendar.MONTH)+1;
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

    private int anoAtual(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        return ano;
    }

    // Pega o dia da semana atual
    private int diaSemana(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date ontem = sdf.parse(strDate);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(ontem);
        int diaDaSemana = gc.get(GregorianCalendar.DAY_OF_WEEK);

        return diaDaSemana;

        /* O resultado do valor, vale respectivamente:
            1 = domingo
            2 = segunda
            3 = terça
            4 = quarta
            5 = quinta
            6 = sexta
            7 = sabado
        */

    }

    // Pega o último dia do MÊS atual
    private int ultimoDiaMes() {
        Calendar c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH) + 1;
        c.set(Calendar.MONTH, mes); //setando o mês para janeiro

        int ultimoDia = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        return ultimoDia;

    }


}

