package br.com.projetofragmeto.clinup.database;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import br.com.projetofragmeto.clinup.model.Endereco;

public interface EnderecoDatabase {
    void inserir(Endereco enderecoUsuario, DatabaseReference usuarioReferencia, String id, String idUsuario, Context applicationContext);
}

