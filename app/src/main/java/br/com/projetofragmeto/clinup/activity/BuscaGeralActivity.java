package br.com.projetofragmeto.clinup.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

    private ArrayAdapter<String> adapter;

    private ArrayList<String> listaNomes = new ArrayList<>();//retorna o nome dos listaNomes da consulta para exibir na listview
    private ArrayList<Profissional> profObjetos = new ArrayList<>();//retorna todos os listaNomes da consulta no banco

    private ArrayList<Clinica> clinObjetos = new ArrayList<>();//retorna todos as Clinicas da consulta no banco
    private ArrayList<String> listaAuxiliar = new ArrayList<>(); // lista auxiliar que pega os nomes dos objetos gerados a pesquisa

    private ArrayList<Hospital> hospObjetos = new ArrayList<>();//retorna todos as Clinicas da consulta no banco

    private ArrayList<Laboratorio> labObjetos = new ArrayList<>();//retorna todos as Clinicas da consulta no banco

    private String[] filtro = {"Profissionais", "Clínicas", "Hospitais", "Laboratórios"};
    private String filtragem = filtro[0];
    private boolean isDefault = true;


    private DatabaseReference firebase; // referencia do banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_geral);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Buscar");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = findViewById(R.id.lista);
        final EditText textoBusca = findViewById(R.id.et_buscar);
        textView = findViewById(R.id.tv_filtro);
        Button filtroBT = findViewById(R.id.bt_filtro);

        firebase = ConfiguracaoFirebase.getFirebase(); // referêcia do firebase para acessar o banco

        buscaDefault();
        listviewDefault();

        adapter = new ArrayAdapter<>(
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



                        switch (textView.getText().toString()) {
                            case ("Profissionais"):
                                isDefault = false;

                                firebase.child("profissionais").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        listaAuxiliar.clear();
                                        listaNomes.clear();
                                        profObjetos.clear();

                                        if (dataSnapshot.getValue() != null) {
                                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                Profissional p = dados.getValue(Profissional.class);//retorna cada objeto da consulta em p
                                                String nome = p.getNome();
                                                profObjetos.add(p);//adiciona o profissional p em profObjetos
                                                listaAuxiliar.add(nome);//adiciona o nome do profissional p em listaNomes
                                                listaNomes.add(nome);//adiciona o nome do profissional p em listaNomes
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

                                        Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                                        for (int j = 0; j < profObjetos.size(); j++) {
                                            if (profObjetos.get(j).getNome().equals(listaNomes.get(i))) {

                                                intent.putExtra("email", profObjetos.get(j).getEmail());
                                                intent.putExtra("nome", profObjetos.get(j).getNome());
                                                intent.putExtra("id", profObjetos.get(j).getId());
                                                intent.putExtra("telefone", profObjetos.get(j).getTelefone());
                                                intent.putExtra("endereco", profObjetos.get(j).getEndereco());
                                                intent.putExtra("especialidade", profObjetos.get(j).getEspecialidade());
                                                intent.putExtra("formacao", profObjetos.get(j).getFormacao());
                                                intent.putExtra("numRegistro", profObjetos.get(j).getId());

                                                intent.putExtra("horaAbrir", profObjetos.get(j).getHoraAbrir());
                                                intent.putExtra("horaFechar", profObjetos.get(j).getHoraFechar());

                                                intent.putExtra("cliente", "profissionais");
                                                intent.putExtra("classe", Profissional.class);
                                                startActivity(intent);
                                            }
                                        }


                                    }
                                });
                                break;
                            case ("Clínicas"):

                                firebase.child("clinica").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        listaAuxiliar.clear();
                                        listaNomes.clear();
                                        profObjetos.clear();
                                        if (dataSnapshot.getValue() != null) {
                                            //clinicas = clinicaDB.buscarDados(dataSnapshot,clinicas);

                                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                Clinica c = dados.getValue(Clinica.class);
                                                String nome = c.getNome();
                                                clinObjetos.add(c);
                                                listaAuxiliar.add(nome);
                                                listaNomes.add(nome);//adiciona o nome do profissional p em listaNomes

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

                                        Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                                        for (int j = 0; j < clinObjetos.size(); j++) {
                                            if (clinObjetos.get(j).getNome().equals(listaNomes.get(i))) {

                                                intent.putExtra("nome", clinObjetos.get(j).getNome());
                                                intent.putExtra("email", clinObjetos.get(j).getEmail());
                                                intent.putExtra("id", clinObjetos.get(j).getId());
                                                intent.putExtra("endereco", clinObjetos.get(j).getEndereco());
                                                intent.putExtra("telefone", clinObjetos.get(j).getTelefone());
                                                intent.putExtra("cnpj", clinObjetos.get(j).getCnpj());
                                                intent.putExtra("especialidade", clinObjetos.get(j).getEspecialidade());
                                                intent.putExtra("numRegistro", clinObjetos.get(j).getCnpj());

                                                intent.putExtra("horaAbrir", clinObjetos.get(j).getHoraAbrir());
                                                intent.putExtra("horaFechar", clinObjetos.get(j).getHoraFechar());

                                                intent.putExtra("cliente", "clinica");
                                                intent.putExtra("classe", Clinica.class);
                                                startActivity(intent);
                                            }
                                        }


                                    }
                                });
                                break;
                            case ("Hospitais"):
                                firebase.child("hospitais").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        listaAuxiliar.clear();
                                        listaNomes.clear();
                                        profObjetos.clear();
                                        if (dataSnapshot.getValue() != null) {
                                            //hospitais = hospitalDB.buscarDados(dataSnapshot,hospitais);
                                            //hospitais.clear();
                                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                Hospital h = dados.getValue(Hospital.class);
                                                String nome = h.getNome();
                                                hospObjetos.add(h);
                                                listaAuxiliar.add(nome);
                                                listaNomes.add(nome);//adiciona o nome do profissional p em listaNomes

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

                                        Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                                        for (int j = 0; j < hospObjetos.size(); j++) {
                                            if (hospObjetos.get(j).getNome().equals(listaNomes.get(i))) {

                                                intent.putExtra("email", hospObjetos.get(j).getEmail());
                                                intent.putExtra("nome", hospObjetos.get(j).getNome());
                                                intent.putExtra("id", hospObjetos.get(j).getId());
                                                intent.putExtra("endereco", hospObjetos.get(j).getEndereco());
                                                intent.putExtra("telefone", hospObjetos.get(j).getTelefone());
                                                intent.putExtra("cnpj", hospObjetos.get(j).getCnpj());
                                                intent.putExtra("especialidade", hospObjetos.get(j).getEspecialidade());
                                                intent.putExtra("numRegistro", hospObjetos.get(j).getCnpj());

                                                intent.putExtra("horaAbrir", hospObjetos.get(j).getHoraAbrir());
                                                intent.putExtra("horaFechar", hospObjetos.get(j).getHoraFechar());

                                                intent.putExtra("cliente", "hospitais");
                                                intent.putExtra("classe", Hospital.class);
                                                startActivity(intent);
                                            }
                                        }


                                    }
                                });
                                break;
                            case ("Laboratórios"):
                                firebase.child("laboratorios").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        listaAuxiliar.clear();
                                        listaNomes.clear();
                                        profObjetos.clear();

                                        if (dataSnapshot.getValue() != null) {
                                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                Laboratorio l = dados.getValue(Laboratorio.class);
                                                String nome = l.getNome();
                                                labObjetos.add(l);
                                                listaAuxiliar.add(nome);
                                                listaNomes.add(nome);//adiciona o nome do profissional p em listaNomes

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

                                        Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                                        for (int j = 0; j < labObjetos.size(); j++) {
                                            if (labObjetos.get(j).getNome().equals(listaNomes.get(i))) {

                                                intent.putExtra("email", labObjetos.get(j).getEmail());
                                                intent.putExtra("nome", labObjetos.get(j).getNome());
                                                intent.putExtra("id", labObjetos.get(j).getId());
                                                intent.putExtra("endereco", labObjetos.get(j).getEndereco());
                                                intent.putExtra("telefone", labObjetos.get(j).getTelefone());
                                                intent.putExtra("especialidade", labObjetos.get(j).getEspecialidade());
                                                intent.putExtra("numRegistro", labObjetos.get(j).getCnpj());

                                                intent.putExtra("horaAbrir", labObjetos.get(j).getHoraAbrir());
                                                intent.putExtra("horaFechar", labObjetos.get(j).getHoraFechar());

                                                intent.putExtra("cliente", "laboratorios");
                                                intent.putExtra("classe", Laboratorio.class);
                                                startActivity(intent);
                                                break;
                                            }
                                        }
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

    //percorre os dados que foram carregados no banco, verificando se a string passada existe e atualiza a listview
    public void buscar(String textoBusca) {
        System.out.println(textoBusca);
        if (listaNomes != null)
            listaNomes.clear();
        for (int i = 0; i < listaAuxiliar.size(); i++) {
            if (listaAuxiliar.get(i).toLowerCase().contains(textoBusca.toLowerCase())) {
                listaNomes.add(listaAuxiliar.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void buscaDefault() {
        textView.setText("Profissionais");
        firebase.child("profissionais").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Profissional p = dados.getValue(Profissional.class);//retorna cada objeto da consulta em p
                        String nome = p.getNome();
                        profObjetos.add(p);//adiciona o profissional p em profObjetos
                        listaNomes.add(nome);//adiciona o nome do profissional p em listaNomes
                        listaAuxiliar.add(nome);//adiciona o nome do profissional p em listaNomes
                    }
                    //adapter.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void listviewDefault() {
        // método que pega o click da listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(BuscaGeralActivity.this, PerfilCliente.class);

                for (int j = 0; j < profObjetos.size(); j++) {
                    if (profObjetos.get(j).getNome().equals(listaNomes.get(i))) {

                        intent.putExtra("email", profObjetos.get(j).getEmail());
                        intent.putExtra("nome", profObjetos.get(j).getNome());
                        intent.putExtra("id", profObjetos.get(j).getId());
                        intent.putExtra("telefone", profObjetos.get(j).getTelefone());
                        intent.putExtra("endereco", profObjetos.get(j).getEndereco());
                        intent.putExtra("especialidade", profObjetos.get(j).getEspecialidade());
                        intent.putExtra("formacao", profObjetos.get(j).getFormacao());
                        intent.putExtra("numRegistro", profObjetos.get(j).getId());

                        intent.putExtra("horaAbrir", profObjetos.get(j).getHoraAbrir());
                        intent.putExtra("horaFechar", profObjetos.get(j).getHoraFechar());

                        intent.putExtra("cliente", "profissionais");
                        intent.putExtra("classe", Profissional.class);
                        startActivity(intent);
                    }
                }


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}

