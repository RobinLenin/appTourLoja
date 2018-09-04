package com.lojatour.applojatour.controlador.adaptador;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lojatour.applojatour.R;
import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;
import com.lojatour.applojatour.controlador.ws.modelo.VisitaWs;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


public class ListaAdaptadorVisitaWs extends ArrayAdapter<VisitaWs> {

    private List<VisitaWs> dataset;
    Context mContext;
    Activity activity;



    public ListaAdaptadorVisitaWs(List<VisitaWs>data, Context context) {
        super(context, R.layout.item_listatop, data);//no funcionaba porque faltaba el parametro data el el super
        this.dataset=data;
        this.mContext=context;
        System.out.println("Entro al constructor 1");
    }

    public ListaAdaptadorVisitaWs(Context context) {
        super(context, R.layout.lista_vaciatop,new ArrayList<VisitaWs>());//
        this.dataset=new ArrayList<VisitaWs>();
        this.mContext=context;
        System.out.println("Entro al constructor 2");
    }

    /**
     * Esta función es llamada automáticamente cuando se llena un listView mediante un adaptador
     * de manera iterativa, donde el array que recibe el constructor de esta clase es del tipo estático
     * @param position
     * @param convertView
     * @param parent
     * @return Un objeto tipo View para ser insertado en el layout respectivo
     *
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater  inflater = LayoutInflater.from(mContext);
        View item = null;

        if (dataset.isEmpty()){
            item = inflater.inflate(R.layout.lista_vaciatop, null);

        }
        else
        {
            item = inflater.inflate(R.layout.item_listatop, null);
            //Log.i("msg","entro al else del getView en ListaAdaptadorVisita");
        }

       /* new DownloadImageTask((ImageView)item.findViewById(R.id.imgFilm))
                    .execute(dataset.get(position).getRuta());*/

        ImageView poster = (ImageView) item.findViewById(R.id.imgFilm);
        Picasso.get().load(dataset.get(position).getRuta()).resize(50, 45).centerCrop().into(poster);

        TextView nombre = (TextView)item.findViewById(R.id.tvNombre);
        nombre.setText(dataset.get(position).getNombre());

        TextView contador = (TextView)item.findViewById(R.id.tvContador);
        contador.setText(dataset.get(position).getContador());

        return  item;

    }

}

