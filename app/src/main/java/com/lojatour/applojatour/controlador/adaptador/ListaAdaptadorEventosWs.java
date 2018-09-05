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
import com.lojatour.applojatour.controlador.ws.modelo.EventoWs;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Clase adaptador para permitir listar los diferentes eventos
 * en el layout item_listaevento
 * @author alexjh
 */
public class ListaAdaptadorEventosWs extends ArrayAdapter<EventoWs> {

    private List<EventoWs> dataset;
    Context mContext;
    Activity activity;



    public ListaAdaptadorEventosWs(List<EventoWs>data, Context context) {
        super(context, R.layout.item_listaevento, data);//no funcionaba porque faltaba el parametro data el el super
        this.dataset=data;
        this.mContext=context;
    }

    public ListaAdaptadorEventosWs(Context context) {
        super(context, R.layout.lista_vaciaevento,new ArrayList<EventoWs>());//
        this.dataset=new ArrayList<EventoWs>();
        this.mContext=context;

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
            item = inflater.inflate(R.layout.lista_vaciaevento, null);
            Log.i("msg","entro al if del getView en ListaAdaptador");
        }
        else
        {
            item = inflater.inflate(R.layout.item_listaevento, null);
            Log.i("msg","entro al else del getView en ListaAdaptador");
        }

        ImageView poster = (ImageView) item.findViewById(R.id.imgEvento);
        Picasso.get().load(dataset.get(position).getRuta()).into(poster);

        TextView nombre = (TextView)item.findViewById(R.id.tvNombre);
        nombre.setText(dataset.get(position).getNombre());

        TextView lugar = (TextView)item.findViewById(R.id.tvLugar);
        lugar.setText(dataset.get(position).getLugar());

        TextView fecha = (TextView)item.findViewById(R.id.tvFecha);
        fecha.setText(dataset.get(position).getFecha());


        return  item;
    }

}

