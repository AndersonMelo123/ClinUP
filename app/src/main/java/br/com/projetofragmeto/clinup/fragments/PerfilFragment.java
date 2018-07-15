package br.com.projetofragmeto.clinup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import br.com.projetofragmeto.clinup.R;

public class PerfilFragment extends Fragment {

    private TextView nome;
    private TextView email;
    private TextView cpf;
    private TextView dataNascimento;


    private DatabaseReference usuarioReferencia;

    public PerfilFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false); // com essa view eu consigo fazer as alterações dento do fragment


        nome = view.findViewById(R.id.nome_id);
        email = view.findViewById(R.id.email_id);
        cpf = view.findViewById(R.id.telefone_id);
        dataNascimento = view.findViewById(R.id.nascimento_user_fragment_perfil);


        try{
            String idUsuarios = getArguments().getString("id");
        }catch (Exception e){
            Log.i("ERRO",e.toString());

        }

        //nome.setText("" +idUsuarios);
        //Log.i("idUser",""+idUsuarios);


        /*
        usuarioReferencia = ConfiguracaoFirebase.getFirebase() // Consultando o usuário no banco de dados se existir ele pega
                .child("usuarios").child(idUsuarios);

        usuarioReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {// método chamado sempre que os dados forem alterados no banco
                Usuario usuario = new Usuario();
                usuario.setNome(dataSnapshot.child("nome").getValue().toString());//consulta o banco e pega o nome do usuário conectado
                usuario.setEmail(dataSnapshot.child("email").getValue().toString());//consulta o banco e pega o email do usuário conectado
                usuario.setCpf(dataSnapshot.child("cpf").getValue().toString());//consulta o banco e pega o cpf do usuário conectado
                usuario.setDataNascimento(dataSnapshot.child("dataNascimento").getValue().toString());//consulta o banco e pega a data de nascimento do usuário conectado

                nome.setText(usuario.getNome());
                email.setText(usuario.getEmail());
                cpf.setText(usuario.getCpf());
                dataNascimento.setText(usuario.getDataNascimento());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        return view;
    }

}
