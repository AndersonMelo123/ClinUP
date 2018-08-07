package br.com.projetofragmeto.clinup.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.database.ProfissionalDB;
import br.com.projetofragmeto.clinup.model.Profissional;

public class BuscarProfissionalFragment extends Fragment implements Serializable {

    private ListView listView;
    private EditText texto;
    private TextView textView;
    private Button botaoBuscar, botaoFiltrar;
    private ArrayAdapter adapter;
    private ArrayList profissionais;//retorna o nome dos profissionais da consulta para exibir na listview
    private DatabaseReference firebase;
    private ProfissionalDB profissionalDB;
    private ArrayList<Profissional> profObjetos = new ArrayList<Profissional>();//retorna todos os profissionais da consulta no banco


    private String[] filtro = {"Todos", "Nome", "Especialidade"};
    String filtragem = filtro[0];

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
        texto = view.findViewById(R.id.bpf_et_buscar);
        botaoBuscar = view.findViewById(R.id.bpf_bt_buscar);
        botaoFiltrar = view.findViewById(R.id.bpf_bp_filtro);
        textView = view.findViewById(R.id.textView);

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

        // método que pega o click da listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("i", (String) profissionais.get(i));
                //Log.i("i",profObjetos.get(i).getEspecialidade());

                //Intent intent = new Intent(getActivity(), AgendarActivity.class);
                Intent intent = new Intent(getActivity(), PerfilCliente.class);

                intent.putExtra("email", profObjetos.get(i).getId());
                intent.putExtra("nome", profObjetos.get(i).getNome());
                intent.putExtra("id", profObjetos.get(i).getId());
                intent.putExtra("telefone", profObjetos.get(i).getTelefone());
                intent.putExtra("endereco", profObjetos.get(i).getEndereco());
                intent.putExtra("especialidade", profObjetos.get(i).getEspecialidade());
                intent.putExtra("formacao", profObjetos.get(i).getFormacao());
                intent.putExtra("Num_registro", profObjetos.get(i).getNum_registro());

                intent.putExtra("horaAbrir",profObjetos.get(i).getHoraAbrir());
                intent.putExtra("horaFechar",profObjetos.get(i).getHoraFechar());

                intent.putExtra("cliente", "profissionais");
                intent.putExtra("classe", Profissional.class);
                startActivity(intent);
            }
        });

        botaoFiltrar.setOnClickListener(new View.OnClickListener() {
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

        botaoBuscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                profissionais.clear();
                final String nome = texto.getText().toString();//pega nome do campo de texto
                switch (filtragem) {
                    case ("Todos")://se o filtro selecionado for Todos
                        profissionais.clear();//limpa o array profissionais
                        profObjetos.clear();//limpa o array profObjetos

                        firebase.addValueEventListener(new ValueEventListener() {//faz a consulta no banco
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    //profissionais.clear();
                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Profissional p = dados.getValue(Profissional.class);//retorna cada objeto da consulta em p
                                        String nome = p.getNome();
                                        Log.i("NOME", nome);
                                        profObjetos.add(p);//adiciona o profissional p em profObjetos
                                        profissionais.add(nome);//adiciona o nome do profissional p em profissionais
                                    }
                                    //profissionais = profissionalDB.buscarDados(dataSnapshot,profissionais);
                                    adapter.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;

                    case ("Nome"):
                        profissionais.clear();
                        profObjetos.clear();
                        firebase.orderByChild("nome").equalTo(nome).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    profissionais.clear();
                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Profissional p = dados.getValue(Profissional.class);
                                        String nome = p.getNome();
                                        Log.i("NOME", nome);
                                        profObjetos.add(p);
                                        profissionais.add(nome);
                                    }
                                    //profissionais = profissionalDB.buscarDados(dataSnapshot,profissionais);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;
                    case ("Especialidade"):
                        profissionais.clear();
                        profObjetos.clear();
                        firebase.orderByChild("especialidade").equalTo(nome).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    profissionais.clear();
                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Profissional p = dados.getValue(Profissional.class);
                                        String nome = p.getNome();
                                        Log.i("NOME", nome);
                                        profObjetos.add(p);
                                        profissionais.add(nome);
                                    }
                                    //profissionais = profissionalDB.buscarDados(dataSnapshot,profissionais);
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
