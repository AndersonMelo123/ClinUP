package br.com.projetofragmeto.clinup.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import br.com.projetofragmeto.clinup.model.Profissional;

public class BuscarProfissionalFragment extends Fragment implements Serializable {

    private TextView textViewFiltro;
    private ArrayAdapter adapter;
    private ArrayList profissionais;//retorna o nome dos profissionais da consulta para exibir na listview
    private ArrayList<Profissional> profObjetos = new ArrayList<Profissional>();//retorna todos os profissionais da consulta no banco
    private ArrayList listaAuxiliar = new ArrayList();


    private String[] filtro = {"Nome", "Especialidade"};
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
        


        ListView listView = view.findViewById(R.id.lv_profissional);
        EditText textoBusca = view.findViewById(R.id.bpf_et_buscar);
        Button botaoBuscar = view.findViewById(R.id.bpf_bt_buscar);
        Button botaoFiltrar = view.findViewById(R.id.bpf_bp_filtro);
        textViewFiltro = view.findViewById(R.id.textView);

        adapter = new ArrayAdapter(
                getActivity(), // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                profissionais //array list contendo todos os contados
        );
        listView.setAdapter(adapter); //seta o adaptados

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebase().child("profissionais");


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
                Log.i("VALOR",profissionais.get(i).toString() +":"+ i);
                Log.i("VALOR",profObjetos.get(i).equals(profissionais.get(i)) +":"+ i);

                Log.i("VALOR",profObjetos.get(i).getNome() +":"+ i);

                Intent intent = new Intent(getActivity(), PerfilCliente.class);

                for(int j = 0; j < profObjetos.size();j++){
                    if(profObjetos.get(j).getNome().equals(profissionais.get(i))){
                        Log.i("VALOR",profObjetos.get(j).getNome() + ":"+ profissionais.get(i));

                        intent.putExtra("email", profObjetos.get(j).getId());
                        intent.putExtra("nome", profObjetos.get(j).getNome());
                        intent.putExtra("id", profObjetos.get(j).getId());
                        intent.putExtra("telefone", profObjetos.get(j).getTelefone());
                        intent.putExtra("endereco", profObjetos.get(j).getEndereco());
                        intent.putExtra("especialidade", profObjetos.get(j).getEspecialidade());
                        intent.putExtra("formacao", profObjetos.get(j).getFormacao());
                        intent.putExtra("Num_registro", profObjetos.get(j).getNum_registro());

                        intent.putExtra("horaAbrir",profObjetos.get(j).getHoraAbrir());
                        intent.putExtra("horaFechar",profObjetos.get(j).getHoraFechar());

                        intent.putExtra("cliente", "profissionais");
                        intent.putExtra("classe", Profissional.class);
                        startActivity(intent);
                    }
                }


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
                        textViewFiltro.setText(filtragem);
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


        textoBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { // cada vez que uma letra é digitada na busca esse método é chamado

                buscar(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        switch (filtragem){
            case ("Nome"):

                profissionais.clear();//limpa o array profissionais
                profObjetos.clear();//limpa o array profObjetos

                textViewFiltro.setText(textViewFiltro.getText()+" Nome");

                // consulta todos os profissionais no banco
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //profissionais.clear();
                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                Profissional p = dados.getValue(Profissional.class);//retorna cada objeto da consulta em p
                                String nome = p.getNome();
                                Log.i("NOME", nome);
                                profObjetos.add(p);//adiciona o profissional p em profObjetos
                                listaAuxiliar.add(nome);//adiciona o nome do profissional p na lista auxiliar
                            }
                            //profissionais = profissionalDB.buscarDados(dataSnapshot,profissionais);
                            adapter.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas

                            Log.i("ARRAY",profissionais.toString()); //exibe array com o nome de todos os profissioais

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case ("Especialidade"): // filtro por especialidade não está funcionando. Tenho que refazer
                profissionais.clear();
                profObjetos.clear();
                firebase.orderByChild("especialidade").equalTo("Cardiologista").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            //profissionais.clear();
                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                Profissional p = dados.getValue(Profissional.class);//retorna cada objeto da consulta em p
                                String nome = p.getNome();
                                Log.i("NOME", nome);
                                profObjetos.add(p);//adiciona o profissional p em profObjetos
                                listaAuxiliar.add(nome);//adiciona o nome do profissional p na lista auxiliar
                            }
                            //profissionais = profissionalDB.buscarDados(dataSnapshot,profissionais);
                            adapter.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas

                            Log.i("ARRAY",profissionais.toString()); //exibe array com o nome de todos os profissioais

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
        }





        /*
        botaoBuscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                profissionais.clear();

                final String nome = textoBusca.getText().toString();//pega nome do campo de textoBusca
                switch (filtragem) {
                    case ("Todos")://se o filtro selecionado for Todos


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

                                    Log.i("ARRAY",profissionais.toString()); //exibe array com o nome de todos os profissioais

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
        });*/

        return view;
    }

    public void buscar(String textoBusca){
        System.out.println(textoBusca);
        profissionais.clear();
        for(int i=0;i<listaAuxiliar.size();i++){
            if(listaAuxiliar.get(i).toString().toLowerCase().contains(textoBusca.toLowerCase())){
                profissionais.add(listaAuxiliar.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
