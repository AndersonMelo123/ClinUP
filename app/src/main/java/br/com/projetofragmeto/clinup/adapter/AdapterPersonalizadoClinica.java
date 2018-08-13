package br.com.projetofragmeto.clinup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.model.Clinica;

public class AdapterPersonalizadoClinica extends BaseAdapter {

    private final List<Clinica> clinicas;
    private final Activity act;

    public AdapterPersonalizadoClinica(List<Clinica> clinicas, Activity act) {
        this.clinicas = clinicas;
        this.act = act;
    }

    @Override
    public int getCount() {
        return clinicas.size();
    }

    @Override
    public Object getItem(int position) {
        return clinicas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_clinica_personalizada, parent, false);

        Clinica clinica = clinicas.get(position);

        TextView nome = view.findViewById(R.id.lista_clinica_personalizada_nome);
        TextView descricao = view.findViewById(R.id.lista_clinica_personalizada_descricao);
        ImageView imagem = view.findViewById(R.id.lista_clinica_personalizada_imagem);

        nome.setText(clinica.getNome());
        descricao.setText(clinica.getTelefone());

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
