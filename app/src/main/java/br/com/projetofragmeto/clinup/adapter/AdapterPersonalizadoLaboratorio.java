package br.com.projetofragmeto.clinup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.model.Laboratorio;

public class AdapterPersonalizadoLaboratorio extends BaseAdapter {

    private final List<Laboratorio> laboratorios;
    private final Activity act;

    public AdapterPersonalizadoLaboratorio(List<Laboratorio> laboratorios, Activity act) {
        this.laboratorios = laboratorios;
        this.act = act;
    }

    @Override
    public int getCount() {
        return laboratorios.size();
    }

    @Override
    public Object getItem(int position) {
        return laboratorios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_laboratorio_personalizada, parent, false);

        Laboratorio laboratorio = laboratorios.get(position);

        TextView nome = view.findViewById(R.id.lista_laboratorio_personalizada_nome);
        TextView descricao = view.findViewById(R.id.lista_laboratorio_personalizada_descricao);
        ImageView imagem = view.findViewById(R.id.lista_laboratorio_personalizada_imagem);

        nome.setText(laboratorio.getNome());
        descricao.setText(laboratorio.getTelefone());

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
