package at.ums.luna.friedhofums.actividades;


import android.Manifest;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import at.ums.luna.friedhofums.GPS.MiPosicion;
import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    double miLatitud;
    double miLongitud;

    public static MapaFragment newInstance() {
        MapaFragment fragment = new MapaFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, null, false);

        miLatitud = getArguments().getDouble("miLatitud");
        miLongitud = getArguments().getDouble("miLongitud");


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




            //agregamos las marca

        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setPageSize(PAGESIZE);
        queryOptions.addSortByOption("idGrab ASC");
        dataQuery.setQueryOptions(queryOptions);


        Backendless.Persistence.of(Grab.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Grab>>() {

            private int offset = 0;
            private boolean firstResponse = true;


            @Override
            public void handleResponse(BackendlessCollection<Grab> listaGrabBack) {

                if(firstResponse){
                    Log.i("MENSAJES",  "En total hay " + listaGrabBack.getTotalObjects() + " tumbas en la lista");
                    firstResponse = false;
                }

                int size = listaGrabBack.getCurrentPage().size();
                Log.i("MENSAJES",  "Obtenidas " + size + " tumbas en la lista");

                if (size>0){
                    offset+= listaGrabBack.getCurrentPage().size();
                    listaGrabBack.getPage(PAGESIZE,offset,this);

                    for (Grab tl  : listaGrabBack.getCurrentPage()){
                        double distancia = obtenerDistancia(miLatitud,miLongitud,tl.getLatitud(),tl.getLongitud());

//                        Log.i("MENSAJES", "Distacia hasta " + tl.getIdGrab() + " = " + distancia + " metros");

                        mMap.addMarker(agregarMarca(tl));

                    }
                }
                listaGrabBack.getCurrentPage();

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("MENSAJES","Error num " + backendlessFault.getCode());
            }

        });

            //correcto
//
//
//        Backendless.Persistence.of(Grab.class).find( new AsyncCallback<BackendlessCollection<Grab>>() {
//            @Override
//            public void handleResponse(BackendlessCollection<Grab> grabBackendlessCollection) {
//                for(Grab gl : grabBackendlessCollection.getCurrentPage()){
//
//                    int distancia = obtenerDistancia(miLatitud,miLongitud,gl.getLatitud(),gl.getLongitud());
//
//                    Log.i("MENSAJES", "Distacia hasta " + gl.getIdGrab() + " = " + distancia + " metros");
//
//                    mMap.addMarker(agregarMarca(gl));
//                }
//            }
//
//            @Override
//            public void handleFault(BackendlessFault backendlessFault) {
//
//            }
//        });

//        LatLng lieboch = new LatLng(46.961007, 15.346874);
//        mMap.addMarker(new MarkerOptions().position(lieboch).title("Esto es Lieboch"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lieboch));

    }

    public MarkerOptions agregarMarca(Grab grab){
        MarkerOptions agregar = new MarkerOptions();

        agregar.position(new LatLng(grab.getLatitud(), grab.getLongitud()));
        agregar.title(grab.getIdGrab() + " (" + grab.getGrabname() + ")");
        agregar.icon(grab.iconoDeTumba());

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
