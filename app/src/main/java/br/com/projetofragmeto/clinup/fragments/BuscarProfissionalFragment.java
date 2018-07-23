package br.com.projetofragmeto.clinup.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.activity.AgendarActivity;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.ProfissionalDB;
import br.com.projetofragmeto.clinup.helper.Base64Custom;
import br.com.projetofragmeto.clinup.model.Profissional;

public class BuscarProfissionalFragment extends Fragment implements Serializable {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Profissional> profissionais;
    private DatabaseReference firebase;
    ProfissionalDB profissionalDB;

    public BuscarProfissionalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_profissional, container, false);

        profissionais = new ArrayList();
        profissionalDB = new ProfissionalDB();


        listView = view.findViewById(R.id.lv_profissional);


        adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                profissionais //array list contendo todos os contados
        );
        listView.setAdapter(adapter); //seta o adaptados

        firebase = ConfiguracaoFirebase.getFirebase().child("profissionais");


        // adiciona um profissional no banco
        /*Profissional profissional = new Profissional("Maria","Rua 2", "Pediatria","UFRPE", "002");
        String id = "006";
        firebase.child(id).setValue(profissional);*/
        //Profissional profissional = new Profissional("Ademário", "005", "Pneumologista", "UPE", "006");

        //profissionalDB.adicionarDados(profissional,"009");

        // listner para recuperar contatos

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    profissionais = profissionalDB.buscarDados(dataSnapshot,profissionais);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), AgendarActivity.class);

                // Recuperar dados a serem passados
                Profissional profissional = profissionais.get( position );

                // Enviando dados para conversa activity

                //String email = Base64Custom.decodificarBase64( conversa.getIdUsuario() );

                intent.putExtra("email", profissional.getId() );
                intent.putExtra("nome", profissional.getNome() );
                intent.putExtra("nome", profissional.getId() );

                startActivity(intent);

            }
        });

        return view;
    }


}
