package br.com.projetofragmeto.clinup.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.model.Hospital;

public class AdapterPersonalizadoHospital extends BaseAdapter {

    private final List<Hospital> hospitals;
    private final Activity act;

    public AdapterPersonalizadoHospital(List<Hospital> hospitals, Activity act) {
        this.hospitals = hospitals;
        this.act = act;
    }

    @Override
    public int getCount() {
        return hospitals.size();
    }

    @Override
    public Object getItem(int position) {
        return hospitals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.lista_hospital_personalizada, parent, false);

        Hospital hospital = hospitals.get(position);

        TextView nome = view.findViewById(R.id.lista_hospital_personalizada_nome);
        TextView descricao = view.findViewById(R.id.lista_hospital_personalizada_descricao);
        ImageView imagem = view.findViewById(R.id.lista_hospital_personalizada_imagem);

        nome.setText(hospital.getNome());
        descricao.setText(hospital.getTelefone());

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
