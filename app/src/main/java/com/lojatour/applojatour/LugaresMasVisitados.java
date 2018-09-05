package com.lojatour.applojatour;


import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lojatour.applojatour.controlador.adaptador.ListaAdaptadorVisitaWs;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.modelo.VisitaWs;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * Actividad para listar en un layout los 3 sitios más viistados
 * @author alexjh
 * @version 1.00
 */
public class LugaresMasVisitados extends AppCompatActivity {

    private ListaAdaptadorVisitaWs listaAdaptadorWS;
    private ListView listView;
    private RequestQueue requestQueue;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topsites);

        listView = (ListView) findViewById(R.id.mi_listatop);//en el main no hubiera ido el rootView


        //listView.setEmptyView(findViewById(R.id.lista_vacia));
        listaAdaptadorWS = new ListaAdaptadorVisitaWs(this);//en el main hubiera ido this
        listView.setAdapter(listaAdaptadorWS);

        requestQueue = Volley.newRequestQueue(this);//en el main hubiera ido con el this

        consultarVisitaWS();


        //este metodo se activa cuando se da click enu un item dela lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                VisitaWs sitio = listaAdaptadorWS.getItem(position);

                muestraDialogo(sitio);
            }
        });


    }

    /**
     * Método para mostrar un dialog al dar click el cualquier item de la lista
     * @param sitio
     */
    private void muestraDialogo(VisitaWs sitio) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //
        View mView = getLayoutInflater().inflate(R.layout.dialog_sitiotop, null);

        ImageView poster = (ImageView) mView.findViewById(R.id.imgFilmDlg);
        Picasso.get().load(sitio.getRuta()).error(R.mipmap.ic_home).into(poster);

        TextView titulo = (TextView) mView.findViewById(R.id.txtNombreDlg);
        titulo.setText(sitio.getNombre());

        TextView contador = (TextView) mView.findViewById(R.id.txContadorDlg);
        contador.setText(sitio.getContador());

        TextView favoritos = (TextView) mView.findViewById(R.id.txtFavoritoDlg);
        favoritos.setText(sitio.getFavorito());

        TextView likes = (TextView) mView.findViewById(R.id.txtMeGustaDlg);
        likes.setText(sitio.getMe_gusta());

        builder.setView(mView);

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Método que permite listar todos los 3 sitios más visitados
     */
    private void consultarVisitaWS(){
        //aqui van todos los request
        VolleyPeticion<VisitaWs[]> visitas = Conexion.getListaSitiosMasVisitados(
                getApplicationContext(),
                new Response.Listener<VisitaWs[]>() {
                    @Override
                    public void onResponse(VisitaWs[] response) {
                        //cuando son de este tipo no va con el this va con el getApplicationContext
                        listaAdaptadorWS = new ListaAdaptadorVisitaWs(Arrays.asList(response),getApplicationContext());//en el main hubiera ido con getApplicationContext()
                        listView.setAdapter(listaAdaptadorWS);

                        //dialog();/////////
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

        requestQueue.add(visitas);

    }




}

