package at.ums.luna.friedhofums.inicio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.actividades.ListadoTumbas;
import at.ums.luna.friedhofums.GPS.MiPosicion;
import at.ums.luna.friedhofums.actividades.Preferencias;
import at.ums.luna.friedhofums.servidor.Defaults;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private String idTrabajadorActual;
    private String nombreTrabajadorActual;
    private String userActual;
    private String passActual;

    double miLatitud;
    double miLongitud;
    Localizacion Local;

    TextView tv1;
    TextView tv2;

    LocationManager mlocManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);



        //Cargar settings por defecto
        PreferenceManager.setDefaultValues(this,R.xml.settings,false);

        //activamos la toolbar
        setToolbar();


        //conectamos con el servidor
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        //activamos el servicio PUSH de backendless

        Backendless.Messaging.registerDevice("545190333328", "default", new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(MainActivity.this, backendlessFault.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();


        //rellenamos trabajadorActual
        mostrarPreferencias();

        //activa el GPS
        busca();


        toolbar.setTitle(String.format(this.getString(R.string.Trabajador), nombreTrabajadorActual));

        //comprobacion si no esta configurado el trabajador
        if (idTrabajadorActual.equals("ßß")){
            Intent intento = new Intent(this, Preferencias.class);
            startActivity(intento);

        }


    }

    @Override
    protected void onPause() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocManager.removeUpdates(Local);
        Log.i("MENSAJES", "PARANDO GPS");
        super.onPause();
    }

    private void setToolbar(){
        //añadir la Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    /*
    COMIENZA EL BLOQUE DE GPS
     */

    private void busca() {
    /* Uso de la clase LocationManager para obtener la localizacion del GPS */
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Local = new Localizacion();
        Local.setMiPosicion(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                Local);

        tv1.setText("Localizacion agregada");
        tv2.setText("");
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMiPosicion() {
            return mainActivity;
        }

        public void setMiPosicion(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();
            String Text = "Lat = " + loc.getLatitude() + " Long = " + loc.getLongitude();
            tv1.setText(Text);
            miLatitud = loc.getLatitude();
            miLongitud = loc.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            tv1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            tv1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Este metodo se ejecuta cada vez que se detecta un cambio en el
            // status del proveedor de localizacion (GPS)
            // Los diferentes Status son:
            // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
            // TEMPORARILY_UNAVAILABLE -> Temporalmente no disponible pero se
            // espera que este disponible en breve
            // AVAILABLE -> Disponible
        }

    }/* Fin de la clase localizacion */









   /*
    FINALIZA EL BLOQUE DE GPS
     */




    public void mostrarPreferencias(){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);

        idTrabajadorActual = pref.getString("prefijo","?");
        nombreTrabajadorActual = pref.getString("nombre","?");
        userActual = pref.getString("usuario","?");
        passActual = pref.getString("password","?");

    }

    public void abrirListadoTumbas(View v){
        Intent intento = new Intent(this,ListadoTumbas.class);
        intento.putExtra("miLatitud",miLatitud);
        intento.putExtra("miLongitud", miLongitud);
        startActivity(intento);
    }

    public void mostrarGeoCategorias(View v){

    Intent intento = new Intent(this, MiPosicion.class);
        startActivity(intento);

//          Ver geoCategoria
// Backendless.Geo.getCategories(new AsyncCallback<List<GeoCategory>>() {
//            @Override
//            public void handleResponse(List<GeoCategory> geoCategories) {
//                Log.i("MENSAJES", "Listado GEO " + geoCategories.get(0).getName());
//            }
//
//            @Override
//            public void handleFault(BackendlessFault backendlessFault) {
//
//            }
//        });

//        AGREGAR geoPunto
//        List<String> categoria = new ArrayList<>();
//                categoria.add("Grab");
//        Map<String, Object> meta = new HashMap<String, Object>();
//        meta.put("name", "Lieboch1");
//
//        Backendless.Geo.savePoint(46.960998, 15.346689, categoria, meta, new AsyncCallback<GeoPoint>() {
//            @Override
//            public void handleResponse(GeoPoint geoPoint) {
//                Log.i("MENSAJES" , geoPoint.getObjectId());
//
//            }
//
//            @Override
//            public void handleFault(BackendlessFault backendlessFault) {
//
//            }
//        });

//        Buscar en Radius
//        BackendlessGeoQuery query = new BackendlessGeoQuery(46.961036, 15.347048, 5, Units.METERS );
//        query.addCategory("Grab");
//
//        Backendless.Geo.getPoints(query, new AsyncCallback<BackendlessCollection<GeoPoint>>() {
//            @Override
//            public void handleResponse(BackendlessCollection<GeoPoint> geoPointBackendlessCollection) {
//                Log.i("MENSAJES", String.format( "Busqueda en radio: %s", geoPointBackendlessCollection.getCurrentPage()));
//            }
//
//            @Override
//            public void handleFault(BackendlessFault backendlessFault) {
//
//            }
//        });



    }

}
