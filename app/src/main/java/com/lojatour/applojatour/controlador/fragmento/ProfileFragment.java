package com.lojatour.applojatour.controlador.fragmento;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lojatour.applojatour.LoginActivity;
import com.lojatour.applojatour.MainActivity;
import com.lojatour.applojatour.R;
import com.lojatour.applojatour.SitiosFavoritosActivity;
import com.lojatour.applojatour.SitiosLikesActivity;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.VolleyProcesadorResultado;
import com.lojatour.applojatour.controlador.ws.VolleyTiposError;
import com.lojatour.applojatour.controlador.ws.modelo.ResponseWs;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioLoginJson;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioWs;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    private TextView txtNombre;
    private TextView txtCorreo;
    private TextView txtEdad;
    private ImageView imgPerfil;
    private RequestQueue requestQueue;
    private Button btnEditarPerfil;

    private LinearLayout txtLikes;
    private LinearLayout txtFavoritos;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtNombre = (TextView)view.findViewById(R.id.txtNombre);
        txtCorreo = (TextView)view.findViewById(R.id.txtEmail);
        txtEdad = (TextView)view.findViewById(R.id.txtEdad);
        btnEditarPerfil = (Button) view.findViewById(R.id.btnEditarPerfil);

        txtLikes = (LinearLayout) view.findViewById(R.id.txtLikes);
        txtFavoritos = (LinearLayout) view.findViewById(R.id.txtFavoritos);
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muestraDialogoModificar();
            }
        });

        txtLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irSitiosLikes();
            }
        });
        txtFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irSitiosFavoritos();
            }
        });

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
        else{
            oyentes();
        }
        return view;

    }

    private void irSitiosLikes() {
        Intent intent = new Intent(getContext(), SitiosLikesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void irSitiosFavoritos() {
        Intent intent = new Intent(getContext(), SitiosFavoritosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void muestraDialogoModificar() {
        requestQueue = Volley.newRequestQueue(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_registrar, null);


        final EditText edit_Usuario = (EditText) mView.findViewById(R.id.edit_Usuario);
        final EditText edit_Clave = (EditText) mView.findViewById(R.id.edit_Clave);
        final EditText edit_RClave = (EditText) mView.findViewById(R.id.edit_RClave);
        final EditText edit_Email = (EditText) mView.findViewById(R.id.edit_Email);
        final EditText edit_Edad = (EditText) mView.findViewById(R.id.edit_Edad);
        edit_Edad.setEnabled(false);

        builder.setView(mView);
        final AlertDialog alert = builder.create();
        alert.show();

        buscarDatosUsuario(alert);


        Button btn_cancelar = (Button) mView.findViewById(R.id.dialogButtonCancelar);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        Button btn_registrar = (Button) mView.findViewById(R.id.dialogButtonRegistrar);
        btn_registrar.setText("Modificar");
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioWs usuario = new UsuarioWs();

                if(edit_Clave.getText().toString().equalsIgnoreCase(edit_RClave.getText().toString())){

                    usuario.setNombre(edit_Usuario.getText().toString());
                    usuario.setClave(edit_Clave.getText().toString());
                    usuario.setCorreo(edit_Email.getText().toString());
                    System.out.println("Usuario Editado" + edit_Usuario.getText().toString());
                    //usuario.setEdad(edit_Edad.getText().toString());
                    //usuario.setGenero("0");
                }else {
                    Toast.makeText(getContext(), "Las contraseñas no coinsiden",
                            Toast.LENGTH_SHORT).show();
                }
                if(usuario != null){
                    modificarUsuario(usuario, alert);
                }else {
                    Toast.makeText(getContext(), "Existen errores en los datos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buscarDatosUsuario(final Dialog dialog){

        VolleyPeticion<UsuarioWs> buscar = Conexion.getUsuario(
                getContext(),
                MainActivity.ID_EXTERNAL,
                new Response.Listener<UsuarioWs>() {
                    @Override
                    public void onResponse(UsuarioWs response) {
                        final EditText edit_Usuario = (EditText) dialog.findViewById(R.id.edit_Usuario);
                        final EditText edit_Clave = (EditText) dialog.findViewById(R.id.edit_Clave);
                        final EditText edit_RClave = (EditText) dialog.findViewById(R.id.edit_RClave);
                        final EditText edit_Email = (EditText) dialog.findViewById(R.id.edit_Email);
                        edit_Usuario.setText(response.nombre);
                        edit_Email.setText(response.correo);
                        edit_Clave.setText(response.clave);
                        edit_RClave.setText(response.clave);
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



    private void modificarUsuario(UsuarioWs usuario, final Dialog dialog) {
        HashMap<String, String> mapa = new HashMap<>();
        mapa.put("nombre", usuario.getNombre());
        mapa.put("clave", usuario.getClave());
        mapa.put("correo", usuario.getCorreo());
        VolleyPeticion<ResponseWs> modificar = Conexion.modificarUsuario(
                getContext(),
                MainActivity.ID_EXTERNAL,
                mapa,
                new Response.Listener<ResponseWs>() {
                    @Override
                    public void onResponse(ResponseWs response) {
                        Toast.makeText(getContext(), response.mensaje,
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyTiposError errores = VolleyProcesadorResultado.parseErrorResponse(error);
                        Toast.makeText(getContext(), errores.errorMessage,
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
        );
        requestQueue.add(modificar);
    }

    private void oyentes(){
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

