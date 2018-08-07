package br.com.projetofragmeto.clinup.helper;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import br.com.projetofragmeto.clinup.activity.CadastroEndereco;
import br.com.projetofragmeto.clinup.model.Endereco;


public class AddressRequest extends AsyncTask<Void, Void, Endereco> {
    private WeakReference<CadastroEndereco> activity;

    public AddressRequest( CadastroEndereco activity ){
        this.activity = new WeakReference<>( activity );
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if( activity.get() != null ){
            activity.get().lockFields( true );
        }
    }

    @Override
    protected Endereco doInBackground(Void... voids) {
        try {
            String jsonString = JsonRequest.request( activity.get().getUriZipCode() );

            Gson gson = new Gson();
            return gson.fromJson( jsonString, Endereco.class );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Endereco endereco) {
        super.onPostExecute(endereco);

        if( activity.get() != null ){
            activity.get().lockFields( false );

            if( endereco != null ){
                activity.get().setDataViews(endereco);
            }
        }
    }
}
