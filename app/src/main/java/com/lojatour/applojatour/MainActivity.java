package com.lojatour.applojatour;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lojatour.applojatour.controlador.fragmento.SearchFragment;
import com.lojatour.applojatour.controlador.fragmento.HomeFragment;
import com.lojatour.applojatour.controlador.fragmento.MapaFragment;
import com.lojatour.applojatour.controlador.fragmento.ProfileFragment;
import com.lojatour.applojatour.controlador.utilidades.Utilidades;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView uidTextView;
    //private BottomNavigationView bottomNavigationView;


    public static String TOKEN = "";
    public static String ID_EXTERNAL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        //bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //cargamos el fragmento predeterminado al comenzar.
        //loadFragment(new HomeFragment());
        //getting bottom navigation view and attaching the listener
        //bottomNavigationView = findViewById(R.id.bottomNavigationView);
        //bottomNavigationView.setOnNavigationItemSelectedListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();

            nameTextView.setText(name);
            emailTextView.setText(email);
            uidTextView.setText(uid);
        } else {
            verInicioSesion();
        }


        //if (AccessToken.getCurrentAccessToken() == null) {
        //    irLogin();
        //}
    }

    private void verInicioSesion() {
        if (Utilidades.isEmpty(MainActivity.TOKEN)) {
            irLogin();
        } else {
            nameTextView.setText(TOKEN);
        }
    }

    private void irLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        irLogin();
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
