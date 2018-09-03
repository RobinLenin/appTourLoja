package com.lojatour.applojatour;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lojatour.applojatour.controlador.fragmento.SearchFragment;
import com.lojatour.applojatour.controlador.fragmento.HomeFragment;
import com.lojatour.applojatour.controlador.fragmento.MapaFragment;
import com.lojatour.applojatour.controlador.fragmento.ProfileFragment;
import com.lojatour.applojatour.controlador.utilidades.Utilidades;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioWs;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    //private TextView nameTextView;
    //private TextView emailTextView;
    //private TextView uidTextView;
    private BottomNavigationView bottomNavigationView;


    public static String TOKEN = "";
    public static String ID_EXTERNAL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null || !(Utilidades.isEmpty(MainActivity.TOKEN))) {


            //String name = user.getDisplayName();
            //String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();
            //String uid = user.getUid();

            //nameTextView.setText(name);
            //emailTextView.setText(email);
            //uidTextView.setText(uid);

            //cargamos el fragmento predeterminado al comenzar.
            loadFragment(new HomeFragment());
            //getting bottom navigation view and attaching the listener
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);


        } else {
            irLogin();
        }


        //if (AccessToken.getCurrentAccessToken() == null) {
        //    irLogin();
        //}
    }
/*
    private void verInicioSesion() {
        if (Utilidades.isEmpty(MainActivity.TOKEN)) {
            irLogin();
        } else {
            //nameTextView.setText(TOKEN);
        }
    }
*/
    private void irLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
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
        if (id == R.id.item_editPerfil) {
            muestraDialogo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void muestraDialogo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_registrar, null);


        final EditText edit_Usuario = (EditText) mView.findViewById(R.id.edit_Usuario);
        final EditText edit_Clave = (EditText) mView.findViewById(R.id.edit_Clave);
        final EditText edit_RClave = (EditText) mView.findViewById(R.id.edit_RClave);
        final EditText edit_Email = (EditText) mView.findViewById(R.id.edit_Email);
        final EditText edit_Edad = (EditText) mView.findViewById(R.id.edit_Edad);


        builder.setView(mView);
        final AlertDialog alert = builder.create();
        alert.show();

        Button btn_cancelar = (Button) mView.findViewById(R.id.dialogButtonCancelar);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        /*Button btn_registrar = (Button) mView.findViewById(R.id.dialogButtonRegistrar);
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioWs usuario = new UsuarioWs();
                if(edit_Clave.getText().toString().equalsIgnoreCase(edit_RClave.getText().toString())){

                    usuario.setNombre(edit_Usuario.getText().toString());
                    usuario.setClave(edit_Clave.getText().toString());
                    usuario.setCorreo(edit_Email.getText().toString());
                    usuario.setEdad(edit_Edad.getText().toString());
                    usuario.setGenero("0");
                }else {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinsiden",
                            Toast.LENGTH_SHORT).show();
                }
                if(usuario != null){
                    registrarUsuario(usuario, alert);
                }else {
                    Toast.makeText(getApplicationContext(), "Existen errores en los datos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });*/
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
                        "Has llamado al SearchFragment desde el MainActivity",
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
