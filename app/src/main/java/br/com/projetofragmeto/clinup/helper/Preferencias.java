package br.com.projetofragmeto.clinup.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "clinup.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";

    public Preferencias( Context contextoParametro){

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE );
        editor = preferences.edit();

    }

    public void salvarDados( String identificadorUsuario, String nomeUsuario ){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario); // Salva o identificador no aparelho
        editor.putString(CHAVE_NOME, nomeUsuario); // Salva o identificador no aparelho
        editor.commit();

    }

    public String getIdentificador(){

        return preferences.getString(CHAVE_IDENTIFICADOR, null); // Recuperar o identificador que foi salvo
    }

    public String getNOME(){

        return preferences.getString(CHAVE_NOME, null); // Recuperar o identificador que foi salvo
    }
}
