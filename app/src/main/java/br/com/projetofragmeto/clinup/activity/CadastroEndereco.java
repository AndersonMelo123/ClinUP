package br.com.projetofragmeto.clinup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.config.ConfiguracaoFirebase;
import br.com.projetofragmeto.clinup.helper.Preferencias;
import br.com.projetofragmeto.clinup.model.Endereco;
import br.com.projetofragmeto.clinup.utils.Util;
import br.com.projetofragmeto.clinup.helper.ZipCodeListener;

public class CadastroEndereco extends AppCompatActivity {
    private Button botaoCadastrar;

    private EditText etZipCode;
    private Util util;

    private EditText rua, CEP, complemento, numero, cidade, estado, bairro;

    private DatabaseReference usuarioReferencia;

    private Endereco enderecoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        botaoCadastrar = findViewById(R.id.bt_cadastrarID);

        etZipCode = findViewById(R.id.et_zip_code);
        etZipCode.addTextChangedListener(new ZipCodeListener(this));

        final Spinner spStates = findViewById(R.id.sp_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.states,
                        android.R.layout.simple_spinner_item);
        spStates.setAdapter(adapter);

        util = new Util(this,
                R.id.et_zip_code,
                R.id.et_street,
                R.id.et_complement,
                R.id.et_neighbor,
                R.id.et_city,
                R.id.sp_state,
                R.id.et_number,
                R.id.tv_zip_code_search);

        rua = findViewById(R.id.et_street);
        cidade = findViewById(R.id.et_city);
        numero = findViewById(R.id.et_number);
        bairro = findViewById(R.id.et_neighbor);
        complemento = findViewById(R.id.et_complement);
        CEP = findViewById(R.id.et_zip_code);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enderecoUsuario = new Endereco();

                enderecoUsuario.setBairro(bairro.getText().toString());
                enderecoUsuario.setLogradouro(rua.getText().toString());
                enderecoUsuario.setLocalidade(cidade.getText().toString());
                enderecoUsuario.setComplemento(complemento.getText().toString());
                enderecoUsuario.setCep(CEP.getText().toString());
                enderecoUsuario.setUf(spStates.getSelectedItem().toString());
                enderecoUsuario.setNumero(numero.getText().toString());


                Preferencias preferencesUser = new Preferencias(CadastroEndereco.this);
                final String idUsuario = preferencesUser.getIdentificador(); // Obter o identificador do usuário que está logado

                usuarioReferencia = ConfiguracaoFirebase.getFirebase().child("endereco").push();
                final String id = usuarioReferencia.getKey();
                enderecoUsuario.setId(id);

                usuarioReferencia.setValue(enderecoUsuario);

                usuarioReferencia = ConfiguracaoFirebase.getFirebase();
                usuarioReferencia.child("usuarios").child(idUsuario).child("endereco").setValue(id).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            abrirTelaPrincipal();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == Endereco.REQUEST_ZIP_CODE_CODE
                && resultCode == RESULT_OK ){

            etZipCode.setText( data.getStringExtra( Endereco.ZIP_CODE_KEY ) );
        }
    }

    public String getUriZipCode(){
        return "https://viacep.com.br/ws/"+etZipCode.getText()+"/json/";
    }


    public void lockFields( boolean isToLock ){
        util.lockFields( isToLock );
    }


    public void setDataViews(Endereco endereco){
        setField( R.id.et_street, endereco.getLogradouro() );
        setField( R.id.et_complement, endereco.getComplemento() );
        setField( R.id.et_neighbor, endereco.getBairro() );
        setField( R.id.et_city, endereco.getLocalidade() );
        setSpinner( R.id.sp_state, R.array.states, endereco.getUf() );
    }

    private void setField( int id, String data ){
        ((EditText) findViewById(id)).setText( data );
    }

    private void setSpinner( int id, int arrayId, String data ){
        String[] itens = getResources().getStringArray(arrayId);

        for( int i = 0; i < itens.length; i++ ){

            if( itens[i].endsWith( "("+data+")" ) ){
                ((Spinner) findViewById(id)).setSelection( i );
                return;
            }
        }
        ((Spinner) findViewById(id)).setSelection( 0 );
    }

    public void searchZipCode( View view ){
        Intent intent = new Intent( this, ZipCodeSearchActivity.class );
        startActivityForResult(intent, Endereco.REQUEST_ZIP_CODE_CODE);
    }

    // Abrir tela principal
    private void abrirTelaPrincipal() {
        Intent intent = new Intent(this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
