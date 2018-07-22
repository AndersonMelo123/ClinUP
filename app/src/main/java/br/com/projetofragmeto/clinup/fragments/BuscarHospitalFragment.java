

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
import br.com.projetofragmeto.clinup.database.HospitalDB;

public class BuscarHospitalFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList hospitais;
    private DatabaseReference firebase;
    private HospitalDB hospitalDB;


    public BuscarHospitalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_hospital, container, false);

        hospitais = new ArrayList();
        hospitalDB = new HospitalDB();
        firebase = ConfiguracaoFirebase.getFirebase().child("hospitais");


        listView = view.findViewById(R.id.lv_hospital);

        adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                hospitais //array list contendo todos os contados
        );
        listView.setAdapter(adapter); //seta o adaptados

        // listner para recuperar contatos

        /*firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    hospitais = hospitalDB.buscarDados(dataSnapshot,hospitais);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        String[] filtro = {"default","nome","exame"};
        String filtragem = filtro[0];
        final String nome = "Dom Moura";

        switch (filtragem){
            case("default"):
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null){
                            hospitais = hospitalDB.buscarDados(dataSnapshot,hospitais);
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
                        hospitais = hospitalDB.filtroNome(nome,dataSnapshot,hospitais);
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
