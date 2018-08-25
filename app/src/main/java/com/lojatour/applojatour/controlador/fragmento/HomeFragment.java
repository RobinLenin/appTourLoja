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

import com.lojatour.applojatour.MainActivity;
import com.lojatour.applojatour.R;
import com.lojatour.applojatour.SitiosMasVisitados;

/**
 * Created by Belal on 1/23/2018.
 */

public class HomeFragment extends Fragment {

    private VideoView videoView;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private LinearLayout layout002;//aqui estan los 4 cardViews
    private Intent intent;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       videoView.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       /*
simplemente cambie el fragmento_pashboard con el fragmento que desea inflar
como si la clase es HomeFragment debería tener R.layout.home_fragment
si es DashboardFragment debería tener R.layout.fragment_dashboard*/
        View rootView=inflater.inflate(R.layout.fragment_home,container,false);//fragmento_home

        //cuando se usa framgents los findViewByid van con rootView
        layout002 = (LinearLayout)rootView.findViewById(R.id.layout002);
        cardView1 = (CardView) rootView.findViewById(R.id.card1);
        cardView2 = (CardView) rootView.findViewById(R.id.card2);
        cardView3 = (CardView) rootView.findViewById(R.id.card3);
        cardView4 = (CardView) rootView.findViewById(R.id.card4);
        videoView = (VideoView) rootView.findViewById(R.id.video);

        intent = new Intent(getActivity(),SitiosMasVisitados.class);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "has dado click card", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "has dado click card", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "has dado click card", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "has dado click card", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        //Context pathVideo:: android.resource://com.lojatour.applojatour/2131558400
        //Activity pathVideo:: android.resource://com.lojatour.applojatour/2131558400
        String path = "android.resource://" + getActivity().getPackageName()
                + "/" + R.raw.lojavideo;
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

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }
}
