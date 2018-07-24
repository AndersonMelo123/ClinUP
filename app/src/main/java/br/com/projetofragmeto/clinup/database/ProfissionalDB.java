package br.com.projetofragmeto.clinup.database;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Profissional;

public class ProfissionalDB implements Database {

    private DatabaseReference firebase;
    @Override
    public void adicionarDados(Object object,String id) {
        firebase = ConfiguracaoFirebase.getFirebase().child("profissionais");
        firebase.child(id).setValue(object);
    }


    @Override
    public void removerDados() {

    }

    @Override
    public ArrayList buscarDados(DataSnapshot data,ArrayList arrayList) {

        arrayList.clear();
        for(DataSnapshot dados: data.getChildren()){
            Profissional p = dados.getValue(Profissional.class);
            String nome = p.getNome();
            Log.i("NOME",nome);
            arrayList.add(nome);
        }

        return arrayList;
    }

    @Override
    public void alterarDados() {

    }

    public ArrayList filtroNome(String nome,DataSnapshot data, final ArrayList arrayList){


        if(data.exists()) {
            for(DataSnapshot dados: data.getChildren()){
                Profissional p = dados.getValue(Profissional.class);

                arrayList.add(p.getNome());

                }
        }



        return arrayList;

    } public ArrayList filtroEspecialidade(String especialidade,DataSnapshot data, final ArrayList arrayList){


        if(data.exists()) {
            for(DataSnapshot dados: data.getChildren()){
                Profissional p = dados.getValue(Profissional.class);

                arrayList.add(p.getNome());

                }
        }



        return arrayList;

    }
}
