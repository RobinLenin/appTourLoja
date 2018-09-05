package com.lojatour.applojatour.controlador.ws;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.lojatour.applojatour.controlador.ws.modelo.EventoWs;
import com.lojatour.applojatour.controlador.ws.modelo.ImagenWs;
import com.lojatour.applojatour.controlador.ws.modelo.ResponseWs;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioLoginJson;
import com.lojatour.applojatour.controlador.ws.modelo.SitioTuristicoWs;
import com.lojatour.applojatour.controlador.ws.modelo.VisitaWs;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioWs;
import java.util.HashMap;

/**
 * Clase para conectar nuestra aplicacion con el servicio web
 * mediante una url
 */
public class Conexion {

    /**
     * Variable estática que contiene la url del servicio web
     */
    private final static String API_URL = "http://tuor.000webhostapp.com/tour/public/index.php/";

    /**
     * Método estatico para conectar con el end-point iniciarSesion del
     * servicio web
     * @param mapa
     * @param responseListener
     * @param errorListener
     * @return
     */
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


    /**
     * Método estatico para conectar con el end-point registrarUsuario del
     * servicio web
     * @param context
     * @param responseListener
     * @param errorListener
     * @return
     */

    public static VolleyPeticion<ResponseWs> registrarUsuario(
            @NonNull final Context context,
            @NonNull final HashMap mapa,
            @NonNull Response.Listener<ResponseWs> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){
        final String url = API_URL + "usuario/registrar";
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

        request.setResponseClass(ResponseWs.class);
        return request;


    }

    /**
     * Método estatico para conectar con el end-point usuarioBuscar del
     * servicio web
     * @param context
     * @param external_id
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<UsuarioWs> getUsuario(
            @NonNull final Context context,
            @NonNull String external_id,
            @NonNull Response.Listener<UsuarioWs> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){

        final String url = API_URL + "usuario/buscar/"+external_id;
        VolleyPeticion peticion = new VolleyPeticion(context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener);

        peticion.setResponseClass(UsuarioWs.class);
        return peticion;
    }

    /**
     * Método estatico para conectar con el end-point usuarioModificar del
     * servicio web
     * @param context
     * @param external_id
     * @param mapa
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<ResponseWs> modificarUsuario(
            @NonNull final Context context,
            @NonNull String external_id,
            @NonNull final HashMap mapa,
            @NonNull Response.Listener<ResponseWs> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){

        final String url = API_URL + "usuario/modificar/"+external_id;
        VolleyPeticion peticion = new VolleyPeticion(context,
                Request.Method.POST,
                url,
                mapa,
                HashMap.class,
                String.class,
                responseListener,
                errorListener);

        peticion.setResponseClass(ResponseWs.class);
        return peticion;
    }

    /**
     * Método estatico para conectar con el end-point listarUsuarios del
     * servicio web
     * @param context
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<UsuarioWs[]> listarUsuarios(
            @NonNull final Context context,
            @NonNull Response.Listener<UsuarioWs[]> responseListener,
            @NonNull Response.ErrorListener errorListener
    )
    {

        final String url = API_URL + "usuario/listar";

        VolleyPeticion peticion = new VolleyPeticion(context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener);

        peticion.setResponseClass(UsuarioWs[].class);

        return peticion;
    }

    /**
     * Método estatico para conectar con el end-point usuarioLoginCorreo del
     * servicio web con el fin de obtener el token y externak_id del usuario
     * @param context
     * @param mapa
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<UsuarioLoginJson> obtenerTokenExternal(
            @NonNull final Context context,
            @NonNull final HashMap mapa,
            @NonNull Response.Listener<UsuarioLoginJson> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){
        final String url = API_URL + "usuario/loginCorreo";
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

    /**
     * Método estatico para conectar con el end-point listarTodosLosSitios del
     * servicio web
     * @param context
     * @param token
     * @param id
     * @param responseListener
     * @param errorListener
     * @return
     */
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

    /**
     * Método estatico para conectar con el end-point listarAllSites del
     * servicio web
     * @param context
     * @param responseListener
     * @param errorListener
     * @return
     */
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

    /**
     *  Método estatico para conectar con el end-point buscar imagenes del
     * servicio web
     * @param sitio_id
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<ImagenWs> buscarImagenSitio(
            @NonNull final Context context,
            @NonNull String sitio_id,
            @NonNull Response.Listener<ImagenWs> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){
        final String url = API_URL + "imagen/buscar/"+sitio_id;
        VolleyPeticion request = new VolleyPeticion(
                context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener
        );

        request.setResponseClass(ImagenWs.class);

        return request;

    }

    /**
     * Método estatico para conectar con el end-point sitioMasVisitados del
     * servicio web
     * @param context
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<VisitaWs[]> getListaSitiosMasVisitados(
            @NonNull final Context context,
            @NonNull Response.Listener<VisitaWs[]> responseListener,
            @NonNull Response.ErrorListener errorListener
    )
    {

        final String url = API_URL + "visita/listarMasVisitadosTest";

        VolleyPeticion peticion = new VolleyPeticion(context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener);

        peticion.setResponseClass(VisitaWs[].class);

        return peticion;
    }

    /**
     * Método estatico para conectar con el end-point listarSitiosFavoritos del
     * servicio web
     * @param context
     * @param external_id
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<VisitaWs[]> getListaFavoritos(
            @NonNull final Context context,
            @NonNull String external_id,
            @NonNull Response.Listener<VisitaWs[]> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){
        final String url = API_URL + "visita/listarFavoritos/"+external_id;
        VolleyPeticion request = new VolleyPeticion(
                context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener
        );

        request.setResponseClass(VisitaWs[].class);

        return request;

    }

    /**
     *
     * @param context
     * @param external_id
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<VisitaWs[]> getListaLikes(
            @NonNull final Context context,
            @NonNull String external_id,
            @NonNull Response.Listener<VisitaWs[]> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){
        final String url = API_URL + "visita/listarLikes/"+external_id;
        VolleyPeticion request = new VolleyPeticion(
                context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener
        );

        request.setResponseClass(VisitaWs[].class);

        return request;

    }

    /**
     *
     * @param context
     * @param titulo
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<SitioTuristicoWs[]> getSitiosBuscar(
            @NonNull final Context context,
            @NonNull String titulo,
            @NonNull Response.Listener<SitioTuristicoWs[]> responseListener,
            @NonNull Response.ErrorListener errorListener
    )
    {
        titulo = titulo.replace(" ","+");
        final String url = API_URL + "sitio/buscarTest/"+titulo;

        VolleyPeticion peticion = new VolleyPeticion(
                context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener);

        peticion.setResponseClass(SitioTuristicoWs[].class);

        return peticion;


    }

    /**
     *
     * @param context
     * @param mapa
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<VisitaWs> setLikeOrVisita(
            @NonNull final Context context,
            @NonNull final HashMap mapa,
            @NonNull Response.Listener<VisitaWs> responseListener,
            @NonNull Response.ErrorListener errorListener
    ){
        final String url = API_URL + "visita/registrarTest";
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

        request.setResponseClass(VisitaWs.class);
        return request;

    }

    /**
     * Método estatico para conectar con el end-point listarEventos del
     * servicio web
     * @param context
     * @param responseListener
     * @param errorListener
     * @return
     */
    public static VolleyPeticion<EventoWs[]> getListaEventos(
            @NonNull final Context context,
            @NonNull Response.Listener<EventoWs[]> responseListener,
            @NonNull Response.ErrorListener errorListener
    )
    {

        final String url = API_URL + "evento/listar";

        VolleyPeticion peticion = new VolleyPeticion(context,
                Request.Method.GET,
                url,
                responseListener,
                errorListener);

        peticion.setResponseClass(EventoWs[].class);

        return peticion;
    }


}
