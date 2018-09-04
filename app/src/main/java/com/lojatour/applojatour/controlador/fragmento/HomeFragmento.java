package com.lojatour.applojatour.controlador.fragmento;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;

import com.lojatour.applojatour.MainActivity;
import com.lojatour.applojatour.MapsActivity;
import com.lojatour.applojatour.R;

import com.lojatour.applojatour.controlador.adaptador.ListaAdaptadorSitiosWs;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;

public class HomeFragmento extends Fragment {

    public static String TITLE="";
    private ListView listView;
    private ListaAdaptadorSitiosWs listaAdaptadorSitiosWs;
    private RequestQueue requestQueue;

    private Button btnIrRuta;

    //singleton
    public static HomeFragmento newInstance(){
        return new HomeFragmento();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TITLE = "Listado";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.activity_prueba,container,false);//fragmento_home

        //listView =(ListView)rootView.findViewById(R.id.lxxx);//mi_lista
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));//si esta la lista vacia desaparece
        //y se muestra el subview
        requestQueue= Volley.newRequestQueue(getActivity());
        consultaSitioTuristicoWs();
        btnIrRuta = (Button) rootView.findViewById(R.id.btnIrRuta);
        btnIrRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irMapa();
            }
        });

        return rootView;

    }


    public void irMapa(){
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private  void consultaSitioTuristicoWs(){
        VolleyPeticion<SitioTuristicoWs[]>noticias= Conexion.listarSitiosAll(
                getActivity(), MainActivity.TOKEN,
                MainActivity.ID_EXTERNAL,
                new Response.Listener<SitioTuristicoWs[]>() {
                    @Override
                    public void onResponse(SitioTuristicoWs[] response) {
                        listaAdaptadorSitiosWs=new ListaAdaptadorSitiosWs(Arrays.asList(response),getActivity());
                        listView.setAdapter(listaAdaptadorSitiosWs);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 = Toast.makeText(getActivity(),
                                getContext().getString(R.string.nombre_app),
                                Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        toast1.show();//en getString msg_no_busqueda
                    }
                }
        );
        requestQueue.add(noticias);
    }
}
