package com.lojatour.applojatour.controlador.fragmento;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lojatour.applojatour.MainActivity;
import com.lojatour.applojatour.R;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.VolleyProcesadorResultado;
import com.lojatour.applojatour.controlador.ws.VolleyTiposError;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioLoginJson;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioWs;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class ProfileFragment extends Fragment {
<<<<<<< HEAD
=======
    private TextView txtNombre;
    private TextView txtCorreo;
    private TextView txtEdad;
    private ImageView imgPerfil;
    private RequestQueue requestQueue;
>>>>>>> developer

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtNombre = (TextView)view.findViewById(R.id.txtNombre);
        txtCorreo = (TextView)view.findViewById(R.id.txtEmail);
        txtEdad = (TextView)view.findViewById(R.id.txtEdad);


        requestQueue = Volley.newRequestQueue(getContext());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            imgPerfil = (ImageView) view.findViewById(R.id.imgPerfil);
            String nombre = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
            Picasso.get().load(user.getPhotoUrl()).resize(50, 45).centerCrop().into(imgPerfil);

            txtNombre.setText(nombre);
            txtCorreo.setText(email);
        }
        //oyentes();

        return view;


    }
<<<<<<< HEAD
}
=======

    private void oyentes(){

                //HashMap<String, String> mapa = new HashMap<>();
                //mapa.put("external_id", MainActivity.ID_EXTERNAL);
                VolleyPeticion<UsuarioWs> buscar = Conexion.getUsuario(
                        getContext(),
                        MainActivity.ID_EXTERNAL,
                        new Response.Listener<UsuarioWs>() {
                            @Override
                            public void onResponse(UsuarioWs response) {
                                txtNombre.setText(response.nombre);
                                txtCorreo.setText(response.correo);
                                txtEdad.setText(response.edad);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyTiposError errores = VolleyProcesadorResultado.parseErrorResponse(error);
                                Toast.makeText(getContext(), errores.errorMessage,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue.add(buscar);
            }


}
>>>>>>> developer
