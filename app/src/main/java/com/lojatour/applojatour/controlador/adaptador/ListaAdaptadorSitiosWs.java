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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;

import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;


public class ListaAdaptadorSitiosWs extends ArrayAdapter<SitioTuristicoWs> {

    private List<SitioTuristicoWs> dataset;
    Context mContext;
    Activity activity;



    public ListaAdaptadorSitiosWs(List<SitioTuristicoWs>data,Context context) {
        super(context, R.layout.item_lista, data);//no funcionaba porque faltaba el parametro data el el super
        this.dataset=data;
        this.mContext=context;
    }

    public ListaAdaptadorSitiosWs(Context context) {
        super(context, R.layout.lista_vacia,new ArrayList<SitioTuristicoWs>());//
        this.dataset=new ArrayList<SitioTuristicoWs>();
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
            item = inflater.inflate(R.layout.lista_vacia, null);
            Log.i("msg","entro al if del getView en ListaAdaptador");
        }
        else
        {
            item = inflater.inflate(R.layout.item_lista, null);
            Log.i("msg","entro al else del getView en ListaAdaptador");
        }

       /* new DownloadImageTask((ImageView)item.findViewById(R.id.imgFilm))
                    .execute(dataset.get(position).getRuta());*/

        ImageView poster = (ImageView) item.findViewById(R.id.imgFilm);
        Picasso.get().load(dataset.get(position).getRuta()).resize(50, 45).centerCrop().into(poster);

        TextView nombre = (TextView)item.findViewById(R.id.tvNombre);
        nombre.setText(dataset.get(position).getNombre());

        TextView tipo = (TextView)item.findViewById(R.id.tvTipo);
        tipo.setText(dataset.get(position).getTipo());


        return  item;
    }


    private  class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage){
            this.bmImage=bmImage;
        }


        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIconll = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIconll = BitmapFactory.decodeStream(in);
            }
            catch (Exception e){
                Log.e("ERROOOR",e.getMessage());
                e.printStackTrace();
            }
            return mIconll;
        }


        //luego de haberse cargado la imagen este fija la imagen
        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }

    }
}

