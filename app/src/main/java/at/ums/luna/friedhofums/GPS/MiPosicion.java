package at.ums.luna.friedhofums.GPS;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.maps.model.LatLng;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

public class MiPosicion extends Activity {

    TextView tvIdGrab;
    TextView tvGrabname;
    TextView tvGrabart;
    TextView tvFriedhof;
    TextView tvFeld;
    TextView tvReihe;
    TextView tvNummer;
    TextView tvKunde;
    TextView tvTelefon1;
    TextView tvTelefon2;
    TextView tvBemerkung;
    Button botonGuardar;

    double miLatitud;
    double miLongitud;

    Grab tumbaObtenida;

    Localizacion Local;

    LocationManager mlocManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_posicion);

        tvIdGrab = (TextView) findViewById(R.id.tvIdGrab);
        tvGrabname = (TextView) findViewById(R.id.tvGrabname);
        tvGrabart = (TextView) findViewById(R.id.tvGrabart);
        tvFriedhof = (TextView) findViewById(R.id.tvFriedhof);
        tvFeld = (TextView) findViewById(R.id.tvFeld);
        tvReihe = (TextView) findViewById(R.id.tvReihe);
        tvNummer = (TextView) findViewById(R.id.tvNummer);
        tvKunde = (TextView) findViewById(R.id.tvKunde);
        tvTelefon1 = (TextView) findViewById(R.id.tvTelefon1);
        tvTelefon2 = (TextView) findViewById(R.id.tvTelefon2);
        tvBemerkung = (TextView) findViewById(R.id.tvBemerkung);
        botonGuardar = (Button) findViewById(R.id.botonGuaardaPosicion);

        obtenerTumba();

        busca();


    }

    private void busca() {
    /* Uso de la clase LocationManager para obtener la localizacion del GPS */
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Local = new Localizacion();
        Local.setMiPosicion(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                Local);

    }


    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MiPosicion miPosicion;

        public MiPosicion getMiPosicion() {
            return miPosicion;
        }

        public void setMiPosicion(MiPosicion miPosicion) {
            this.miPosicion = miPosicion;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            miLatitud = loc.getLatitude();
            miLongitud = loc.getLongitude();
            botonGuardar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado

        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
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


    @Override
    protected void onPause() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocManager.removeUpdates(Local);
        super.onPause();
    }

    public void guardaCoordenadas(View v){

                tumbaObtenida.setLatitud(miLatitud);
                tumbaObtenida.setLongitud(miLongitud);

                Backendless.Persistence.save(tumbaObtenida, new AsyncCallback<Grab>() {
                    @Override
                    public void handleResponse(Grab grab) {
                        Log.i("MENSAJES", String.format("La tumba %s tiene la posicion %s - %s",
                                tumbaObtenida.getIdGrab(), tumbaObtenida.getLatitud(),tumbaObtenida.getLongitud()));

                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });

        OperacionesBaseDatos db = new OperacionesBaseDatos(this);
        db.actualizaCoordenadasTumba(tumbaObtenida.getIdGrab(),miLatitud,miLongitud);

        Toast.makeText(this,"Grab gespeichert",Toast.LENGTH_SHORT).show();

    }


    public void obtenerTumba(){

        String codigoObtenido =  getIntent().getStringExtra("idGrab");
        String whereClause = "idGrab = '" + codigoObtenido + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Backendless.Persistence.of(Grab.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Grab>>() {
            @Override
            public void handleResponse(BackendlessCollection<Grab> grabBackendlessCollection) {
                tumbaObtenida = grabBackendlessCollection.getCurrentPage().get(0);

                tvIdGrab.setText(tumbaObtenida.getIdGrab());
                tvGrabname.setText(tumbaObtenida.getGrabname());
                tvGrabart.setText(tumbaObtenida.getGrabart());
                tvFriedhof.setText(tumbaObtenida.getFriedhof());
                tvFeld.setText(tumbaObtenida.getFeld());
                tvReihe.setText(tumbaObtenida.getReihe());
                tvNummer.setText(tumbaObtenida.getNummer());
                tvKunde.setText(tumbaObtenida.getKunde());
                tvTelefon1.setText(tumbaObtenida.getTelefon1());
                tvTelefon2.setText(tumbaObtenida.getTelefon2());
                tvBemerkung.setText(tumbaObtenida.getBemerkung());

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });
    }

}
