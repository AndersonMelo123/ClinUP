package br.com.projetofragmeto.clinup.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.activity.PerfilCliente;
import br.com.projetofragmeto.clinup.activity.PrincipalActivity;
import br.com.projetofragmeto.clinup.adapter.AdapterPersonalizadoFavoritos;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Clinica;
import br.com.projetofragmeto.clinup.model.Favoritos;
import br.com.projetofragmeto.clinup.model.Hospital;
import br.com.projetofragmeto.clinup.model.Laboratorio;
import br.com.projetofragmeto.clinup.model.Profissional;

public class FavoritosFragment extends Fragment {

    private ListView listView;
    private String getId, tipo;
    public ArrayList favoritos;//retorna o nome dos profissionais da consulta para exibir na listview
    private DatabaseReference firebase;
    public ArrayList<Favoritos> favObjetos = new ArrayList<Favoritos>();
    private AdapterPersonalizadoFavoritos adapterPersonalizado;

    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        favoritos = new ArrayList();
        listView = view.findViewById(R.id.lv_favoritos);

        adapterPersonalizado = new AdapterPersonalizadoFavoritos(favObjetos, getActivity());

        listView.setAdapter(adapterPersonalizado);

        firebase = ConfiguracaoFirebase.getFirebase().child("favoritos");

        favoritos.clear();//limpa o array profissionais
        favObjetos.clear();//limpa o array profObjetos


        Preferencias preferencesUser = new Preferencias(getContext());
        final String idUsuarios = preferencesUser.getIdentificador();

        //metodo q faz a listagem
        firebase.addValueEventListener(new ValueEventListener() {//faz a consulta no banco
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Favoritos fav = dados.getValue(Favoritos.class);//retorna cada objeto da consulta em fav

                        String idUsuarioFavoritos = fav.getIdUsuario();

                        if (idUsuarioFavoritos.equals(idUsuarios)) {

                            String info = fav.getIdCliente();
                            favObjetos.add(fav);//adiciona o profissional p em profObjetos
                            favoritos.add(info);//adiciona o nome do profissional p em profissionais

                        }
                    }
                    adapterPersonalizado.notifyDataSetChanged();//notifica ao adapter as mudanças ocorridas
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //pega o clic da listView e exbibe o perfil do favorito
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Favoritos favoritos = favObjetos.get(i);

                getId = favoritos.getIdCliente();
                tipo = favoritos.getTipo();

                firebase = ConfiguracaoFirebase.getFirebase().child(tipo);

                firebase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if (dataSnapshot.getKey().equals(getId)) {
                            coletaDadosCliente(tipo, dataSnapshot);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //pega o clic no list view e exclui o favorito
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                getId = favObjetos.get(i).getId();

                excluirFavoritos(getId);
                return true;
            }
        });

        return view;
    }

    private void coletaDadosCliente(String tipo, DataSnapshot dataSnapshot) {

        switch (tipo) {
            case "laboratorios":

                Laboratorio laboratorio = dataSnapshot.getValue(Laboratorio.class);

                Intent intent1 = new Intent(getContext(), PerfilCliente.class);

                intent1.putExtra("email", laboratorio.getId());
                intent1.putExtra("nome", laboratorio.getNome());
                intent1.putExtra("id", laboratorio.getCnpj());
                intent1.putExtra("endereco", laboratorio.getEndereco());
                intent1.putExtra("telefone", laboratorio.getTelefone());
                intent1.putExtra("especialidade", laboratorio.getEspecialidade());
                intent1.putExtra("num_registro", laboratorio.getCnpj());

                intent1.putExtra("horaAbrir", laboratorio.getHoraAbrir());
                intent1.putExtra("horaFechar", laboratorio.getHoraFechar());

                intent1.putExtra("cliente", "laboratorios");
                intent1.putExtra("classe", Laboratorio.class);
                startActivity(intent1);
                break;
            case "clinica":

                Clinica clinica = dataSnapshot.getValue(Clinica.class);

                Intent intent2 = new Intent(getContext(), PerfilCliente.class);

                intent2.putExtra("nome", clinica.getNome());
                intent2.putExtra("email", clinica.getEmail());
                intent2.putExtra("id", clinica.getCnpj());
                intent2.putExtra("endereco", clinica.getEndereco());
                intent2.putExtra("telefone", clinica.getTelefone());
                intent2.putExtra("cnpj", clinica.getCnpj());
                intent2.putExtra("especialidade", clinica.getEspecialidade());
                intent2.putExtra("num_registro", clinica.getCnpj());

                intent2.putExtra("horaAbrir", clinica.getHoraAbrir());
                intent2.putExtra("horaFechar", clinica.getHoraFechar());

                intent2.putExtra("cliente", "clinica");
                intent2.putExtra("classe", Clinica.class);
                startActivity(intent2);
                break;

            case "hospitais":

                Hospital hospital = dataSnapshot.getValue(Hospital.class);

                Intent intent3 = new Intent(getContext(), PerfilCliente.class);

                intent3.putExtra("email", hospital.getEmail());
                intent3.putExtra("nome", hospital.getNome());
                intent3.putExtra("id", hospital.getCnpj());
                intent3.putExtra("endereco", hospital.getEndereco());
                intent3.putExtra("telefone", hospital.getTelefone());
                intent3.putExtra("cnpj", hospital.getCnpj());
                intent3.putExtra("especialidade", hospital.getEspecialidade());
                intent3.putExtra("num_registro", hospital.getCnpj());

                intent3.putExtra("horaAbrir", hospital.getHoraAbrir());
                intent3.putExtra("horaFechar", hospital.getHoraFechar());


                intent3.putExtra("cliente", "hospitais");
                intent3.putExtra("classe", Hospital.class);
                startActivity(intent3);
                break;

            default:
                Profissional profissional = dataSnapshot.getValue(Profissional.class);

                Intent intent = new Intent(getContext(), PerfilCliente.class);

                intent.putExtra("email", profissional.getId());
                intent.putExtra("nome", profissional.getNome());
                intent.putExtra("id", profissional.getNum_registro());
                intent.putExtra("telefone", profissional.getTelefone());
                intent.putExtra("endereco", profissional.getEndereco());
                intent.putExtra("especialidade", profissional.getEspecialidade());
                intent.putExtra("formacao", profissional.getFormacao());
                intent.putExtra("Num_registro", profissional.getNum_registro());

                intent.putExtra("horaAbrir", profissional.getHoraAbrir());
                intent.putExtra("horaFechar", profissional.getHoraFechar());

                intent.putExtra("cliente", "profissionais");
                intent.putExtra("classe", Profissional.class);
                startActivity(intent);
                break;
        }
    }

    private void excluirFavoritos(final String getId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_excluir_favorito, null);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final DatabaseReference bancoDados = ConfiguracaoFirebase.getFirebase().child("favoritos").child(getId);

                bancoDados.removeValue();

                Toast.makeText(getContext(), "Favorito excluido o com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PrincipalActivity.class);
                startActivity(intent);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Cancelar", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}