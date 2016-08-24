package at.ums.luna.friedhofums.actividades;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import at.ums.luna.friedhofums.GPS.MiPosicion;
import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.adaptadores.AdaptadorArbeitDetalle;
import at.ums.luna.friedhofums.adaptadores.AdaptadorArbeitKopf;
import at.ums.luna.friedhofums.modelo.ArbeitDetail;
import at.ums.luna.friedhofums.modelo.ArbeitKopf;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListadoDetailFragment extends Fragment implements SearchView.OnQueryTextListener {

    private ListView mListViewDetail;
    private ArrayList<ArbeitDetail> mListaDetalle;
    private Context esteContexto;
    private AdaptadorArbeitDetalle adaptadorDetalle;
    private String filtro;
    private String idTarea;
    private SearchView mSearchView;
    private int totalRegistro = 0;
    private int contadorProgreso;

    private String idRecoger = "sin filtro";
    private String idTierra = "sin filtro";
    private String idPlantar = "sin filtro";
    private String idRegar = "sin filtro";
    private String idLimpiar = "sin filtro";
    private String idDecorar = "sin filtro";
    private String idPflege = "sin filtro";

    private ImageButton recoger;
    private ImageButton tierra;
    private ImageButton plantar;
    private ImageButton regar;
    private ImageButton limpiar;
    private ImageButton decorar;
    private ImageButton pflege;
    private TextView tvTotalRegistro;

    private View viewFragmento;


    public ListadoDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        esteContexto = getContext();

        filtro = getArguments().getString("filtro");
        idTarea = getArguments().getString("idTarea");

        viewFragmento = inflater.inflate(R.layout.fragment_listado_detail, container, false);

        mSearchView = (SearchView) viewFragmento.findViewById(R.id.search_view_detail);
        mListViewDetail = (ListView) viewFragmento.findViewById(R.id.miListView_detail);

        recoger = (ImageButton)viewFragmento.findViewById(R.id.botonRecoger);
        tierra = (ImageButton)viewFragmento.findViewById(R.id.botonTierra);
        plantar = (ImageButton)viewFragmento.findViewById(R.id.botonPlantar);
        regar = (ImageButton)viewFragmento.findViewById(R.id.botonRegar);
        limpiar = (ImageButton)viewFragmento.findViewById(R.id.botonLimpiar);
        decorar = (ImageButton)viewFragmento.findViewById(R.id.botonDecorar);
        pflege = (ImageButton)viewFragmento.findViewById(R.id.botonPflege);
        tvTotalRegistro = (TextView)viewFragmento.findViewById(R.id.tvTotalRegistro);


//        return obtenerListadoDetalle(viewFragmento);
        return viewFragmento;

    }

    private void setupSearchView(){
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Suchen hier");
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
                    switch (idRecoger){
                        case "sin filtro":
                            recoger.setBackgroundResource(R.drawable.borde_imagen_accent);
                            idRecoger = "JA";
                            break;
                        case "JA":
                            recoger.setAlpha(0.2f);
                            idRecoger = "PENDIENTE";
                            break;
                        case "PENDIENTE":
                            recoger.setAlpha(1.0f);
                            recoger.setBackgroundResource(R.drawable.borde_imagen_sin_filtro);
                            idRecoger = "sin filtro";
                            break;
                    }
                    filtrar_tareas();
                    break;

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        obtenerListadoDetalle(viewFragmento);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)){
            mListViewDetail.clearTextFilter();
        } else {
            mListViewDetail.setFilterText(newText.toString());
        }

        clickEnLista();

        return true;
    }

    private void filtrar_tareas(){
        //filtra el listado en funcion de los botones apretados
        filtro = getArguments().getString("filtro");

        prepararFiltro(idRecoger,"recoger");

//        switch (idRecoger){
//            case "sin filtro":
//                break;
//            case "JA":
//                filtro = filtro + " and recoger = 'JA'";
//                break;
//            case "PENDIENTE":
//                filtro = filtro + " and recoger != 'JA'";
//                filtro = filtro + " and recoger != 'NEIN'";
//                break;
//        }

        obtenerListadoDetalle(viewFragmento);

    }

    private void prepararFiltro(String idTarea, String tarea){
        switch (idTarea){
            case "sin filtro":
                break;
            case "JA":
                filtro = filtro + " and " + tarea + " = 'JA'";
                break;
            case "PENDIENTE":
                filtro = filtro + " and " + tarea + " != 'JA'";
                filtro = filtro + " and " + tarea + " != 'NEIN'";
                break;
        }
    }

    // ----------------------- METODO ANTIGUO DIRECTO AL SERVIDOR
    private View obtenerListadoDetalle(View viewFragmento) {
        mListaDetalle = new ArrayList<>();

        //Prepara la barra de progreso
        final ProgressDialog progress;
        progress =new ProgressDialog(getContext());
        progress.setMessage("Herunterladend...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgress(0);
        progress.setCancelable(false);
        contadorProgreso = 0;



        final int PAGESIZE = 100;

        String whereClause = filtro;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addSortByOption("idGrab");
        queryOptions.setPageSize(PAGESIZE);
        dataQuery.setWhereClause(whereClause.toString());
        dataQuery.setQueryOptions(queryOptions);


        Backendless.Persistence.of(ArbeitDetail.class).find(dataQuery, new AsyncCallback<BackendlessCollection<ArbeitDetail>>() {

            private int offset = 0;
            private boolean firstResponse = true;


            @Override
            public void handleResponse(BackendlessCollection<ArbeitDetail> listaTareasBack) {

                if(firstResponse){
                    totalRegistro = listaTareasBack.getTotalObjects();
                    tvTotalRegistro.setText(String.valueOf(totalRegistro));
                    progress.setMax(totalRegistro);
                    progress.show();

                    firstResponse = false;
                }

                int size = listaTareasBack.getCurrentPage().size();

                if (size>0){
                    offset+= listaTareasBack.getCurrentPage().size();
                    listaTareasBack.getPage(PAGESIZE,offset,this);

                    for (ArbeitDetail tl  : listaTareasBack.getCurrentPage()){
                        mListaDetalle.add(tl);
                        progress.setProgress(contadorProgreso);
                        contadorProgreso++;

                    }
                } else{
                    progress.hide();

                }
                listaTareasBack.getCurrentPage();

                adaptadorDetalle = new AdaptadorArbeitDetalle(esteContexto,mListaDetalle);

                mListViewDetail.setAdapter(adaptadorDetalle);
                mListViewDetail.setTextFilterEnabled(true);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("MENSAJES","Error num " + backendlessFault.getCode());
            }

        });

        clickEnLista();

        setupSearchView();

        return viewFragmento;
    }

    private void clickEnLista() {
        mListViewDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArbeitDetail detallePresionado = adaptadorDetalle.objetoArrayList.get(position);

                //Abre una actividad
                Intent intento = new Intent(esteContexto, FormularioDetalle.class);
                intento.putExtra("idObjeto",detallePresionado.getObjectId());
                intento.putExtra("idGrab", detallePresionado.getIdGrab());
                startActivity(intento);


            }
        });
    }

}
