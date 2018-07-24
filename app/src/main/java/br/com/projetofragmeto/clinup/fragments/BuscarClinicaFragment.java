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
import br.com.projetofragmeto.clinup.activity.AgendarActivity;
import br.com.projetofragmeto.clinup.activity.PerfilCliente;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.ClinicaDB;
import br.com.projetofragmeto.clinup.model.Clinica;

public class BuscarClinicaFragment extends Fragment implements Serializable{
    private ListView listView;
    private ArrayAdapter adapter;

    private DatabaseReference firebase;
    private ClinicaDB clinicaDB;

    private ArrayList clinicas;
    private ArrayList<Clinica> clinObjetos = new ArrayList<Clinica>();//retorna todos as Clinicas da consulta no banco

    private EditText texto;
    private Button botaoBusca;
    private Button botaoFiltro;
    private TextView textView;

    String[] filtro = {"Todos","Nome"};
    String filtragem = filtro[0];

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

        botaoBusca = view.findViewById(R.id.bcf_bt_buscar);
        botaoFiltro = view.findViewById(R.id.bcf_bp_filtro);
        texto = view.findViewById(R.id.bcf_et_buscar);
        textView = view.findViewById(R.id.bcf_et);


        listView = view.findViewById(R.id.lv_clinica);

        adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                clinicas //array list contendo todos os contados
        );
        listView.setAdapter(adapter); //seta o adaptados

        // método que pega o click da listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("i", (String) profissionais.get(i));
                //Log.i("i",profObjetos.get(i).getEspecialidade());

                Intent intent = new Intent(getActivity(), AgendarActivity.class);
                intent.putExtra("nome",clinObjetos.get(i).getNome());
                intent.putExtra("email", clinObjetos.get(i).getEmail() );
                intent.putExtra("id", clinObjetos.get(i).getCnpj() );
                intent.putExtra("endereco", clinObjetos.get(i).getEndereco() );
                intent.putExtra("telefone", clinObjetos.get(i).getTelefone() );
                intent.putExtra("cnpj", clinObjetos.get(i).getCnpj() );

                intent.putExtra("cliente","clinica");
                intent.putExtra("classe",Clinica.class);
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
                switch (filtragem){
                    case("Todos"):

                        clinicas.clear();
                        clinObjetos.clear();
                        //final String nome = texto.getText().toString();//pega nome do campo de texto
                        firebase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if(dataSnapshot.getValue() != null){
                                    //clinicas = clinicaDB.buscarDados(dataSnapshot,clinicas);

                                    for(DataSnapshot dados: dataSnapshot.getChildren()){
                                        Clinica c = dados.getValue(Clinica.class);
                                        String nome = c.getNome();
                                        clinObjetos.add(c);
                                        clinicas.add(nome);
                                    }


                                    adapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;
                    case("Nome"):
                        clinicas.clear();
                        clinObjetos.clear();

                        firebase.orderByChild("nome").equalTo(nome).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //clinicas = clinicaDB.filtroNome(nome,dataSnapshot,clinicas);

                                if(dataSnapshot.getValue() != null) {
                                    //clinicas = clinicaDB.buscarDados(dataSnapshot,clinicas);

                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Clinica c = dados.getValue(Clinica.class);
                                        String nome = c.getNome();
                                        clinObjetos.add(c);
                                        clinicas.add(nome);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;
            /*
                case("exame"):
                firebase.child("exames").orderByChild("nome").equalTo(exame).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        clinicas = clinicaDB.filtroExame(exame,dataSnapshot,clinicas);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });*/
                }

            }
        });
        //final String nome = "CEM";
        //final String exame = "sangue";

        return view;
    }

}
