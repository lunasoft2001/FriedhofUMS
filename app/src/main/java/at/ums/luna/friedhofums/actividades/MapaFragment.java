package at.ums.luna.friedhofums.actividades;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static MapaFragment newInstance() {
        MapaFragment fragment = new MapaFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, null, false);

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
        Backendless.Persistence.of(Grab.class).find(new AsyncCallback<BackendlessCollection<Grab>>() {
            @Override
            public void handleResponse(BackendlessCollection<Grab> grabBackendlessCollection) {
                for(Grab gl : grabBackendlessCollection.getCurrentPage()){
                    mMap.addMarker(agregarMarca(gl));
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });

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
}
