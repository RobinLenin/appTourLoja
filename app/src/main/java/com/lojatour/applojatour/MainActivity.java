package com.lojatour.applojatour;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;

    public static String TOKEN = "";
    public static String ID_EXTERNAL = "";

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
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


            //String name = user.getDisplayName();
            //String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();
            //String uid = user.getUid();

            //nameTextView.setText(name);
            //emailTextView.setText(email);
            //uidTextView.setText(uid);
            if(user != null){
                buscarUsuarios();
            }

        } else {
            irLogin();
        }

    }

    private void buscarUsuarios(){
        VolleyPeticion<UsuarioWs[]> lista = Conexion.listarUsuarios(
                getApplicationContext(),
                new Response.Listener<UsuarioWs[]>() {
                    @Override
                    public void onResponse(UsuarioWs[] response) {
                        if(verificarUsuario(Arrays.asList(response))==false){
                            /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UsuarioWs usuario = new UsuarioWs();
                            String clave = generarClave();
                            usuario.setNombre(user.getDisplayName());
                            usuario.setClave(clave);
                            usuario.setCorreo(user.getEmail());
                            usuario.setEdad("18");
                            usuario.setGenero("1");
                            registrarUsuarioFacebook(usuario);*/
                            Toast toast1 = Toast.makeText(getApplicationContext(), "Se ha registrado usuario con Facebook",
                                    Toast.LENGTH_SHORT);
                            toast1.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast1.show();
                        }
                        else{
                            //login("sddasd", "sada");
                            Toast toast1 = Toast.makeText(getApplicationContext(), "Se inicio sesion con Facebook",
                                    Toast.LENGTH_SHORT);
                            toast1.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast1.show();
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
    public boolean verificarUsuario(List<UsuarioWs> data){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean existe = false;
        for(UsuarioWs str : data)
        {
            if(str.getCorreo().equalsIgnoreCase(user.getEmail())){
                existe = true;
            }
        }
        return existe;
    }

    private String generarClave() {
        String matricula = "";
        int a;
        for (int i = 0; i < 7; i++) {
            if (i < 4) {    // 0,1,2,3 posiciones de numeros
                matricula = (int) (Math.random() * 9) + "" + matricula;

            } else {       // 4,5,6 posiciones de letras
                do {
                    a = (int) (Math.random() * 26 + 65);///
                } while (a == 65 || a == 69 || a == 73 || a == 79 || a == 85);

                char letra = (char) a;
                if (i == 4) {
                    matricula = matricula + "-" + letra;
                } else {
                    matricula = matricula + "" + letra;
                }

            }
        }
        return matricula;
    }

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
                        if(mensaje.equalsIgnoreCase("Operacion Exitosa")){
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

    public void aceptar() {
        Toast.makeText(getApplicationContext(), "Se ha creado la cuenta con sus datos de Facebook",
                Toast.LENGTH_SHORT).show();
    }



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
    private void irLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        TOKEN = "";
        ID_EXTERNAL = "";
        irLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
