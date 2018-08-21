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

import java.io.Serializable;
import java.util.ArrayList;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.activity.PerfilCliente;
import br.com.projetofragmeto.clinup.adapter.AdapterPersonalizadoLaboratorio;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.LaboratorioDB;
import br.com.projetofragmeto.clinup.model.Laboratorio;


public class BuscarLaboratorioFragment extends Fragment implements Serializable {

    private ListView listView;
    private ArrayAdapter adapter;

    private DatabaseReference firebase;
    private LaboratorioDB laboratorioDB;

    private EditText texto;
    private Button botaoBusca;
    private Button botaoFiltro;
    private TextView textView;

    private String[] filtro = {"Todos", "Nome"};
    private String filtragem = filtro[0];

    private ArrayList laboratorios;
    private ArrayList<Laboratorio> labObjetos = new ArrayList<Laboratorio>();//retorna todos as Clinicas da consulta no banco


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

        final AdapterPersonalizadoLaboratorio adapterPersonalizado = new AdapterPersonalizadoLaboratorio(labObjetos, getActivity());

        /*adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                laboratorios //array list contendo todos os contados
        );*/
        listView.setAdapter(adapterPersonalizado); //seta o adaptados

        botaoBusca = view.findViewById(R.id.blf_bt_buscar);
        botaoFiltro = view.findViewById(R.id.blf_bp_filtro);
        texto = view.findViewById(R.id.blf_et_buscar);
        textView = view.findViewById(R.id.blf_et);

        // método que pega o click da listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("i", (String) profissionais.get(i));
                //Log.i("i",profObjetos.get(i).getEspecialidade());

                Intent intent = new Intent(getActivity(), PerfilCliente.class);

                intent.putExtra("email", labObjetos.get(i).getId());
                intent.putExtra("nome", labObjetos.get(i).getNome());
                intent.putExtra("id", labObjetos.get(i).getCnpj());
                intent.putExtra("telefone", labObjetos.get(i).getTelefone());

                intent.putExtra("horaAbrir",labObjetos.get(i).getHoraAbrir());
                intent.putExtra("horaFechar",labObjetos.get(i).getHoraFechar());

                intent.putExtra("cliente", "laboratorios");
                intent.putExtra("classe", Laboratorio.class);
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
                        labObjetos.clear();
                        laboratorios.clear();
                        firebase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Laboratorio l = dados.getValue(Laboratorio.class);
                                        String nome = l.toString();
                                        laboratorios.add(nome);
                                        labObjetos.add(l);
                                    }

                                    adapterPersonalizado.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;
                    case ("Nome"):
                        labObjetos.clear();
                        laboratorios.clear();
                        firebase.orderByChild("nome").equalTo(nome).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Laboratorio l = dados.getValue(Laboratorio.class);
                                        String nome = l.toString();
                                        laboratorios.add(nome);
                                        labObjetos.add(l);
                                    }

                                    adapterPersonalizado.notifyDataSetChanged();
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
