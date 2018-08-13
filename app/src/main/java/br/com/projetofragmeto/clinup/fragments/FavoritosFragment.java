package br.com.projetofragmeto.clinup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.adapter.AdapterPersonalizadoFavoritos;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Favoritos;

public class FavoritosFragment extends Fragment {

    private ListView listView;
    public ArrayList favoritos;//retorna o nome dos profissionais da consulta para exibir na listview
    private DatabaseReference firebase;
    public ArrayList<Favoritos> favObjetos = new ArrayList<Favoritos>();

    public FavoritosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        favoritos = new ArrayList();
        listView = view.findViewById(R.id.lv_favoritos);


        final AdapterPersonalizadoFavoritos adapterPersonalizado = new AdapterPersonalizadoFavoritos(favObjetos, getActivity());

        listView.setAdapter(adapterPersonalizado);

        firebase = ConfiguracaoFirebase.getFirebase().child("favoritos");

        favoritos.clear();//limpa o array profissionais
        favObjetos.clear();//limpa o array profObjetos


        Preferencias preferencesUser = new Preferencias(getContext());
        final String idUsuarios = preferencesUser.getIdentificador();

        //metodo q faz a listagem
        firebase.addValueEventListener(new ValueEventListener() {//faz a consulta no banco
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Favoritos fav = dados.getValue(Favoritos.class);//retorna cada objeto da consulta em a

                        String idUsuarioFavoritos = fav.getId_Usuario();
                        if (idUsuarioFavoritos.equals(idUsuarios)) {

                            String info = fav.toString();
                            favObjetos.add(fav);//adiciona o profissional p em profObjetos
                            favoritos.add(info);//adiciona o nome do profissional p em profissionais

                        }
                    }

                    adapterPersonalizado.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

/*
        //pega o clic no list view e passa pra outra activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), CancelarAgendamentos.class);

                intent.putExtra("ID", agendObjetos.get(i).getId());
                intent.putExtra("nome", agendObjetos.get(i).getNomeUsuario());
                intent.putExtra("dataAtual", agendObjetos.get(i).getDataAtual());
                intent.putExtra("dataConsulta", agendObjetos.get(i).getDataConsulta());
                intent.putExtra("plano", agendObjetos.get(i).getId_Plano());
                intent.putExtra("cliente", agendObjetos.get(i).getId_Cliente());


                intent.putExtra("classe", Agendamento.class);
                startActivity(intent);
            }
        });
*/


        return view;
    }
}
