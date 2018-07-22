package br.com.projetofragmeto.clinup.fragments;

import android.content.Context;
import android.net.Uri;
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
import br.com.projetofragmeto.clinup.database.LaboratorioDB;


public class BuscarLaboratorioFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList laboratorios;
    private DatabaseReference firebase;
    private LaboratorioDB laboratorioDB;

    public BuscarLaboratorioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_laboratorio, container, false);
        firebase = ConfiguracaoFirebase.getFirebase().child("laboratorios");

        laboratorios = new ArrayList();
        laboratorioDB = new LaboratorioDB();



        listView = view.findViewById(R.id.lv_laboratorio);

        adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                laboratorios //array list contendo todos os contados
        );
        listView.setAdapter(adapter); //seta o adaptados

        // listner para recuperar contatos

        /*firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    laboratorios = laboratorioDB.buscarDados(dataSnapshot,laboratorios);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        String[] filtro = {"default","nome"};
        String filtragem = filtro[0];
        final String nome = "LabClin";

        switch (filtragem){
            case("default"):
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null){
                            laboratorios = laboratorioDB.buscarDados(dataSnapshot,laboratorios);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case("nome"):

                firebase.orderByChild("nome").equalTo(nome).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        laboratorios = laboratorioDB.filtroNome(nome,dataSnapshot,laboratorios);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;

        }
        return view;
    }

}
