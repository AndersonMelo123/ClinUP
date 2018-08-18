package br.com.projetofragmeto.clinup.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Clinica;
import br.com.projetofragmeto.clinup.model.Hospital;
import br.com.projetofragmeto.clinup.model.Laboratorio;
import br.com.projetofragmeto.clinup.model.Profissional;

public class BuscaGeralActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textView;

    private ArrayAdapter adapter;

    private ArrayList listaNomes = new ArrayList();//retorna o nome dos listaNomes da consulta para exibir na listview
    private ArrayList<Profissional> profObjetos = new ArrayList<Profissional>();//retorna todos os listaNomes da consulta no banco

    private ArrayList<Clinica> clinObjetos = new ArrayList<Clinica>();//retorna todos as Clinicas da consulta no banco
    private ArrayList listaAuxiliar = new ArrayList(); // lista auxiliar que pega os nomes dos objetos gerados a pesquisa

    private ArrayList<Hospital> hospObjetos = new ArrayList<Hospital>();//retorna todos as Clinicas da consulta no banco

    private ArrayList<Laboratorio> labObjetos = new ArrayList<Laboratorio>();//retorna todos as Clinicas da consulta no banco

    private String[] filtro = {"Profissionais","Clínicas","Hospitais","Laboratórios"};
    String filtragem = filtro[0];

    private DatabaseReference firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_geral);

        //configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Buscar");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = findViewById(R.id.lista);
        EditText textoBusca = findViewById(R.id.et_buscar);
        textView = findViewById(R.id.tv_filtro);
        Button filtroBT = findViewById(R.id.bt_filtro);

        firebase = ConfiguracaoFirebase.getFirebase(); // referêcia do firebase para acessar o banco

        adapter = new ArrayAdapter(
                this, // pega o contexto da activity onde esse fragment está
                R.layout.lista_busca, //layout da lista
                listaNomes //array list contendo todos os contados
        );
        listView.setAdapter(adapter); //seta o adaptados


        filtroBT.setOnClickListener(new View.OnClickListener() {// quando o usuário escolher o filtro
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(BuscaGeralActivity.this);
                mBuilder.setTitle("Filtro");
                mBuilder.setSingleChoiceItems(filtro, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filtragem = filtro[i];
                        textView.setText(filtragem);
                        dialogInterface.dismiss();

                        listaAuxiliar.clear();
                        listaNomes.clear();

                        switch (textView.getText().toString()){
                            case("Profissionais"):
                                Log.i("FILTRO",filtragem);

                                firebase.child("profissionais").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if (dataSnapshot.getValue() != null) {
                                            //listaNomes.clear();
                                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                Profissional p = dados.getValue(Profissional.class);//retorna cada objeto da consulta em p
                                                String nome = p.getNome();
                                                Log.i("NOME", nome);
                                                profObjetos.add(p);//adiciona o profissional p em profObjetos
                                                listaAuxiliar.add(nome);//adiciona o nome do profissional p em listaNomes
                                            }
                                            //listaNomes = profissionalDB.buscarDados(dataSnapshot,listaNomes);
                                            adapter.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas

                                            Log.i("ARRAY", listaNomes.toString()); //exibe array com o nome de todos os profissioais

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                // método que pega o click da listview
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        passarInfoProf(i);
                                        /*Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                                        for(int j = 0; j < profObjetos.size();j++){
                                            if(profObjetos.get(j).getNome().equals(listaNomes.get(i))){
                                                Log.i("VALOR",profObjetos.get(j).getNome() + ":"+ listaNomes.get(i));

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

                                                intent.putExtra("cliente", "listaNomes");
                                                intent.putExtra("classe", Profissional.class);
                                                startActivity(intent);
                                            }
                                        }*/


                                    }
                                });
                                break;
                            case("Clínicas"):
                                Log.i("FILTRO",filtragem);

                                firebase.child("clinica").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if(dataSnapshot.getValue() != null){
                                            //clinicas = clinicaDB.buscarDados(dataSnapshot,clinicas);

                                            for(DataSnapshot dados: dataSnapshot.getChildren()){
                                                Clinica c = dados.getValue(Clinica.class);
                                                String nome = c.getNome();
                                                clinObjetos.add(c);
                                                listaAuxiliar.add(nome);
                                            }


                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                // método que pega o click da listview
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        passarInfoClin(i);
                                        /*Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                                        for(int j = 0; j < clinObjetos.size();j++){
                                            if(clinObjetos.get(j).getNome().equals(listaNomes.get(i))){
                                                Log.i("VALOR",clinObjetos.get(j).getNome() + ":"+ listaNomes.get(i));

                                                intent.putExtra("nome",clinObjetos.get(i).getNome());
                                                intent.putExtra("email", clinObjetos.get(i).getEmail() );
                                                intent.putExtra("id", clinObjetos.get(i).getCnpj() );
                                                intent.putExtra("endereco", clinObjetos.get(i).getEndereco() );
                                                intent.putExtra("telefone", clinObjetos.get(i).getTelefone() );
                                                intent.putExtra("cnpj", clinObjetos.get(i).getCnpj() );

                                                intent.putExtra("horaAbrir",clinObjetos.get(i).getHoraAbrir());
                                                intent.putExtra("horaFechar",clinObjetos.get(i).getHoraFechar());

                                                intent.putExtra("cliente","clinica");
                                                intent.putExtra("classe",Clinica.class);
                                                startActivity(intent);
                                            }
                                        }*/


                                    }
                                });
                                break;
                            case("Hospitais"):
                                Log.i("FILTRO",filtragem);
                                firebase.child("hospitais").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if (dataSnapshot.getValue() != null) {
                                            //hospitais = hospitalDB.buscarDados(dataSnapshot,hospitais);
                                            //hospitais.clear();
                                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                Hospital h = dados.getValue(Hospital.class);
                                                String nome = h.getNome();
                                                hospObjetos.add(h);
                                                listaAuxiliar.add(nome);
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                // método que pega o click da listview
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        passarInfoHosp(i);
                                        /*Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                                        for(int j = 0; j < hospObjetos.size();j++){
                                            if(hospObjetos.get(j).getNome().equals(listaNomes.get(i))){
                                                Log.i("VALOR",hospObjetos.get(j).getNome() + ":"+ listaNomes.get(i));

                                                intent.putExtra("email", hospObjetos.get(i).getEmail());
                                                intent.putExtra("nome", hospObjetos.get(i).getNome());
                                                intent.putExtra("id", hospObjetos.get(i).getCnpj());
                                                intent.putExtra("endereco", hospObjetos.get(i).getEndereco());
                                                intent.putExtra("telefone", hospObjetos.get(i).getTelefone());
                                                intent.putExtra("cnpj", hospObjetos.get(i).getCnpj());

                                                intent.putExtra("horaAbrir",hospObjetos.get(i).getHoraAbrir());
                                                intent.putExtra("horaFechar",hospObjetos.get(i).getHoraFechar());


                                                intent.putExtra("cliente", "hospitais");
                                                intent.putExtra("classe", Hospital.class);
                                                startActivity(intent);
                                            }
                                        }*/


                                    }
                                });
                                break;
                            case("Laboratórios"):
                                Log.i("FILTRO",filtragem);
                                firebase.child("laboratorios").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if (dataSnapshot.getValue() != null) {
                                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                Laboratorio l = dados.getValue(Laboratorio.class);
                                                String nome = l.getNome();
                                                labObjetos.add(l);
                                                listaAuxiliar.add(nome);
                                            }

                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                // método que pega o click da listview
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        passarInfoLab(i);
                                        /*Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                                        for(int j = 0; j < labObjetos.size();j++){
                                            if(labObjetos.get(j).getNome().equals(listaNomes.get(i))){
                                                Log.i("VALOR",labObjetos.get(j).getNome() + ":"+ listaNomes.get(i));

                                                intent.putExtra("email", labObjetos.get(i).getId());
                                                intent.putExtra("nome", labObjetos.get(i).getNome());
                                                intent.putExtra("id", labObjetos.get(i).getId());
                                                intent.putExtra("telefone", labObjetos.get(i).getTelefone());

                                                intent.putExtra("horaAbrir",labObjetos.get(i).getHoraAbrir());
                                                intent.putExtra("horaFechar",labObjetos.get(i).getHoraFechar());

                                                intent.putExtra("cliente", "laboratorios");
                                                intent.putExtra("classe", Laboratorio.class);
                                                startActivity(intent);
                                            }
                                        }*/


                                    }
                                });
                                break;
                        }



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

    }

    public void buscar(String textoBusca){
        System.out.println(textoBusca);
        if (listaNomes != null)
            listaNomes.clear();
        for(int i=0;i<listaAuxiliar.size();i++){
            if(listaAuxiliar.get(i).toString().toLowerCase().contains(textoBusca.toLowerCase())){
                listaNomes.add(listaAuxiliar.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void passarInfoProf(int i){
        Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

        for(int j = 0; j < profObjetos.size();j++){
            if(profObjetos.get(j).getNome().equals(listaNomes.get(i))){
                Log.i("VALOR",profObjetos.get(j).getNome() + ":"+ listaNomes.get(i));

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

                intent.putExtra("cliente", "listaNomes");
                intent.putExtra("classe", Profissional.class);
                startActivity(intent);
            }
        }
    }
    public void passarInfoHosp(int i){
        Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

        for(int j = 0; j < hospObjetos.size();j++){
            if(hospObjetos.get(j).getNome().equals(listaNomes.get(i))){
                Log.i("VALOR",hospObjetos.get(j).getNome() + ":"+ listaNomes.get(i));

                intent.putExtra("email", hospObjetos.get(i).getEmail());
                intent.putExtra("nome", hospObjetos.get(i).getNome());
                intent.putExtra("id", hospObjetos.get(i).getCnpj());
                intent.putExtra("endereco", hospObjetos.get(i).getEndereco());
                intent.putExtra("telefone", hospObjetos.get(i).getTelefone());
                intent.putExtra("cnpj", hospObjetos.get(i).getCnpj());

                intent.putExtra("horaAbrir",hospObjetos.get(i).getHoraAbrir());
                intent.putExtra("horaFechar",hospObjetos.get(i).getHoraFechar());


                intent.putExtra("cliente", "hospitais");
                intent.putExtra("classe", Hospital.class);
                startActivity(intent);
            }
        }
    }
    public void passarInfoLab(int i){
        Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

        for(int j = 0; j < labObjetos.size();j++){
            if(labObjetos.get(j).getNome().equals(listaNomes.get(i))){
                Log.i("VALOR",labObjetos.get(j).getNome() + ":"+ listaNomes.get(i));

                intent.putExtra("email", labObjetos.get(i).getId());
                intent.putExtra("nome", labObjetos.get(i).getNome());
                intent.putExtra("id", labObjetos.get(i).getId());
                intent.putExtra("telefone", labObjetos.get(i).getTelefone());

                intent.putExtra("horaAbrir",labObjetos.get(i).getHoraAbrir());
                intent.putExtra("horaFechar",labObjetos.get(i).getHoraFechar());

                intent.putExtra("cliente", "laboratorios");
                intent.putExtra("classe", Laboratorio.class);
                startActivity(intent);
            }
        }
    }
    public void passarInfoClin(int i){
        Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

        for(int j = 0; j < clinObjetos.size();j++){
            if(clinObjetos.get(j).getNome().equals(listaNomes.get(i))){
                Log.i("VALOR",clinObjetos.get(j).getNome() + ":"+ listaNomes.get(i));

                intent.putExtra("nome",clinObjetos.get(i).getNome());
                intent.putExtra("email", clinObjetos.get(i).getEmail() );
                intent.putExtra("id", clinObjetos.get(i).getCnpj() );
                intent.putExtra("endereco", clinObjetos.get(i).getEndereco() );
                intent.putExtra("telefone", clinObjetos.get(i).getTelefone() );
                intent.putExtra("cnpj", clinObjetos.get(i).getCnpj() );

                intent.putExtra("horaAbrir",clinObjetos.get(i).getHoraAbrir());
                intent.putExtra("horaFechar",clinObjetos.get(i).getHoraFechar());

                intent.putExtra("cliente","clinica");
                intent.putExtra("classe",Clinica.class);
                startActivity(intent);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

