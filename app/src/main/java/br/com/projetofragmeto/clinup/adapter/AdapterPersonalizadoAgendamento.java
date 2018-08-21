package br.com.projetofragmeto.clinup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.model.Agendamento;

public class AdapterPersonalizadoAgendamento extends BaseAdapter {

    private final List<Agendamento> agendamentos;
    private final Activity act;

    public AdapterPersonalizadoAgendamento(List<Agendamento> agendamentos, Activity act) {
        this.agendamentos = agendamentos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return agendamentos.size();
    }

    @Override
    public Object getItem(int position) {
        return agendamentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_agendamento_personalizada, parent, false);

        Agendamento agendamento = agendamentos.get(position);

        TextView nome = view.findViewById(R.id.lista_agendamento_personalizada_nome);
        TextView descricao = view.findViewById(R.id.lista_agendamento_personalizada_descricao);
        ImageView imagem = view.findViewById(R.id.lista_agendamento_personalizada_imagem);

        nome.setText(agendamento.getNome_Cliente());
        descricao.setText(agendamento.getDataConsulta());

        //Categoria categoria = curso.getCategoria();

        /*if (categoria.equals(Categoria.JAVA)) {
            imagem.setImageResource(R.drawable.java);
        } else if (categoria.equals(Categoria.ANDROID)) {
            imagem.setImageResource(R.drawable.android);
        } else if (categoria.equals(Categoria.HTML)) {
            imagem.setImageResource(R.drawable.html);
        }*/

        return view;
    }
}
