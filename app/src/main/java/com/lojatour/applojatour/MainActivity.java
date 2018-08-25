package com.lojatour.applojatour;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.lojatour.applojatour.controlador.fragmento.DashboardFragment;
import com.lojatour.applojatour.controlador.fragmento.HomeFragment;
import com.lojatour.applojatour.controlador.fragmento.NotificationsFragment;
import com.lojatour.applojatour.controlador.fragmento.ProfileFragment;
import com.lojatour.applojatour.controlador.utilidades.Utilidades;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView uidTextView;
    private BottomNavigationView bottomNavigationView;
    private TextView infoTextView;


    public static String TOKEN = "";
    public static String ID_EXTERNAL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_principal);

       /* nameTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        uidTextView = (TextView) findViewById(R.id.uidTextView);*/



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);


        loadFragment(new HomeFragment());
        //getting bottom navigation view and attaching the listener
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);



        /* bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()== R.id.homeItem){
                    infoTextView.setText(R.string.inicio);

                } else if (item.getItemId()== R.id.searchItem){
                    infoTextView.setText(R.string.buscar);

                } else if (item.getItemId()== R.id.cameraItem){
                    infoTextView.setText(R.string.camara);

                } else if (item.getItemId()== R.id.favoriteItem){
                    infoTextView.setText(R.string.favorito);
                }

                return true;//hay que poner true caso contrario se quedará seleccionado el primer item
            }
        });*/

        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        }*/


        //if (AccessToken.getCurrentAccessToken() == null) {
        //    irLogin();
        //}
    }

    private void verInicioSesion() {
        if (Utilidades.isEmpty(MainActivity.TOKEN)) {
            irLogin();
        } else {
            //nameTextView.setText(TOKEN);
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

/*
    @Override
    protected void onRestart() {
        super.onRestart();
        videoView.start();
    }*/

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.homeItem:
                fragment = new HomeFragment();
                break;

            case R.id.searchItem:
                fragment = new DashboardFragment();
                break;

            case R.id.cameraItem:
                fragment = new NotificationsFragment();
                break;

            case R.id.favoriteItem:
                fragment = new ProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }
}
