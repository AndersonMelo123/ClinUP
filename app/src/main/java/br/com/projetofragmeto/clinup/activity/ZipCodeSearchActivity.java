package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.adapter.AddressAdapter;
import br.com.projetofragmeto.clinup.helper.ZipCodeRequest;
import br.com.projetofragmeto.clinup.model.Endereco;
import br.com.projetofragmeto.clinup.utils.Util;

public class ZipCodeSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Spinner spStates;
    private ListView lvAddress;
    private List<Endereco> enderecos;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_code_search);

        enderecos = new ArrayList<>();
        lvAddress = findViewById(R.id.lv_address);
        AddressAdapter adapter = new AddressAdapter(this, enderecos);
        lvAddress.setAdapter( adapter );
        lvAddress.setOnItemClickListener( this );

        //configurações da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarActivityEsqueciCep);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        spStates = findViewById(R.id.sp_state);
        spStates.setAdapter( ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item) );

        util = new Util(this,
                R.id.et_street,
                R.id.et_city,
                R.id.sp_state);
    }

    public void lockFields( boolean isToLock ){
        util.lockFields( isToLock );
    }

    private String getState(){
        String[] stateArray = ((String) spStates.getSelectedItem()).split("\\(");

        if( stateArray.length == 2 ){
            stateArray = stateArray[1].split("\\)");
            return stateArray[0];
        }
        return "";
    }

    private String getCity(){
        return ((EditText) findViewById(R.id.et_city)).getText().toString();
    }

    private String getStreet(){
        return ((EditText) findViewById(R.id.et_street)).getText().toString();
    }

    public String getUriZipCode(){
        String uri = "https://viacep.com.br/ws/";
        uri += getState()+"/";
        uri += getCity()+"/";
        uri += getStreet()+"/json/";
        return uri;
    }

    public List<Endereco> getEnderecos(){
        return enderecos;
    }

    public void updateListView(){
        ((AddressAdapter) lvAddress.getAdapter()).notifyDataSetChanged();
    }

    public void searchAddress( View view ){ new ZipCodeRequest( this ).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String[] zipCodeArray = enderecos.get( i ).getCep().split("-");
        String zipCode = zipCodeArray[0]+zipCodeArray[1];

        Intent intent = new Intent();
        intent.putExtra( Endereco.ZIP_CODE_KEY, zipCode );
        setResult( RESULT_OK, intent );
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}