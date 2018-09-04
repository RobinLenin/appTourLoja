package com.lojatour.applojatour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SitiosMasVisitados extends AppCompatActivity {
    //public Button btnMapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        // btnMapa = (Button)findViewById(R.id.btnMapa);
        // btnMapa.setOnClickListener(new View.OnClickListener() {
        /*
            @Override
            public void onClick(View v) {
                irMapa();
            }
        });
    }

    private void irMapa() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
*/
    }
}