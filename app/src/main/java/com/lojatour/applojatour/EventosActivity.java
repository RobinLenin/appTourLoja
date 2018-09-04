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
import com.lojatour.applojatour.controlador.adaptador.ListaAdaptadorEventosWs;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.modelo.EventoWs;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * Actividad para llamar al layout de eventos y
 * al controlador que permite listar dichos eventos
 * @author alexjh
 * @since 1.0
 */
public class EventosActivity extends AppCompatActivity {

    private ListaAdaptadorEventosWs listaAdaptadorEvWS;
    private ListView listView;
    private RequestQueue requestQueue;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        listView = (ListView) findViewById(R.id.mi_listaevento);//en el main no hubiera ido el rootView


        //listView.setEmptyView(findViewById(R.id.lista_vacia));
        listaAdaptadorEvWS = new ListaAdaptadorEventosWs(this);//en el main hubiera ido this
        listView.setAdapter(listaAdaptadorEvWS);

        requestQueue = Volley.newRequestQueue(this);//en el main hubiera ido con el this

        consultarEventoWS();


        //este metodo se activa cuando se da click enu un item dela lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


                EventoWs evento = listaAdaptadorEvWS.getItem(position);

               //muestraDialogo(evento);
            }
        });


    }

    /**
     * Método que abre un AlertDialog para presentar todos los eventos
     * @param sitio
     */
    private void muestraDialogo(EventoWs sitio) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //
        View mView = getLayoutInflater().inflate(R.layout.dialog_sitiotop, null);

        ImageView poster = (ImageView) mView.findViewById(R.id.imgFilmDlg);
        Picasso.get().load(sitio.getRuta()).error(R.mipmap.ic_home).into(poster);

        TextView titulo = (TextView) mView.findViewById(R.id.txtNombreDlg);
        titulo.setText(sitio.getNombre());

        TextView lugar = (TextView) mView.findViewById(R.id.txContadorDlg);
        lugar.setText(sitio.getLugar());

        TextView fecha = (TextView) mView.findViewById(R.id.txtFavoritoDlg);
        fecha.setText(sitio.getFecha());


        builder.setView(mView);

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Método para obtener todos los eventos y ponerlos en la item_listaevento.xml
     */
    private void consultarEventoWS(){
        //aqui van todos los request
        VolleyPeticion<EventoWs[]> visitas = Conexion.getListaEventos(
                getApplicationContext(),
                new Response.Listener<EventoWs[]>() {
                    @Override
                    public void onResponse(EventoWs[] response) {
                        //cuando son de este tipo no va con el this va con el getApplicationContext
                        listaAdaptadorEvWS = new ListaAdaptadorEventosWs(Arrays.asList(response),getApplicationContext());//en el main hubiera ido con getApplicationContext()
                        listView.setAdapter(listaAdaptadorEvWS);

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

