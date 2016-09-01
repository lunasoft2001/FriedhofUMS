package at.ums.luna.friedhofums.actividades;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.ArbeitDetail;
import at.ums.luna.friedhofums.modelo.Grab;
import at.ums.luna.friedhofums.modelo.GrabList;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaDetailFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, AdapterView.OnItemSelectedListener {


    double miLatitud;
    double miLongitud;
    private List<ArbeitDetail> mListaTumbas;

    private String filtro;
    private String[] argumentos;

    private String tocado = "NO";


    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Spinner mMapTypeSelector;

    private int mMapTypes[] = {
            GoogleMap.MAP_TYPE_NONE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN
    };

    OperacionesBaseDatos db;

    public MapaDetailFragment() {
        // Required empty public constructor
    }

    public static MapaDetailFragment newInstance(){
        return new MapaDetailFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa_detail, null, false);

        db = new OperacionesBaseDatos(getContext());
        filtro = getArguments().getString("filtro");
        argumentos = getArguments().getStringArray("argumentos");


        mMapTypeSelector = (Spinner) view.findViewById(R.id.map_type_selector);
        mMapTypeSelector.setOnItemSelectedListener(this);

        mListaTumbas = new ArrayList<>();


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        miLatitud = 47.061528;
        miLongitud = 15.459081;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(miLatitud,miLongitud),20));
        mMap.setOnMarkerDragListener(this);

        CameraPosition oldPos = mMap.getCameraPosition();
        CameraPosition pos = CameraPosition.builder(oldPos).bearing(60).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));

        mMap.setOnMarkerClickListener(this);
    }

    public void actualizaMarcas() {
        for (int x= 0; x<mListaTumbas.size();x++ ) {
            mMap.addMarker(agregarMarca(mListaTumbas.get(x)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        ArbeitDetail detalleElegido = new ArbeitDetail();

        //busca el idGrab en la lista para obtener el idObject
        for (ArbeitDetail detalle : mListaTumbas){
            if (detalle.getIdGrab().equals(marker.getTitle())){
                detalleElegido = detalle;
            }
        }

        if (tocado.equals(marker.getTitle())) {
            tocado.equals("NO");

            Intent intento = new Intent(getContext(), FormularioDetalle.class);
            intento.putExtra("idGrab",detalleElegido.getIdGrab());
            intento.putExtra("idObjeto",detalleElegido.getObjectId());
            startActivity(intento);
        } else {
            tocado = marker.getTitle();
            Toast.makeText(getContext(),"Noch click für Detail sehen", Toast.LENGTH_SHORT).show();
        }

        return false;
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

        try {
            db.actualizaCoordenadasTumba(codigoObtenido,nuevaLatitud,nuevaLongitud);
        }catch(Exception e){
            Log.i("MENSAJES", e.toString());
            Toast.makeText(getContext(),"Error. Position nicht gespeichert",Toast.LENGTH_SHORT).show();
        }
    }





    public MarkerOptions agregarMarca(ArbeitDetail detalle){
        MarkerOptions agregar = new MarkerOptions();

        agregar.position(new LatLng(detalle.getGrab().getLatitud(), detalle.getGrab().getLongitud()));
        agregar.title(detalle.getGrab().getIdGrab());
        agregar.snippet(detalle.getGrab().getGrabname() + ": " + String.valueOf(detalle.getCantidad()));
        agregar.icon(detalle.getGrab().iconoDeTumba());
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mMap.setMapType(mMapTypes[position]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * recibe de la activity
     **/
    public void recibeListadoTumbas (ArrayList<ArbeitDetail> grabList){

        mListaTumbas = grabList;
    }

    public void borrarMarcas(){
        mMap.clear();
    }

    /**
     * fin de recepcion
     */





}

