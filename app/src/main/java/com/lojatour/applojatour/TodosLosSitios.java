package com.lojatour.applojatour;

import android.content.Intent;
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
import com.lojatour.applojatour.controlador.adaptador.ListaAdaptadorSitiosWs;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class TodosLosSitios extends AppCompatActivity {

    private ListaAdaptadorSitiosWs listaAdaptadorWS;
    private ListView listView;
    private RequestQueue requestQueue;//
    private Intent intentA1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsites);

        listView= (ListView)findViewById(R.id.mi_lista);//en el main no hubiera ido el rootView
        intentA1 = new Intent(getApplicationContext(),SitiosMasVisitados.class);

        //listView.setEmptyView(findViewById(R.id.lista_vacia));
        listaAdaptadorWS = new ListaAdaptadorSitiosWs(this);//en el main hubiera ido this
        listView.setAdapter(listaAdaptadorWS);

        requestQueue = Volley.newRequestQueue(this);//en el main hubiera ido con el this

        consultarSitiosWS();



        //este metodo se activa cuando se da click enu un item dela lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                SitioTuristicoWs sitio= listaAdaptadorWS.getItem(position);

                muestraDialogo(sitio);
            }
        });


    }

    /**     *
     * @param sitio
     */
    private void muestraDialogo(SitioTuristicoWs sitio){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //
        View mView = getLayoutInflater().inflate(R.layout.dialog_sitio, null);

       ImageView poster = (ImageView) mView.findViewById(R.id.imgFilmDlg);
        Picasso.get().load(sitio.getRuta()).error(R.mipmap.ic_home).into(poster);

        TextView titulo = (TextView)mView.findViewById(R.id.txtNombreDlg);
        titulo.setText(sitio.getNombre());

        TextView descrip = (TextView)mView.findViewById(R.id.txtDescripDlg);
        descrip.setText(sitio.getDescripcion());
        System.out.println("DESCRIP: "+sitio.getDescripcion());

        builder.setView(mView);

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void consultarSitiosWS(){
        //aqui van todos los request
        VolleyPeticion<SitioTuristicoWs[]> films = Conexion.getListaSitios(
                getApplicationContext(),
                new Response.Listener<SitioTuristicoWs[]>() {
                    @Override
                    public void onResponse(SitioTuristicoWs[] response) {
                        //cuando son de este tipo no va con el this va con el getApplicationContext
                        listaAdaptadorWS = new ListaAdaptadorSitiosWs(Arrays.asList(response),getApplicationContext());//en el main hubiera ido con getApplicationContext()
                        listView.setAdapter(listaAdaptadorWS);
                        System.out.println("HolaaaXXXXXXXXX");
                       // consultarImagenWS("5");

                Log.i("msgResponse",response.length+"");

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

        requestQueue.add(films);

    }




}
