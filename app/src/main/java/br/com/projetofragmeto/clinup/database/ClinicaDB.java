package br.com.projetofragmeto.clinup.database;

import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Clinica;
import br.com.projetofragmeto.clinup.model.Hospital;

public class ClinicaDB implements Database {
    private DatabaseReference firebase;
    @Override
    public void adicionarDados(Object object, String id) {
        firebase = ConfiguracaoFirebase.getFirebase().child("clinica");
        firebase.child(id).setValue(object);
    }

    @Override
    public void removerDados() {

    }

    @Override
    public ArrayList buscarDados(DataSnapshot data, ArrayList arrayList)  {

        arrayList.clear();
        for(DataSnapshot dados: data.getChildren()){
            Clinica c = dados.getValue(Clinica.class);
            String nome = c.getNome();
            arrayList.add(nome);
        }

        return arrayList;
    }

    @Override
    public void alterarDados() {

    }
}
