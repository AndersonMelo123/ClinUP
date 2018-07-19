package br.com.projetofragmeto.clinup.activity;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import br.com.projetofragmeto.clinup.R;

public class AgendarActivity extends FragmentActivity {

    private ArrayList<Date> DesativarDatas = new ArrayList<Date>();
    private ArrayList<Date> AtivarDatas = new ArrayList<Date>();

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

        // Colocando o background nas datas ativas
        // Create a hash map
        Map<Date, Drawable> hm = new HashMap<>();


        try { //Pegar as datas válidas para agendar
            // Put nas datas ATIVAS
            hm.put(getDate("31/07/2018"), getDrawable(R.color.Ativas));
            hm.put(getDate("28/07/2018"), getDrawable(R.color.Ativas));
            hm.put(getDate("25/07/2018"), getDrawable(R.color.Ativas));

            AtivarDatas.add(getDate("31/07/2018"));
            AtivarDatas.add(getDate("28/07/2018"));
            AtivarDatas.add(getDate("25/07/2018"));


        } catch (ParseException e) {
            e.printStackTrace();
        }


        int Dia;

        for (Dia = 1; Dia < ultimoDiaMes() + 1; Dia++) {

            Date data = getDate(anoAtual(), mesAtual(), Dia);

            if (!AtivarDatas.contains(data)) {

                DesativarDatas.add(data);
            }

        }
        caldroidFragment.setBackgroundDrawableForDates(hm);

        caldroidFragment.setMinDate(calendar.getTime()); //Setando a data minima

        caldroidFragment.setDisableDates(DesativarDatas);

        caldroidFragment.refreshView(); // Refresh


        CaldroidListener calendario = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String dataSelecionada = format.format(date);
            }

        };

        caldroidFragment.setCaldroidListener(calendario);

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

