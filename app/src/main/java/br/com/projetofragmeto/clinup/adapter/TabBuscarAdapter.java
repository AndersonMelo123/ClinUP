package br.com.projetofragmeto.clinup.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabBuscarAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"PROFISSIONAIS","CLÍNICAS","HOSPITAIS","LABORATÓRIOS"};

    public TabBuscarAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                //fragment = new BuscarProfissionalFragment();
                break;
            case 1:
                //fragment = new BuscarClinicaFragment();
                break;
            case 2:
                //fragment = new BuscarHospitalFragment();
                break;
            case 3:
                //fragment = new BuscarLaboratorioFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
