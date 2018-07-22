package br.com.projetofragmeto.clinup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.ClinicaDB;

public class BuscarClinicaFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList clinicas;
    private DatabaseReference firebase;
    private ClinicaDB clinicaDB;

    public BuscarClinicaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_clinica, container, false);

        clinicas = new ArrayList();
        clinicaDB = new ClinicaDB();
        firebase = ConfiguracaoFirebase.getFirebase().child("clinica");


        listView = view.findViewById(R.id.lv_clinica);

        adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                clinicas //array list contendo todos os contados
        );
        listView.setAdapter(adapter); //seta o adaptados

        // listner para recuperar contatos

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    clinicas = clinicaDB.buscarDados(dataSnapshot,clinicas);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
