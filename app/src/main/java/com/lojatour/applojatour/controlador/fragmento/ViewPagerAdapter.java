package com.lojatour.applojatour.controlador.fragmento;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    //este es el manejador del fragmento
    public static int TAB_COUNT = 1;//NUMERO DE PESTAÑAS

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return HomeFragmento.TITLE;
        }

        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {//permite que fragmento quiero agregar
        switch (position){
            case 0: return  HomeFragmento.newInstance();
        }
        return null;
    }

    //Diagrama de la base de datos
    //api del servico web
    //documentacion del servicio web
}
