package at.ums.luna.friedhofums.actividades;


import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.List;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.ArbeitDetail;
import at.ums.luna.friedhofums.modelo.Grab;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormularioDetalleFragment extends Fragment {

    private String idObjeto;
    private String idTrabajadorActual;

    private TextView grabname;
    private TextView friedhof;
    private TextView bemerkung;
    private TextView detalle;
    private ImageView grabart;
    private ImageButton recoger;
    private ImageButton tierra;
    private ImageButton plantar;
    private ImageButton regar;
    private ImageButton limpiar;
    private ImageButton decorar;
    private ImageButton pflege;

    private String idRecoger;
    private String idTierra;
    private String idPlantar;
    private String idRegar;
    private String idLimpiar;
    private String idDecorar;
    private String idPflege;


    public FormularioDetalleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFragmento = inflater.inflate(R.layout.fragment_formulario_detalle,container,false);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        idTrabajadorActual = pref.getString("prefijo","?");
        idObjeto = getArguments().getString("idObjeto");

        grabname = (TextView) viewFragmento.findViewById(R.id.tvGrabname);
        friedhof = (TextView)viewFragmento.findViewById(R.id.tvFriedhof);
        bemerkung = (TextView)viewFragmento.findViewById(R.id.tvBemerkung);
        detalle = (TextView)viewFragmento.findViewById(R.id.tvDetalle);
        grabart = (ImageView) viewFragmento.findViewById(R.id.imageGrabart);
        recoger = (ImageButton)viewFragmento.findViewById(R.id.botonRecoger);
        tierra = (ImageButton)viewFragmento.findViewById(R.id.botonTierra);
        plantar = (ImageButton)viewFragmento.findViewById(R.id.botonPlantar);
        regar = (ImageButton)viewFragmento.findViewById(R.id.botonRegar);
        limpiar = (ImageButton)viewFragmento.findViewById(R.id.botonLimpiar);
        decorar = (ImageButton)viewFragmento.findViewById(R.id.botonDecorar);
        pflege = (ImageButton)viewFragmento.findViewById(R.id.botonPflege);

        obtenerDatos();

        return viewFragmento;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Codigo para el onClickListener
        getView().findViewById(R.id.botonRecoger).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonTierra).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonPlantar).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonRegar).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonLimpiar).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonDecorar).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonPflege).setOnClickListener(mGlobal_onClickListener);
    }

    //Intents para cualquier botón de la actividad
    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonRecoger:
                    if(idRecoger.equals(idTrabajadorActual)){
                        recoger.setAlpha(1.00f);
                        idRecoger = "JA";
                    } else{
                        recoger.setAlpha(0.20f);
                        idRecoger = idTrabajadorActual;
                    }
                    break;
                case R.id.botonTierra:
                    if(idTierra.equals(idTrabajadorActual)){
                        tierra.setAlpha(1.00f);
                        idTierra = "JA";
                    } else{
                        tierra.setAlpha(0.20f);
                        idTierra = idTrabajadorActual;
                    }
                    break;
                case R.id.botonPlantar:
                    if(idPlantar.equals(idTrabajadorActual)){
                        plantar.setAlpha(1.00f);
                        idPlantar = "JA";
                    } else{
                        plantar.setAlpha(0.20f);
                        idPlantar = idTrabajadorActual;
                    }
                    break;
                case R.id.botonRegar:
                    if(idRegar.equals(idTrabajadorActual)){
                        regar.setAlpha(1.00f);
                        idRegar = "JA";
                    } else{
                        regar.setAlpha(0.20f);
                        idRegar = idTrabajadorActual;
                    }
                    break;
                case R.id.botonLimpiar:
                    if(idLimpiar.equals(idTrabajadorActual)){
                        limpiar.setAlpha(1.00f);
                        idLimpiar = "JA";
                    } else{
                        limpiar.setAlpha(0.20f);
                        idLimpiar = idTrabajadorActual;
                    }
                    break;
                case R.id.botonDecorar:
                    if(idDecorar.equals(idTrabajadorActual)){
                        decorar.setAlpha(1.00f);
                        idDecorar = "JA";
                    } else{
                        decorar.setAlpha(0.20f);
                        idDecorar = idTrabajadorActual;
                    }
                    break;
                case R.id.botonPflege:
                    if(idPflege.equals(idTrabajadorActual)){
                        pflege.setAlpha(1.00f);
                        idPflege = "JA";
                    } else{
                        pflege.setAlpha(0.20f);
                        idPflege = idTrabajadorActual;
                    }
                    break;

              }
        }
    };

    public void obtenerDatos(){

        Backendless.Persistence.of(ArbeitDetail.class).findById(idObjeto, new AsyncCallback<ArbeitDetail>() {
            @Override
            public void handleResponse(ArbeitDetail arbeitDetail) {
                grabname.setText(arbeitDetail.getGrab().getGrabname());
                friedhof.setText(arbeitDetail.getGrab().getFriedhof());
                bemerkung.setText(arbeitDetail.getObservaciones());
                detalle.setText(arbeitDetail.getDetalle());
                grabart.setImageResource(arbeitDetail.getGrab().rutaDeIcono());

                idRecoger = arbeitDetail.getRecoger();
                idTierra = arbeitDetail.getTierra();
                idPlantar = arbeitDetail.getPlantar();
                idRegar = arbeitDetail.getRegar();
                idLimpiar = arbeitDetail.getLimpiar();
                idDecorar = arbeitDetail.getDecorar();
                idPflege = arbeitDetail.getPflege();

                recoger.setAlpha(arbeitDetail.transparenciaTarea(idRecoger));
                tierra.setAlpha(arbeitDetail.transparenciaTarea(idTierra));
                plantar.setAlpha(arbeitDetail.transparenciaTarea(idPlantar));
                regar.setAlpha(arbeitDetail.transparenciaTarea(idRegar));
                limpiar.setAlpha(arbeitDetail.transparenciaTarea(idLimpiar));
                decorar.setAlpha(arbeitDetail.transparenciaTarea(idDecorar));
                pflege.setAlpha(arbeitDetail.transparenciaTarea(idPflege));


                Log.i("MENSAJES", arbeitDetail.getDetalle());
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });


    }

}
