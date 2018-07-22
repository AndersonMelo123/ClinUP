package br.com.projetofragmeto.clinup.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

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
            //String end = p.getEndereco();
            arrayList.add(nome);
            //arrayList.add(end);
        }

        return arrayList;
    }

    @Override
    public void alterarDados() {

    }
}
