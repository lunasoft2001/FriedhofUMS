package at.ums.luna.friedhofums.inicio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import at.ums.luna.friedhofums.servidor.DBHelper;
import at.ums.luna.friedhofums.servidor.Defaults;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

public class MainActivity extends AppCompatActivity {


    private OperacionesBaseDatos operacionesDB;

    private Toolbar toolbar;
    private String idTrabajadorActual;
    private String nombreTrabajadorActual;
    private String userActual;
    private String passActual;

    TextView tv1;
    TextView tv2;

    LocationManager mlocManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);


        /**
         * Declaramos el controlador de la BD y accedemos en modo escritura
         * Para la base de datos creada desde java
         */
        DBHelper bdHelper = new DBHelper(getBaseContext());
        //SQLiteDatabase db = bdHelper.getWritableDatabase();


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
        intento.putExtra("modo",1);
        startActivity(intento);
    }


    public void actualizarTablaGrab(View v){
        // Llama a Backendless y actualiza la tabla interna
        operacionesDB = new OperacionesBaseDatos(this);
        operacionesDB.actualizarGrab(this);


    }

    public void mostrarGeoCategorias(View v){
        Intent intento = new Intent(this,ListadoTumbas.class);
        intento.putExtra("modo",2);
        startActivity(intento);

    }

}
