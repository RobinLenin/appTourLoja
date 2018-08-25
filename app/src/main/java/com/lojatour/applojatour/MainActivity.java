package com.lojatour.applojatour;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.lojatour.applojatour.controlador.utilidades.Utilidades;

public class MainActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView uidTextView;
    private BottomNavigationView bottomNavigationView;
    private TextView infoTextView;
    private VideoView videoView;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private LinearLayout layout002;//aqui estan los 4 cardViews
    private Intent intent;

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

        layout002 = (LinearLayout) findViewById(R.id.layout002);
        cardView1 = (CardView) findViewById(R.id.card1);
        cardView2 = (CardView) findViewById(R.id.card2);
        cardView3 = (CardView) findViewById(R.id.card3);
        cardView4 = (CardView) findViewById(R.id.card4);

        intent = new Intent(this,SitiosMasVisitados.class);


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "has dado click card", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "has dado click card", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "has dado click card", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "has dado click card", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        videoView = (VideoView) findViewById(R.id.video);

        String path = "android.resource://" + getPackageName()
                + "/" + R.raw.lojavideo;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
                mp.setLooping(true);
            }
        });
        videoView.start();


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


    @Override
    protected void onRestart() {
        super.onRestart();
        videoView.start();
    }
}
