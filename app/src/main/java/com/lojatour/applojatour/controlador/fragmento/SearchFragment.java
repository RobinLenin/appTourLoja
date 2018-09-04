package com.lojatour.applojatour.controlador.fragmento;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lojatour.applojatour.R;
import com.lojatour.applojatour.controlador.adaptador.ListaAdaptadorSitiosWs;
import com.lojatour.applojatour.controlador.utilidades.Utilidades;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;

import java.util.Arrays;


public class SearchFragment extends Fragment {
    private ListaAdaptadorSitiosWs listaAdaptadorWS;
    private ListView listView;
    private RequestQueue requestQueue;//
    private EditText txt_busqueda;
    private Button btn_buscar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search,container,false);//esta linea me permite obtener mi frgmento de pagina

        listView= (ListView)rootView.findViewById(R.id.mi_listaBuscar);//en el main no hubiera ido el rootView

        //listView.setEmptyView(findViewById(R.id.lista_vacia));
        listaAdaptadorWS = new ListaAdaptadorSitiosWs(getActivity());//en el main hubiera ido this
        listView.setAdapter(listaAdaptadorWS);
        //inicializamos a los componentes
        txt_busqueda = (EditText)rootView.findViewById(R.id.txt_buscar);
        //txt_busqueda.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);//linea para arreglar el error de SPAN


        requestQueue = Volley.newRequestQueue(getActivity());//en el main hubiera ido con el this


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //una vez qu se crea la vista se crea la actividad
        super.onActivityCreated(savedInstanceState);
        //aqui inicializamos la base de datos

        btn_buscar = (Button)getView().findViewById(R.id.btn_buscar);

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilidades.isEmpty(txt_busqueda.getText().toString())){

                    Toast toast1 = Toast.makeText(getActivity(),
                            getActivity().getString(R.string.txt_busqueda_vacio),
                            Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast1.show();
                    return;
                }
                // si todoo va bien llamamos a este metodo
                consultarWS(txt_busqueda.getText().toString());
            }
        });
    }


    /**
     * Método para buscar sitios turisticos mediante su nombre y es llamado al dar click en el botn de buscar
     * @param titulo
     */
    private void consultarWS(String titulo){
        //aqui van todos los request
        VolleyPeticion<SitioTuristicoWs[]> sites = Conexion.getSitiosBuscar(
                getActivity(),
                titulo,
                new Response.Listener<SitioTuristicoWs[]>() {
                    @Override
                    public void onResponse(SitioTuristicoWs[] response) {
                        //cuando son de este tipo no va con el this va con el getApplicationContext
                        listaAdaptadorWS = new ListaAdaptadorSitiosWs(Arrays.asList(response),getActivity());//en el main hubiera ido con getApplicationContext()
                        listView.setAdapter(listaAdaptadorWS);
                        System.out.println("Entro al OnResponse del consultarWS");

                        //dialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast1 = Toast.makeText(getActivity(),
                                getActivity().getString(R.string.msg_no_busqueda),
                                Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        toast1.show();

                    }
                }
        );

        requestQueue.add(sites);

    }

}

