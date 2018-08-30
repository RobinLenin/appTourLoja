package com.lojatour.applojatour;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Arrays;

public class TodosLosSitios extends AppCompatActivity {

    private ListaAdaptadorSitiosWs listaAdaptadorWS;
    private ListView listView;
    private RequestQueue requestQueue;//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsites);

        listView= (ListView)findViewById(R.id.mi_lista);//en el main no hubiera ido el rootView

        //listView.setEmptyView(findViewById(R.id.lista_vacia));
        listaAdaptadorWS = new ListaAdaptadorSitiosWs(this);//en el main hubiera ido this
        listView.setAdapter(listaAdaptadorWS);

        requestQueue = Volley.newRequestQueue(this);//en el main hubiera ido con el this

        consultarWS();

        //este metodo se activa cuando se da click enu un item dela lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                SitioTuristicoWs sitio= listaAdaptadorWS.getItem(position);

                muestraDialogo(sitio);
            }
        });

    }
    private void muestraDialogo(SitioTuristicoWs sitio){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //
        View mView = getLayoutInflater().inflate(R.layout.dialog_pelicula, null);


       /* ImageView poster = (ImageView) mView.findViewById(R.id.imgFilmDlg);
        Picasso.with(getApplicationContext()).load(pelicula.getPoster()).error(R.mipmap.ic_home).fit().centerInside().into(poster);
*/
        TextView titulo = (TextView)mView.findViewById(R.id.txtNombreDlg);
        titulo.setText(sitio.getNombre());

        TextView descrip = (TextView)mView.findViewById(R.id.txtDescripDlg);
        descrip.setText(sitio.getDescripcion());


        builder.setView(mView);

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void consultarWS(){
        //aqui van todos los request
        VolleyPeticion<SitioTuristicoWs[]> films = Conexion.getListaSitios(
                getApplicationContext(),
                new Response.Listener<SitioTuristicoWs[]>() {
                    @Override
                    public void onResponse(SitioTuristicoWs[] response) {
                        //cuando son de este tipo no va con el this va con el getApplicationContext
                        listaAdaptadorWS = new ListaAdaptadorSitiosWs(Arrays.asList(response),getApplicationContext());//en el main hubiera ido con getApplicationContext()
                        listView.setAdapter(listaAdaptadorWS);

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

                    }
                }
        );

        requestQueue.add(films);

    }

}
