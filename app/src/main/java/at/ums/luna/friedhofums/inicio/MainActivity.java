package at.ums.luna.friedhofums.inicio;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.maps.model.LatLng;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.actividades.ListadoTumbas;
import at.ums.luna.friedhofums.actividades.MiPosicion;
import at.ums.luna.friedhofums.actividades.Preferencias;
import at.ums.luna.friedhofums.servidor.Defaults;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private String idTrabajadorActual;
    private String nombreTrabajadorActual;
    private String userActual;
    private String passActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        toolbar.setTitle(String.format(this.getString(R.string.Trabajador), nombreTrabajadorActual));

        //comprobacion si no esta configurado el trabajador
        if (idTrabajadorActual.equals("ßß")){
            Intent intento = new Intent(this, Preferencias.class);
            startActivity(intento);

        }


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void setToolbar(){
        //añadir la Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }


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
