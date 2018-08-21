package br.com.projetofragmeto.clinup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.model.Favoritos;

public class AdapterPersonalizadoFavoritos extends BaseAdapter {

    private final List<Favoritos> favoritos;
    private final Activity act;

    public AdapterPersonalizadoFavoritos(List<Favoritos> favoritos, Activity act) {
        this.favoritos = favoritos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return favoritos.size();
    }

    @Override
    public Object getItem(int position) {
        return favoritos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_favoritos_personalizada, parent, false);

        Favoritos favoritos = this.favoritos.get(position);

        TextView nome = view.findViewById(R.id.lista_favoritos_personalizada_nome);
        TextView descricao = view.findViewById(R.id.lista_favoritos_personalizada_descricao);
        ImageView imagem = view.findViewById(R.id.lista_favoritos_personalizada_imagem);

        nome.setText(favoritos.getNomeCliente());
        descricao.setText(favoritos.getTipo());

        return view;
    }
}
