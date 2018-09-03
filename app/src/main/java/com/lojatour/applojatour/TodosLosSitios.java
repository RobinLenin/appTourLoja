package com.lojatour.applojatour;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.sql.SQLOutput;
import java.util.Arrays;

import xyz.hanks.library.bang.SmallBangView;

public class TodosLosSitios extends AppCompatActivity {

    private ListaAdaptadorSitiosWs listaAdaptadorWS;
    private ListView listView;
    private RequestQueue requestQueue;//
    private String phone;
    public static double latST = 0.0;
    public static double lngST = 0.0;

    private SmallBangView mSmallBang;
    private Button mButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsites);

        listView= (ListView)findViewById(R.id.mi_lista);//en el main no hubiera ido el rootView
        //listView.setEmptyView(findViewById(R.id.lista_vacia));

        listaAdaptadorWS = new ListaAdaptadorSitiosWs(this);//en el main hubiera ido this
        listView.setAdapter(listaAdaptadorWS);

        requestQueue = Volley.newRequestQueue(this);//en el main hubiera ido con el this

        consultarSitiosWS();


        //este metodo se activa cuando se da click enu un item dela lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                SitioTuristicoWs sitio = listaAdaptadorWS.getItem(position);

                muestraDialogo(sitio);
            }
        });


    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    /**     *
     * @param sitio
     */
    private void muestraDialogo(final SitioTuristicoWs sitio) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //
        View mView = getLayoutInflater().inflate(R.layout.dialog_sitio, null);

        ImageView poster = (ImageView) mView.findViewById(R.id.imgFilmDlg);
        Picasso.get().load(sitio.getRuta()).error(R.mipmap.ic_home).into(poster);

        TextView titulo = (TextView) mView.findViewById(R.id.txtNombreDlg);
        titulo.setText(sitio.getNombre());

        TextView descrip = (TextView) mView.findViewById(R.id.txtDescripDlg);
        descrip.setText(sitio.getDescripcion());
        //////////
        TextView latitud = (TextView) mView.findViewById(R.id.txtLatitudDlg);
        latitud.setText(sitio.getLatitud());
        //
        latST = Double.parseDouble(sitio.getLatitud());

        TextView longuitud = (TextView) mView.findViewById(R.id.txtLonguitudDlg);
        longuitud.setText(sitio.getLonguitud());
        lngST = Double.parseDouble(sitio.getLonguitud());

        TextView telefono = (TextView) mView.findViewById(R.id.txtTelefonoDlg);
        telefono.setText(sitio.getTelefono());
        phone = sitio.getTelefono();

        TextView web = (TextView) mView.findViewById(R.id.txtSitioWebDlg);
        web.setText(sitio.getSitioWeb());

        final SmallBangView like_heart = mView.findViewById(R.id.btnImgLike);

        builder.setView(mView);
        AlertDialog alert = builder.create();

        like_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (like_heart.isSelected()) {
                    like_heart.setSelected(false);

                } else {
                    like_heart.setSelected(true);
                    like_heart.likeAnimation(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            toast("Like +1");
                            String externalSitio = sitio.getExternal_id();
                            System.out.println("EXTERNAL DEL SITIO: "+externalSitio);
                            System.out.println("EXTERNAL DEL USER desde TodosLosSitios: "+MainActivity.ID_EXTERNAL);
                            //metodoDarLike()
                        }
                    });
                }
            }
        });


        alert.show();

    }


    public void onClickLlamada(View v) {

        phone= "tel:"+phone;
        System.out.println("ENTRO EN ONcLICKlLAMADA" + phone);
        Intent i = new Intent(android.content.Intent.ACTION_CALL, Uri.parse(phone));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding

            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "Aun no tienes permisos para llamar", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(i);
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
