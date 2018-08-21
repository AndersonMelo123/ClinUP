package br.com.projetofragmeto.clinup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.model.Profissional;

public class AdapterPersonalizadoProfissionais extends BaseAdapter {

    private final List<Profissional> profissionals;
    private final Activity act;

    public AdapterPersonalizadoProfissionais(List<Profissional> profissional, Activity act) {
        this.profissionals = profissional;
        this.act = act;
    }

    @Override
    public int getCount() {
        return profissionals.size();
    }

    @Override
    public Object getItem(int position) {
        return profissionals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_profissionais_personalizada, parent, false);

        Profissional profissional = profissionals.get(position);

        TextView nome = view.findViewById(R.id.lista_profissionais_personalizada_nome);
        TextView descricao = view.findViewById(R.id.lista_profissionais_personalizada_descricao);
        ImageView imagem = view.findViewById(R.id.lista_profissionais_personalizada_imagem);

        nome.setText(profissional.getNome());
        descricao.setText(profissional.getEspecialidade());

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
