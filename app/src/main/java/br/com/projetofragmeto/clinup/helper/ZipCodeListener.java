package br.com.projetofragmeto.clinup.helper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import br.com.projetofragmeto.clinup.activity.CadastroEndereco;


public class ZipCodeListener implements TextWatcher {
    private Context context;

    public ZipCodeListener( Context context ){
        this.context = context;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String zipCode = editable.toString();

        if( editable.length() == 8 ){
            new AddressRequest( (CadastroEndereco) context ).execute();
        }
    }
}
