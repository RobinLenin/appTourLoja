package com.lojatour.applojatour.controlador.fragmento;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.lojatour.applojatour.EventosActivity;
import com.lojatour.applojatour.LugaresMasVisitados;
import com.lojatour.applojatour.MainActivity;
import com.lojatour.applojatour.R;
import com.lojatour.applojatour.SitiosMasVisitados;
import com.lojatour.applojatour.TodosLosSitios;


public class HomeFragment extends Fragment {

    private VideoView videoView;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private LinearLayout layout002;//aqui estan los 4 cardViews
    private Intent intent1;
    private Intent intent2;
    private Intent intent3;
    private Intent intent4;

/*
 Como tenemos 4 vistas diferentes para cambiar con el botonNavigationView,
 necesitamos crear 4 layouts y 4 clases de fragmento de Java.*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       /*
simplemente cambie el fragmento_pashboard con el fragmento que desea inflar
como si la clase es HomeFragment debería tener R.layout.home_fragment
si es SearchFragment debería tener R.layout.fragment_search*/
        View rootView=inflater.inflate(R.layout.fragment_home,container,false);//fragmento_home

        //cuando se usa framgents los findViewByid van con rootView
        layout002 = (LinearLayout)rootView.findViewById(R.id.layout002);
        cardView1 = (CardView) rootView.findViewById(R.id.card1);
        cardView2 = (CardView) rootView.findViewById(R.id.card2);
        cardView3 = (CardView) rootView.findViewById(R.id.card3);
        cardView4 = (CardView) rootView.findViewById(R.id.card4);
        videoView = (VideoView) rootView.findViewById(R.id.video);

        intent1 = new Intent(getActivity(),LugaresMasVisitados.class);
        intent2 = new Intent(getActivity(),SitiosMasVisitados.class);
        intent3 = new Intent(getActivity(),EventosActivity.class);//
        intent4 = new Intent(getActivity(),TodosLosSitios.class);


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "SITIOS MAS VISITADOS", Toast.LENGTH_SHORT).show();
                startActivity(intent1);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Has dado click cardView2", Toast.LENGTH_SHORT).show();
                startActivity(intent2);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "EVENTOS", Toast.LENGTH_SHORT).show();
                startActivity(intent3);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TODOS LOS SITIOS", Toast.LENGTH_SHORT).show();
                startActivity(intent4);
            }
        });

        String path = "android.resource://" + getActivity().getPackageName()
                + "/" + R.raw.videotwo;
        Log.i("pathVideo: ",path);
        videoView.setVideoURI(Uri.parse(path));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
                mp.setLooping(true);
            }
        });
        videoView.start();

        return rootView;
        //return inflater.inflate(R.layout.fragment_home, null);

    }

    /**
     * Metodo que ayuda a reinicar el video cuando se cambia de actividad y luego se regresa
     * a la misma actividad
     */
    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }
}
