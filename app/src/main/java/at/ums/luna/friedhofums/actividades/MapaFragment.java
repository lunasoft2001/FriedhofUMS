package at.ums.luna.friedhofums.actividades;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.ums.luna.friedhofums.GPS.MiPosicion;
import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.ArbeitDetail;
import at.ums.luna.friedhofums.modelo.Grab;
import at.ums.luna.friedhofums.modelo.GrabList;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, AdapterView.OnItemSelectedListener {

    double miLatitud;
    double miLongitud;
    private List<Grab> mListaTumbas;

    private String filtro;
    private String[] argumentos;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Spinner mMapTypeSelector;

    private String tocado = "NO";

    private int mMapTypes[] = {
            GoogleMap.MAP_TYPE_NONE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN
    };

    OperacionesBaseDatos db;


    public static MapaFragment newInstance() {
        MapaFragment fragment = new MapaFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa, null, false);
        db = new OperacionesBaseDatos(getContext());
        filtro = getArguments().getString("filtro");
        argumentos = getArguments().getStringArray("argumentos");

        mMapTypeSelector = (Spinner) view.findViewById(R.id.map_type_selector);
        mMapTypeSelector.setOnItemSelectedListener(this);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(miLatitud, miLongitud), 20));
        mMap.setOnMarkerDragListener(this);

        CameraPosition oldPos = mMap.getCameraPosition();
        CameraPosition pos = CameraPosition.builder(oldPos).bearing(60).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));

        mMap.setOnMarkerClickListener(this);

//      actualizaMarcas();

    }

    public void actualizaMarcas() {
        for (int x = 0; x < mListaTumbas.size(); x++) {
            mMap.addMarker(agregarMarca(mListaTumbas.get(x)));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (tocado.equals(marker.getTitle())) {
            tocado.equals("NO");

            Intent intento = new Intent(getContext(), MiPosicion.class);
            intento.putExtra("idGrab", marker.getTitle());
            startActivity(intento);
        } else {
            tocado = marker.getTitle();
            Toast.makeText(getContext(), "Noch click für Detail sehen", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();

//        mListaTumbas = db.verListaGrabFiltrada(filtro, argumentos);

        if (mListaTumbas == null) {
            miLatitud = 47.061528;
            miLongitud = 15.459081;
        } else {
            miLatitud = mListaTumbas.get(0).getLatitud();
            miLongitud = mListaTumbas.get(0).getLongitud();
        }


        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(final Marker marker) {
        //Mueve el marker de una tumba, lo guarda en sqLite y en el servidor

        String codigoObtenido = marker.getTitle();
        double nuevaLatitud = marker.getPosition().latitude;
        double nuevaLongitud = marker.getPosition().longitude;

        try {
            db.actualizaCoordenadasTumba(codigoObtenido, nuevaLatitud, nuevaLongitud);
        } catch (Exception e) {
            Log.i("MENSAJES", e.toString());
            Toast.makeText(getContext(), "Error. Position nicht gespeichert", Toast.LENGTH_SHORT).show();
        }
    }


    public MarkerOptions agregarMarca(Grab grab) {
        MarkerOptions agregar = new MarkerOptions();

        agregar.position(new LatLng(grab.getLatitud(), grab.getLongitud()));
        agregar.title(grab.getIdGrab());
        agregar.snippet(grab.getGrabname());
        agregar.icon(grab.iconoDeTumba());
        agregar.draggable(true);

        return agregar;
    }

    public static int obtenerDistancia(double lat_a, double lng_a, double lat_b, double lon_b) {
        double Radius = 6371000; //Radio de la tierra
        double lat1 = lat_a / 1E6;
        double lat2 = lat_b / 1E6;
        double lon1 = lng_a / 1E6;
        double lon2 = lon_b / 1E6;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (Radius * c);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        mMap.setMapType(mMapTypes[position]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }





    /**
     * recibe de la activity
     **/
    public void recibeListadoTumbas (GrabList grabList){

        mListaTumbas = grabList.getGrabList();
    }

    public void borrarMarcas(){
        mMap.clear();
    }

    /**
     * fin de recepcion
     */

}
