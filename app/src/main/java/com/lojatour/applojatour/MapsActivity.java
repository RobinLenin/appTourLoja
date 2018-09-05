package com.lojatour.applojatour;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lojatour.applojatour.controlador.utilidades.DirectionsParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Actividad que permitirá la obtención de la ruta entre el dispositivo y un sitio turistico
 * @author robin
 * @version 1.0
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * Variable utilizada para crear una instancia de googleMap
     */
    private GoogleMap mMap;
    /**
     * Variable utilizada para establecer una solicitud de ubicación
     */
    private static final int LOCATION_REQUEST = 500;
    /**
     * Arreglo de contendra los puntos de origen y destino de tipo LatLng
     */
    ArrayList<LatLng> listPoints;
    /**
     * Variable que almacena el valor de petición de permiso para la localización
     */
    private static int PETICION_PERMISO_LOCALIZACION = 101;
    /**
     * Variable para luego instanciar un objeto de la clase Marker
     */
    private Marker marcador;
    /**
     * Variable tipo double para almacenar latitud de ubicación del dispositivo
     */
    double lat = 0.0;
    /**
     * Variable tipo double para almacenar la longitud de ubicación del dispositivo
     */
    double lng = 0.0;
    /**
     * Variable tipo double que almacenara el valor de la latitud de ubicación de un sitio seleccionado
     */
    double latST = TodosLosSitios.latST;
    /**
     * Variable tipo double que almacenara el valor de la longuitud de ubicación de un sitio seleccionado
     */
    double lngST = TodosLosSitios.lngST;
    /**
     * Variable tipo String que almacenara un mensaje de información del gps del dispositivo
     */
    String mensaje1;
    /**
     * Variable tipo String que almacenará la dirección de la ubicación del dipositivo
     */
    String direccion = "";

    /**
     * Método que ejecutará tareas al iniciar la Actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();

    }


    /**
     * Método que ejecutará tareas cuando se genere el mapa en la actividad
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
        AgregarMarcadorSitio(latST, lngST);
        crearRuta();
    }

    /**
     * Método que volverá al MainActivity al pulsar botón regresar
     */
    @Override
    public void onBackPressed() {
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
        finish();
    }

    /**
     * Método que activa los servicios del Gps cuando estén apagados
     */
    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }

    }

    /**
     * Método que obtendra la dirección de la ubicación del dispositivo
     * @param loc
     */
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion = (DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método quje agrega el marcador destino en el mapa
     * @param latST
     * @param lngST
     */
    private void AgregarMarcadorSitio(double latST, double lngST) {
        LatLng coordenadas = new LatLng(latST, lngST);
        listPoints.add(coordenadas);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coordenadas);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(markerOptions);
    }

    /**
     * Método que agrega el marcador de origen al mapa
     * @param lat
     * @param lng
     */
    private void AgregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        listPoints.add(coordenadas);
        CameraUpdate MiUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Dirección:" + direccion)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera(MiUbicacion);
    }

    /**
     * Método que genera la ruta entre los marcadores de origen y destino
     */
    public void crearRuta(){
        if (listPoints.size() == 2) {
            //Create the URL to get request from first marker to second marker
            String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
        }
    }

    /**
     * Método que permite la actualización de la ubicacion del dispositivo
     * @param location
     */
    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            AgregarMarcador(lat, lng);

        }
    }

    /**
     * Método oyente para el control del gpsp
     */
    LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            ActualizarUbicacion(location);
            setLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            mensaje1 = ("GPS Activado");
            Mensaje();
        }

        @Override
        public void onProviderDisabled(String s) {
            mensaje1 = ("GPS Desactivado");
            locationStart();
            Mensaje();
        }
    };


    /**
     * Método que obtiene la ubicación del dispositivo movil
     */
    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,0,locListener);
        }

    }

    /**
     * Método que genera un Toast para presentar la variable mensaje
     */
    public void Mensaje() {
        Toast toast = Toast.makeText(this, mensaje1, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    /**
     * Método que devuelve un String con la solicitud url
     * @param origin
     * @param dest
     * @return
     */
    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    /**
     * Método que envía una solicitud http get a una url
     * @param reqUrl
     * @return
     * @throws IOException
     */
    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    /**
     * Método que controla el resultado de la solicitud de permisos
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    /**
     * Clase interna para la ejecución de instrucciones
     */
    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    /**
     * Método que convierte los valores obtenidos de la ubicación y
     */

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
