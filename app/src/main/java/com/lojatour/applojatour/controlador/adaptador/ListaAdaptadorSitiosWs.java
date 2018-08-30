package com.lojatour.applojatour.controlador.adaptador;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;

import java.util.List;

public class ListaAdaptadorSitiosWs extends ArrayAdapter<SitioTuristicoWs> {

    private List<SitioTuristicoWs> dataset;
    Context mContext;
    Activity activity;


    public ListaAdaptadorSitiosWs(List<SitioTuristicoWs>data,Context context) {
        super(context, R.layout.item_lista);//item_lista.xnl
        this.dataset=data;
        this.mContext=context;
        Log.i("msg","entro al constructor1 ListaAdaptador");
    }
    public ListaAdaptadorSitiosWs(Context context) {
        super(context,R.layout.lista_vacia, new ArrayList<SitioTuristicoWs>());
        this.dataset = new ArrayList<SitioTuristicoWs>();
        this.mContext = context;

    }
    static class ViewHolder {
        protected TextView tvNombre;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.i("Entro al getView","Lista Adaptador");
        LayoutInflater  inflater = LayoutInflater.from(mContext);

        View item = null;

        if (dataset.isEmpty()){
            item = inflater.inflate(R.layout.lista_vacia, null);
            Log.i("msg","entro al if de ListaAdaptador");
        }
        else
        {
            item = inflater.inflate(R.layout.item_lista, null);
            Log.i("msg","entro al else de ListaAdaptador");
        }

        /*//En cada item del listView se mostraran la imagen...
        if (!dataset.get(position).getPoster().equals("N/A")){
            new DownloadImageTask((ImageView)item.findViewById(R.id.imgFilm))
                    .execute(dataset.get(position).getPoster());
        }*/

        TextView nombre = (TextView)item.findViewById(R.id.tvNombre);
        nombre.setText(dataset.get(position).getNombre());

        TextView descrip = (TextView)item.findViewById(R.id.tvDescripcion);
        descrip.setText(dataset.get(position).getDescripcion());
        ///////////
        final ViewHolder viewHolder = new ViewHolder();

        // *** instanciamos a los recursos
        viewHolder.tvNombre = (TextView)item.findViewById(R.id.tvNombre);

        // importante!!! establecemos el mensaje
        viewHolder.tvNombre.setText(dataset.get(position).getNombre());

        return  item;

    }
}

