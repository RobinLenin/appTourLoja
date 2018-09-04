package com.lojatour.applojatour;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lojatour.applojatour.controlador.adaptador.ListaAdaptadorVisitaWs;
import com.lojatour.applojatour.controlador.adaptador.ListaAdaptadorVisitaWsFavLik;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.modelo.VisitaWs;

import java.util.Arrays;

public class SitiosLikesActivity extends AppCompatActivity {

    private ListaAdaptadorVisitaWsFavLik listaAdaptador;
    private ListView listView;
    private RequestQueue requestQueue;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios_favoritos);

        listView = (ListView) findViewById(R.id.mi_listafav);//en el main no hubiera ido el rootView


        //listView.setEmptyView(findViewById(R.id.lista_vacia));
        listaAdaptador = new ListaAdaptadorVisitaWsFavLik(this);//en el main hubiera ido this
        listView.setAdapter(listaAdaptador);

        requestQueue = Volley.newRequestQueue(this);//en el main hubiera ido con el this

        consultarVisitaWS();

    }


    private void consultarVisitaWS(){
        //aqui van todos los request
        VolleyPeticion<VisitaWs[]> favorito = Conexion.getListaLikes(
                getApplicationContext(),
                MainActivity.ID_EXTERNAL,
                new Response.Listener<VisitaWs[]>() {
                    @Override
                    public void onResponse(VisitaWs[] response) {
                        //cuando son de este tipo no va con el this va con el getApplicationContext
                        listaAdaptador = new ListaAdaptadorVisitaWsFavLik(Arrays.asList(response), getApplicationContext());
                        listView.setAdapter(listaAdaptador);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 = Toast.makeText(getApplicationContext(),
                                getApplicationContext().getString(R.string.msg_no_busqueda),
                                Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        toast1.show();

                        Log.i("msgResponse","ERROR");

                    }
                }
        );

        requestQueue.add(favorito);

    }
    @Override
    public void onBackPressed() {
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
        finish();
    }

}
