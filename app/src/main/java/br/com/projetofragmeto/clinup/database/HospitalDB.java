package br.com.projetofragmeto.clinup.database;

import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.model.Clinica;
import br.com.projetofragmeto.clinup.model.Hospital;
import br.com.projetofragmeto.clinup.model.Laboratorio;

public class HospitalDB implements Database {
    private DatabaseReference firebase;

    @Override
    public void adicionarDados(Object object, String id) {
        firebase = ConfiguracaoFirebase.getFirebase().child("hospitais");
        firebase.child(id).setValue(object);
    }

    @Override
    public void removerDados() {

    }

    @Override
    public ArrayList buscarDados(DataSnapshot data, ArrayList arrayList) {

        arrayList.clear();
        for(DataSnapshot dados: data.getChildren()){
            Hospital h = dados.getValue(Hospital.class);
            String nome = h.getNome();
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
                Clinica c = dados.getValue(Clinica.class);

                arrayList.add(c.getNome());

            }
        }



        return arrayList;

    }
    public ArrayList filtroExame(String especialidade,DataSnapshot data, final ArrayList arrayList){


        if(data.exists()) {
            for(DataSnapshot dados: data.getChildren()){
                Clinica c = dados.getValue(Clinica.class);

                arrayList.add(c.getNome());

            }
        }

        return arrayList;
    }
}
