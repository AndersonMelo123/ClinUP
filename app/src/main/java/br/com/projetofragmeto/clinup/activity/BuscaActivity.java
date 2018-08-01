package br.com.projetofragmeto.clinup.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;

import br.com.projetofragmeto.clinup.R;
import br.com.projetofragmeto.clinup.adapter.TabBuscarAdapter;
import br.com.projetofragmeto.clinup.fragments.BuscarClinicaFragment;
import br.com.projetofragmeto.clinup.fragments.BuscarHospitalFragment;
import br.com.projetofragmeto.clinup.fragments.BuscarLaboratorioFragment;
import br.com.projetofragmeto.clinup.fragments.BuscarProfissionalFragment;
import br.com.projetofragmeto.clinup.helper.SlidingTabLayout;

public class BuscaActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private BuscarProfissionalFragment buscarProfissionalFragment = new BuscarProfissionalFragment();
    private BuscarClinicaFragment buscarClinicaFragment = new BuscarClinicaFragment();
    private BuscarHospitalFragment buscarHospitalFragment = new BuscarHospitalFragment();
    private BuscarLaboratorioFragment buscarLaboratorioFragment = new BuscarLaboratorioFragment();

    private Bundle data= new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        toolbar = findViewById(R.id.toolbarActivity);
        toolbar.setTitle("Buscar");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){//setinha de voltar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_busca);

        //Configurar Adapter
        TabBuscarAdapter tabBuscarAdapter = new TabBuscarAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabBuscarAdapter);

        slidingTabLayout.setViewPager(viewPager);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //método para finalizar a activity caso seja apertado a setinha de voltar
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
