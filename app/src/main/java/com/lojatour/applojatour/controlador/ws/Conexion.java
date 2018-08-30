package com.lojatour.applojatour.controlador.ws;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioLoginJson;
import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;

import java.util.HashMap;

public class Conexion {

    private final static String API_URL = "http://tuor.000webhostapp.com/tour/public/index.php/";

    public static VolleyPeticion<UsuarioLoginJson> iniciarSesion(
            @NonNull final Context context,
            @NonNull final HashMap mapa,
            @NonNull Response.Listener<UsuarioLoginJson> responseListener,
            @NonNull Response.ErrorListener errorListener
            ){
        final String url = API_URL + "usuario/login";
        VolleyPeticion request = new VolleyPeticion(
                context,
                Request.Method.POST,
                url,
                mapa,
                HashMap.class,
                String.class,
                responseListener,
                errorListener
        );

        request.setResponseClass(UsuarioLoginJson.class);
        return request;


    }


    public static VolleyPeticion<SitioTuristicoWs[]>listarSitiosAll(
            @NonNull final Context context,
            @NonNull final String token,
            @NonNull final String id,
            @NonNull Response.Listener<SitioTuristicoWs[]> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){
        //aqui ya no se usa el hashmap
        final String url =API_URL + "sitio/listar";

        VolleyPeticion request = new VolleyPeticion(
                context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener
        );
        request.setResponseClass(SitioTuristicoWs[].class);

        try {
            request.getHeaders().put("Api-Token",token);

        }catch (Exception ex){
            Log.e("Error de listar",ex.toString());

        }

        return request;
    }

    public static VolleyPeticion<SitioTuristicoWs[]> getListaSitios(
            @NonNull final Context context,
            @NonNull Response.Listener<SitioTuristicoWs[]> responseListener,
            @NonNull Response.ErrorListener errorListener
    )
    {

        final String url = API_URL + "sitio/listar";

        VolleyPeticion peticion = new VolleyPeticion(context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener);

        peticion.setResponseClass(SitioTuristicoWs[].class);

        return peticion;
    }
}
