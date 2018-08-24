package com.lojatour.applojatour.controlador.ws;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lojatour.applojatour.controlador.ws.Modelos.UsuarioLoginJson;

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
}
