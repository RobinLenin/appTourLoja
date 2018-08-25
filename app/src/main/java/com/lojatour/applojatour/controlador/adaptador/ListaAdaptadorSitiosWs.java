package com.lojatour.applojatour.controlador.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.lojatour.applojatour.R;
import java.util.List;
import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;

import java.util.List;

public class ListaAdaptadorSitiosWs extends ArrayAdapter<SitioTuristicoWs> {

    private List<SitioTuristicoWs> dataset;
    private Context mContext;


    public ListaAdaptadorSitiosWs(List<SitioTuristicoWs>data,Context context) {
        super(context, R.layout.activity_login);//item_lista.xnl
        this.dataset=data;
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
       /* View item = inflater.inflate(R.layout.item_lista,null);

        TextView titulo=(TextView)item.findViewById(R.id.tvTitulo);
        TextView contenido=(TextView)item.findViewById(R.id.tvDescripcion);
        TextView fecha=(TextView)item.findViewById(R.id.tvYear);

        SitioTuristicoWs sitiosWs =dataset.get(position);

        titulo.setText(sitiosWs.nombre);
        contenido.setText(sitiosWs.descripcion);
        fecha.setText(sitiosWs.horarios);
        return item;*/
       return null;
    }
}

