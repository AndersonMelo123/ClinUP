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
import br.com.projetofragmeto.clinup.activity.CancelarAgendamentos;
import br.com.projetofragmeto.clinup.adapter.AdapterPersonalizadoAgendamento;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Agendamento;


public class ListaFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    public ArrayList agendamentos;//retorna o nome dos profissionais da consulta para exibir na listview
    private DatabaseReference firebase;
    public ArrayList<Agendamento> agendObjetos = new ArrayList<Agendamento>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        agendamentos = new ArrayList();
        listView = view.findViewById(R.id.lv_agendamentos);


        final AdapterPersonalizadoAgendamento adapterPersonalizadoAgendamento = new AdapterPersonalizadoAgendamento(agendObjetos, getActivity());

        /*adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                agendamentos //array list contendo todos os contados
        );*/

        listView.setAdapter(adapterPersonalizadoAgendamento);

        firebase = ConfiguracaoFirebase.getFirebase().child("agendamento");

        agendamentos.clear();//limpa o array profissionais
        agendObjetos.clear();//limpa o array profObjetos


        Preferencias preferencesUser = new Preferencias(getContext());
        final String idUsuarios = preferencesUser.getIdentificador();

        //metodo q faz a listagem
        firebase.addValueEventListener(new ValueEventListener() {//faz a consulta no banco
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Agendamento ag = dados.getValue(Agendamento.class);//retorna cada objeto da consulta em a

                        String idUsuarioAgendamento = ag.getIdUsuario();
                        if (idUsuarioAgendamento.equals(idUsuarios)) {

                            String info = ag.toString();
                            agendObjetos.add(ag);//adiciona o profissional p em profObjetos
                            agendamentos.add(info);//adiciona o nome do profissional p em profissionais

                        }
                    }

                    adapterPersonalizadoAgendamento.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //pega o clic no list view e passa pra outra activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), CancelarAgendamentos.class);

                intent.putExtra("ID", agendObjetos.get(i).getId());
                intent.putExtra("nome", agendObjetos.get(i).getNomeCliente());
                intent.putExtra("dataAtual", agendObjetos.get(i).getDataAtual());
                intent.putExtra("dataConsulta", agendObjetos.get(i).getDataConsulta());
                intent.putExtra("plano", agendObjetos.get(i).getIdPlano());
                intent.putExtra("cliente", agendObjetos.get(i).getIdCliente());

                intent.putExtra("classe", Agendamento.class);
                startActivity(intent);
            }
        });


        return view;
    }

}