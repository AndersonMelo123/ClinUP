package br.com.projetofragmeto.clinup.database;

import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public interface Database {

    public void adicionarDados(Object object,String id);
    public void removerDados();
    public ArrayList buscarDados(DataSnapshot data, ArrayList arrayList);
    public void alterarDados();
}
