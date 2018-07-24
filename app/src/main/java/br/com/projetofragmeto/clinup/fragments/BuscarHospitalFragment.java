package br.com.projetofragmeto.clinup.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.activity.AgendarActivity;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.HospitalDB;
import br.com.projetofragmeto.clinup.model.Hospital;

public class BuscarHospitalFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;

    private DatabaseReference firebase;
    private HospitalDB hospitalDB;

    private EditText texto;
    private Button botaoBusca;
    private Button botaoFiltro;
    private TextView textView;

    private String[] filtro = {"Todos", "Nome"};
    private String filtragem = filtro[0];

    private ArrayList hospitais;
    private ArrayList<Hospital> hospObjetos = new ArrayList<Hospital>();//retorna todos as Clinicas da consulta no banco

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

        botaoBusca = view.findViewById(R.id.bhf_bt_buscar);
        botaoFiltro = view.findViewById(R.id.bhf_bp_filtro);
        texto = view.findViewById(R.id.bhf_et_buscar);
        textView = view.findViewById(R.id.bhf_et);

        final String nome = "Dom Moura";


        // método que pega o click da listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("i", (String) profissionais.get(i));
                //Log.i("i",profObjetos.get(i).getEspecialidade());

                Intent intent = new Intent(getActivity(), AgendarActivity.class);
                intent.putExtra("nome", hospObjetos.get(i).getNome());
                intent.putExtra("email", hospObjetos.get(i).getEmail());
                intent.putExtra("nome", hospObjetos.get(i).getNome());
                intent.putExtra("id", hospObjetos.get(i).getCnpj());

                intent.putExtra("cliente", "hospitais");
                intent.putExtra("classe", Hospital.class);
                startActivity(intent);
            }
        });

        botaoFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("Filtro");
                mBuilder.setSingleChoiceItems(filtro, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filtragem = filtro[i];
                        textView.setText(filtragem);
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //Mostra alert Dialog
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        botaoBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nome = texto.getText().toString();//pega nome do campo de texto
                switch (filtragem) {
                    case ("Todos"):
                        hospitais.clear();
                        hospObjetos.clear();

                        firebase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    //hospitais = hospitalDB.buscarDados(dataSnapshot,hospitais);
                                    //hospitais.clear();
                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Hospital h = dados.getValue(Hospital.class);
                                        String nome = h.getNome();
                                        hospitais.add(nome);
                                        hospObjetos.add(h);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;
                    case ("Nome"):
                        hospitais.clear();
                        hospObjetos.clear();
                        firebase.orderByChild("nome").equalTo(nome).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    //hospitais = hospitalDB.buscarDados(dataSnapshot,hospitais);
                                    //hospitais.clear();
                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Hospital h = dados.getValue(Hospital.class);
                                        String nome = h.getNome();
                                        hospitais.add(nome);
                                        hospObjetos.add(h);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;

                }
            }
        });


        return view;
    }

}
