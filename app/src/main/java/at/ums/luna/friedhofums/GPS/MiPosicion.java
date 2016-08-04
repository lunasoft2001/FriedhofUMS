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
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.maps.model.LatLng;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;

public class MiPosicion extends Activity {

    TextView mensaje1;
    TextView mensaje2;

    double miLatitud;
    double miLongitud;

    Localizacion Local;

    LocationManager mlocManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_posicion);

        mensaje1 = (TextView) findViewById(R.id.mensaje_id);
        mensaje2 = (TextView) findViewById(R.id.mensaje_id2);
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

        mensaje1.setText("Localizacion agregada");
        mensaje2.setText("");
    }


    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    mensaje2.setText("Mi direccion es: \n"
                            + DirCalle.getAddressLine(0));

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            mensaje1.setText(Text);

            miLatitud = loc.getLatitude();
            miLongitud = loc.getLongitude();

            this.miPosicion.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            mensaje1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            mensaje1.setText("GPS Activado");
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

        String codigoObtenido =  getIntent().getStringExtra("idGrab");
        String whereClause = "idGrab = '" + codigoObtenido + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Backendless.Persistence.of(Grab.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Grab>>() {
            @Override
            public void handleResponse(BackendlessCollection<Grab> grabBackendlessCollection) {
                final Grab tumbaObtenida = grabBackendlessCollection.getCurrentPage().get(0);

                Log.i("MENSAJES", tumbaObtenida.getIdGrab() + " " + tumbaObtenida.getGrabname());

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

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });


    }

}
