package br.com.projetofragmeto.clinup.database;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
            arrayList.add(nome);
        }

        return arrayList;
    }

    @Override
    public void alterarDados() {

    }
}
