package com.lojatour.applojatour;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.lojatour.applojatour.controlador.ws.Conexion;
import com.lojatour.applojatour.controlador.ws.modelo.ResponseWs;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioLoginJson;
import com.lojatour.applojatour.controlador.ws.VolleyPeticion;
import com.lojatour.applojatour.controlador.ws.VolleyProcesadorResultado;
import com.lojatour.applojatour.controlador.ws.VolleyTiposError;
import com.lojatour.applojatour.controlador.ws.modelo.UsuarioWs;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    //Variables de registro de usuario
    public String mensaje;
    public String siglas;

    private LoginButton loginButtonFacebook;
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressBar progressBar;

    //
    private EditText correo;
    private EditText clave;
    private Button btn_login;
    private TextView txt_registrar;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        callbackManager = CallbackManager.Factory.create();
        loginButtonFacebook = (LoginButton) findViewById(R.id.login_buttonFacebook);
        loginButtonFacebook.setReadPermissions("email");

        //
        correo = (EditText) findViewById(R.id.txtUsuario);
        clave = (EditText) findViewById(R.id.txtClave);
        btn_login = (Button) findViewById(R.id.btnLogin);
        txt_registrar = (TextView) findViewById(R.id.txt_registrar);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        txt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muestraDialogo();
            }
        });

        // Callback registration
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    irPantallaPrincipal();
                }
            }
        };
        oyentes();

    }

    private void oyentes(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = correo.getText().toString();
                String password = clave.getText().toString();
                if(user.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.usuarioVacio,
                            Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(password.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.claveVacio,
                            Toast.LENGTH_SHORT).show();
                    return ;
                }
                progressBar.setVisibility(View.VISIBLE);
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
                                MainActivity.ID_EXTERNAL = response.id;
                                Toast.makeText(getApplicationContext(), "Bienvenido " + response.nombre,
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                irPantallaPrincipal();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                VolleyTiposError errores = VolleyProcesadorResultado.parseErrorResponse(error);
                                Toast.makeText(getApplicationContext(), errores.errorMessage,
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                );
                requestQueue.add(inicio);
            }
        });
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

        Button btn_registrar = (Button) mView.findViewById(R.id.dialogButtonRegistrar);
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
        });
    }

    private void registrarUsuario(final UsuarioWs usuario, final Dialog dialogo){

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
                        mensaje = response.mensaje;
                        siglas = response.siglas;
                        System.out.println(mensaje+ siglas);

                        Toast.makeText(getApplicationContext(), "Usuario " +
                                usuario.getNombre() + " creado, Ingrese con su correo y contraseña",
                                Toast.LENGTH_SHORT).show();
                        irPantallaPrincipal();
                        dialogo.dismiss();
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



    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressBar.setVisibility(View.VISIBLE);
        loginButtonFacebook.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
                loginButtonFacebook.setVisibility(View.VISIBLE);
            }
        });
    }

    private void irPantallaPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
