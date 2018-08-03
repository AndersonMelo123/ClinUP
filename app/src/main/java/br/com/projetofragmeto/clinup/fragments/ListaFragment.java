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

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.activity.Agendamentos;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Agendamento;


public class ListaFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList agendamentos;//retorna o nome dos profissionais da consulta para exibir na listview
    private DatabaseReference firebase;
    //private ProfissionalDB profissionalDB;
    private ArrayList<Agendamento> agendObjetos = new ArrayList<Agendamento>();
    private Agendamento agendamentoAtual;
    private String idKey;

    public ListaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        agendamentos = new ArrayList();
        listView = view.findViewById(R.id.lv_agendamentos);


        adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                agendamentos //array list contendo todos os contados
        );

        listView.setAdapter(adapter);

        firebase = ConfiguracaoFirebase.getFirebase().child("agendamento");

        agendamentos.clear();//limpa o array profissionais
        agendObjetos.clear();//limpa o array profObjetos


        //metodo q faz a listagem
        firebase.addValueEventListener(new ValueEventListener() {//faz a consulta no banco
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    //profissionais.clear();
                    for(DataSnapshot dados: dataSnapshot.getChildren()){
                        Agendamento a = dados.getValue(Agendamento.class);//retorna cada objeto da consulta em p
                        String nome = a.getNomeUsuario();
                        //Log.i("NOME",nome);
                        agendObjetos.add(a);//adiciona o profissional p em profObjetos
                        agendamentos.add(nome);//adiciona o nome do profissional p em profissionais
                    }

                    //profissionais = profissionalDB.buscarDados(dataSnapshot,profissionais);
                    adapter.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Agendamento age = (Agendamento)adapterView.getAdapter().getItem(i);

                Intent intent = new Intent(getActivity(),Agendamentos.class);

                intent.putExtra("ID", agendObjetos.get(i).getId());
                intent.putExtra("nome", agendObjetos.get(i).getNomeUsuario());
                intent.putExtra("dataAtual",  agendObjetos.get(i).getDataAtual());
                intent.putExtra("dataConsulta", agendObjetos.get(i).getDataConsulta() );
                intent.putExtra("plano", agendObjetos.get(i).getId_Plano() );

                intent.putExtra("classe",Agendamento.class);
                startActivity(intent);
            }
        });


        return view;
    }


}
