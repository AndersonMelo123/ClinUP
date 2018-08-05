package br.com.projetofragmeto.clinup.helper;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.projetofragmeto.clinup.activity.ZipCodeSearchActivity;
import br.com.projetofragmeto.clinup.model.Endereco;


public class ZipCodeRequest extends AsyncTask<Void, Void, Void> {
    private WeakReference<ZipCodeSearchActivity> activity;

    public ZipCodeRequest(ZipCodeSearchActivity activity ){
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
    protected Void doInBackground(Void... voids) {
        try {
            String jsonString = JsonRequest.request( activity.get().getUriZipCode() );

            Gson gson = new Gson();
            JSONArray jsonArray = new JSONArray(jsonString);
            activity.get().getEnderecos().clear();

            for( int i = 0; i < jsonArray.length(); i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Endereco a = gson.fromJson( jsonObject.toString(), Endereco.class );
                activity.get().getEnderecos().add( a );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void svoid) {
        super.onPostExecute(svoid);

        if( activity.get() != null ){
            activity.get().lockFields( false );
            activity.get().updateListView();
        }
    }
}
