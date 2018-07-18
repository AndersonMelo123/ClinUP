package br.com.projetofragmeto.clinup.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.activity.AgendarActivity;


public class HomeFragment extends Fragment {

    private Button bt_Agendar;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        bt_Agendar = view.findViewById(R.id.bt_agendarID);

        bt_Agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AgendarActivity.class);
                startActivity(intent);

            }
        });


        return view;

    }
}
