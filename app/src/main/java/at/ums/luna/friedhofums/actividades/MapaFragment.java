package at.ums.luna.friedhofums.actividades;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import at.ums.luna.friedhofums.GPS.MiPosicion;
import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    double miLatitud;
    double miLongitud;
    private List<Grab> mListaTumbas;

    private String filtro;
    private String[] argumentos;


    OperacionesBaseDatos db;


    public static MapaFragment newInstance() {
        MapaFragment fragment = new MapaFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa, null, false);

        filtro = getArguments().getString("filtro");
        argumentos = getArguments().getStringArray("argumentos");

        db = new OperacionesBaseDatos(getContext());
        mListaTumbas = db.verListaGrabFiltrada(filtro, argumentos);

        miLatitud = mListaTumbas.get(1).getLatitud();
        miLongitud = mListaTumbas.get(1).getLongitud();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(miLatitud,miLongitud),20));
        mMap.setOnMarkerDragListener(this);



        //obtenemos la lista

        for (int x= 0; x<mListaTumbas.size();x++ ) {
            mMap.addMarker(agregarMarca(mListaTumbas.get(x)));
        }

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

        String codigoObtenido =  marker.getTitle();
        double nuevaLatitud =  marker.getPosition().latitude;
        double nuevaLongitud = marker.getPosition().longitude;

        db.actualizaCoordenadasTumba(codigoObtenido,nuevaLatitud,nuevaLongitud);

    }





    public MarkerOptions agregarMarca(Grab grab){
        MarkerOptions agregar = new MarkerOptions();

        agregar.position(new LatLng(grab.getLatitud(), grab.getLongitud()));
        agregar.title(grab.getIdGrab());
        agregar.snippet(grab.getGrabname());
        agregar.icon(grab.iconoDeTumba());
        agregar.draggable(true);

        return agregar;
    }

    public static int obtenerDistancia(double lat_a,double lng_a, double lat_b, double lon_b){
        double Radius = 6371000; //Radio de la tierra
        double lat1 = lat_a / 1E6;
        double lat2 = lat_b / 1E6;
        double lon1 = lng_a / 1E6;
        double lon2 = lon_b / 1E6;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (Radius * c);

    }

}
