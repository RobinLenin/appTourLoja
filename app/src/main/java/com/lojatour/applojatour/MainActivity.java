package com.lojatour.applojatour;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lojatour.applojatour.controlador.fragmento.SearchFragment;
import com.lojatour.applojatour.controlador.fragmento.HomeFragment;
import com.lojatour.applojatour.controlador.fragmento.MapaFragment;
import com.lojatour.applojatour.controlador.fragmento.ProfileFragment;
import com.lojatour.applojatour.controlador.utilidades.Utilidades;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.VolleyProcesadorResultado;
import com.lojatour.applojatour.controlador.ws.VolleyTiposError;
import com.lojatour.applojatour.controlador.ws.modelo.ResponseWs;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioLoginJson;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioWs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Actividad principar de la aplicación
 * @author robin
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    /**
     * Varibles estaticas para el control del usuario logueado
     */
    public static String TOKEN = "";
    public static String ID_EXTERNAL = "";

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        cargarPreferencias();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_actionloja_round);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null || !(Utilidades.isEmpty(MainActivity.TOKEN))) {

            //cargamos el fragmento predeterminado al comenzar.
            loadFragment(new HomeFragment());
            //getting bottom navigation view and attaching the listener
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);

            if(user != null){
                buscarUsuarios();
            }

        } else {
            irLogin();
        }

    }

    /**
     * Método utilizado para leer datos guardados en la memoria del telefono utilizando SharedPreferences
     */
    private void cargarPreferencias() {
        SharedPreferences sharedPreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        TOKEN = sharedPreferences.getString("token", "");
        ID_EXTERNAL = sharedPreferences.getString("idExternal", "");
        System.out.println("LLaves: " + TOKEN);
    }

    /**
     * Método que realiza una solicitud al servicio para listar los usuarios
     */
    private void buscarUsuarios(){
        VolleyPeticion<UsuarioWs[]> lista = Conexion.listarUsuarios(
                getApplicationContext(),
                new Response.Listener<UsuarioWs[]>() {
                    @Override
                    public void onResponse(UsuarioWs[] response) {
                        if(verificarUsuario(Arrays.asList(response))==false){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UsuarioWs usuario = new UsuarioWs();
                            usuario.setNombre(user.getDisplayName());
                            usuario.setClave("usuario");
                            usuario.setCorreo(user.getEmail());
                            usuario.setEdad("18");
                            usuario.setGenero("1");
                            registrarUsuarioFacebook(usuario);
                        }

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

        requestQueue.add(lista);

    }

    /**
     * Método que comprueba si el usuario ya esta registrado con facebook
     * @param data
     * @return
     */
    public boolean verificarUsuario(List<UsuarioWs> data){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean existe = false;
        for(UsuarioWs str : data)
        {
            if(str.getCorreo().equalsIgnoreCase(user.getEmail())){
                loginCorreoFacebook(str.getCorreo());
                existe = true;

            }
        }
        return existe;
    }

    /**
     * Método para registrar usuario con datos de facebook
     * @param usuario
     */
    private void registrarUsuarioFacebook(final UsuarioWs usuario){

        HashMap<String, String> mapa = new HashMap<>();
        mapa.put("nombre", usuario.getNombre());
        mapa.put("clave", usuario.getClave());
        mapa.put("correo", usuario.getCorreo());
        mapa.put("edad", usuario.getEdad());
        mapa.put("genero", usuario.getGenero());
        VolleyPeticion<ResponseWs> registro = Conexion.registrarUsuario(
                getApplicationContext(),
                mapa,
                new Response.Listener<ResponseWs>() {
                    @Override
                    public void onResponse(ResponseWs response) {
                        String mensaje = response.mensaje;
                        if(mensaje.equalsIgnoreCase("Operacion existosa al registrar el Usuario")){
                            login(usuario.getNombre(), usuario.getClave());
                            crearDialogo(usuario.getClave());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyTiposError errores = VolleyProcesadorResultado.parseErrorResponse(error);
                        Toast.makeText(getApplicationContext(), errores.errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(registro);
    }

    /**
     * Método que genera dialogo que muestra contraseña por defecto del usuario creado con datos de facebook
     * @param clave
     */
    private void crearDialogo(String clave) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Importante");
        dialogo.setMessage("Su clave de ingreso a la app es: " + clave);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                aceptar();
            }
        });
        dialogo.show();
    }

    /**
     * Método que muestra un Toast con información
     */
    public void aceptar() {
        Toast.makeText(getApplicationContext(), "Se ha creado la cuenta con sus datos de Facebook",
                Toast.LENGTH_SHORT).show();
    }


    /**
     * Método que loguea al usuario creado con datos de facebook
     * @param user
     * @param password
     */
    private void login(String user, String password) {
        HashMap<String, String> mapa = new HashMap<>();
        mapa.put("correo", user);
        mapa.put("clave", password);
        VolleyPeticion<UsuarioLoginJson> inicio = Conexion.iniciarSesion(
                getApplicationContext(),
                mapa,
                new Response.Listener<UsuarioLoginJson>() {
                    @Override
                    public void onResponse(UsuarioLoginJson response) {
                        guardarCredenciales(response.token, response.external_id);
                        MainActivity.TOKEN = response.token;
                        MainActivity.ID_EXTERNAL = response.external_id;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        VolleyTiposError errores = VolleyProcesadorResultado.parseErrorResponse(error);
                        Toast.makeText(getApplicationContext(), errores.errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(inicio);
    }
    /**
     * Método utilizado para guardar datos en la memoria del telefono utilizando SharedPreferences
     * @param token
     * @param idExternal
     */
    public void guardarCredenciales(String token, String idExternal){
        SharedPreferences sharedPreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("idExternal", idExternal);
        editor.commit();
    }

    /**
     * Método que crea una petición al servidor que devuelva token y external_id
     * @param correo
     */
    private void loginCorreoFacebook(String correo) {
        HashMap<String, String> mapa = new HashMap<>();
        mapa.put("correo", correo);
        VolleyPeticion<UsuarioLoginJson> inicio = Conexion.obtenerTokenExternal(
                getApplicationContext(),
                mapa,
                new Response.Listener<UsuarioLoginJson>() {
                    @Override
                    public void onResponse(UsuarioLoginJson response) {
                        guardarCredenciales(response.token, response.external_id);
                        MainActivity.TOKEN = response.token;
                        MainActivity.ID_EXTERNAL = response.external_id;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        VolleyTiposError errores = VolleyProcesadorResultado.parseErrorResponse(error);
                        Toast.makeText(getApplicationContext(), errores.errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(inicio);
    }

    /**
     * Método utilizado para ir al LoginActivity
      */

    private void irLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Método para cerrar sesión tando de facebook como de la app
     */

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        TOKEN = "";
        ID_EXTERNAL = "";
        SharedPreferences sharedPreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("token", "");
        editor.putString("idExternal", "");
        editor.commit();
        irLogin();
    }

    /**
     * Método que controla las opciones del menú
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Método que controla el layout main
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cerrarSesion) {
            logOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Método para cambiar el fragmento
     * @param fragment
     * @return
     */
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Este metod se llamará cada vez que toquemos una opción desde el buttonNavigationView
     // y es aqui donde cambiaremos los fragmentos
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.homeItem:
                fragment = new HomeFragment();
                break;

            case R.id.searchItem:
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Ha llamado al SearchFragment desde MainActivity",
                        Toast.LENGTH_SHORT);
                toast1.show();
                fragment = new SearchFragment();
                break;

            case R.id.mapaItem:
                fragment = new MapaFragment();
                break;

            case R.id.profileItem:
                fragment = new ProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }
}
