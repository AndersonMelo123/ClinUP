package br.com.projetofragmeto.clinup.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Laboratorio;

public class LaboratorioDB implements Database {

    private DatabaseReference firebase;
    @Override
    public void adicionarDados(Object object, String id) {
        firebase = ConfiguracaoFirebase.getFirebase().child("laboratorios");
        firebase.child(id).setValue(object);
    }

    @Override
    public void removerDados() {

    }

    @Override
    public ArrayList buscarDados(DataSnapshot data, ArrayList arrayList){

        arrayList.clear();
        for(DataSnapshot dados: data.getChildren()){
            Laboratorio l = dados.getValue(Laboratorio.class);
            String nome = l.getNome();
            arrayList.add(nome);
        }

        return arrayList;
    }

    @Override
    public void alterarDados() {

    }
}
