package at.ums.luna.friedhofums.actividades;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
public class ListadoDetailFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener{

    private ListView mListViewDetail;
    private ArrayList<ArbeitDetail> mListaDetalle;
    private Context esteContexto;
    private AdaptadorArbeitDetalle adaptadorDetalle;
    private String filtro;
    private String idTarea;
    private SearchView mSearchView;
    private int totalRegistro = 0;
    private ProgressDialog progress;
    private SwipeRefreshLayout swipeRefreshLayout;

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

    public Context esteContexto(){
        return getContext();
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
        swipeRefreshLayout = (SwipeRefreshLayout) viewFragmento.findViewById(R.id.swipe_refresh_layout);

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


        /**
         * Mostramos la animacion de Swipe Refresh al crear la actitvity
         */
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                filtrar_tareas();
            }
        });



    }

    @Override
    public void onRefresh() {
        filtrar_tareas();
    }

    @Override
    public void onResume() {
        super.onResume();


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
        prepararFiltro(idTierra, "tierra");
        prepararFiltro(idPlantar, "plantar");
        prepararFiltro(idRegar, "regar");
        prepararFiltro(idLimpiar, "limpiar");
        prepararFiltro(idDecorar, "decorar");
        prepararFiltro(idPflege, "pflege");

        new obtenerListadoDetalleAsync().execute();

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

    private class obtenerListadoDetalleAsync extends AsyncTask<String, Integer, Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mListaDetalle = new ArrayList<>();
            swipeRefreshLayout.setRefreshing(true);
//            progress = new ProgressDialog(getContext());
//            progress.setTitle("Server Verbindung");
//            progress.setMessage("Herunterladend...");
//            progress.setProgressStyle(progress.STYLE_HORIZONTAL);
//            progress.setProgress(0);
//            progress.setMax(100);
//            progress.show();


        }

        @Override
        protected Integer doInBackground(String... params) {

            final int PAGESIZE = 100;
            int offset= 0;
            String whereClause = filtro;
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            QueryOptions queryOptions = new QueryOptions();
            queryOptions.addSortByOption("idGrab");
            queryOptions.setPageSize(PAGESIZE);
            dataQuery.setWhereClause(whereClause.toString());
            dataQuery.setQueryOptions(queryOptions);

            BackendlessCollection<ArbeitDetail> arbeitDetailobtenidos;
            arbeitDetailobtenidos = Backendless.Persistence.of(ArbeitDetail.class).find(dataQuery);

            totalRegistro = arbeitDetailobtenidos.getTotalObjects();
//            progress.setMax(totalRegistro);


            int size = arbeitDetailobtenidos.getCurrentPage().size();

            while (size >0)
            {
                arbeitDetailobtenidos = arbeitDetailobtenidos.getPage(PAGESIZE,offset);
                for (ArbeitDetail tl  : arbeitDetailobtenidos.getCurrentPage()){
                    mListaDetalle.add(tl);

//                    progress.incrementProgressBy(1);
                }
                offset+=size;
                arbeitDetailobtenidos.getCurrentPage();
                size = arbeitDetailobtenidos.getCurrentPage().size();
            }

//            publishProgress();
            return totalRegistro;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            if(progress.getProgress() < progress.getMax()) {
//                progress.setProgress(values[0]);
//            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

//            progress.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            adaptadorDetalle = new AdaptadorArbeitDetalle(esteContexto,mListaDetalle);
            mListViewDetail.setAdapter(adaptadorDetalle);
            mListViewDetail.setTextFilterEnabled(true);

            tvTotalRegistro.setText(String.valueOf(totalRegistro));
            clickEnLista();
            setupSearchView();

        }
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
                    //filtrar_tareas();
                    break;
                case R.id.botonTierra:
                    switch (idTierra){
                        case "sin filtro":
                            tierra.setBackgroundResource(R.drawable.borde_imagen_accent);
                            idTierra = "JA";
                            break;
                        case "JA":
                            tierra.setAlpha(0.2f);
                            idTierra = "PENDIENTE";
                            break;
                        case "PENDIENTE":
                            tierra.setAlpha(1.0f);
                            tierra.setBackgroundResource(R.drawable.borde_imagen_sin_filtro);
                            idTierra = "sin filtro";
                            break;
                    }
                    //filtrar_tareas();
                    break;
                case R.id.botonPlantar:
                    switch (idPlantar){
                        case "sin filtro":
                            plantar.setBackgroundResource(R.drawable.borde_imagen_accent);
                            idPlantar = "JA";
                            break;
                        case "JA":
                            plantar.setAlpha(0.2f);
                            idPlantar = "PENDIENTE";
                            break;
                        case "PENDIENTE":
                            plantar.setAlpha(1.0f);
                            plantar.setBackgroundResource(R.drawable.borde_imagen_sin_filtro);
                            idPlantar = "sin filtro";
                            break;
                    }
                    //filtrar_tareas();
                    break;
                case R.id.botonRegar:
                    switch (idRegar){
                        case "sin filtro":
                            regar.setBackgroundResource(R.drawable.borde_imagen_accent);
                            idRegar = "JA";
                            break;
                        case "JA":
                            regar.setAlpha(0.2f);
                            idRegar = "PENDIENTE";
                            break;
                        case "PENDIENTE":
                            regar.setAlpha(1.0f);
                            regar.setBackgroundResource(R.drawable.borde_imagen_sin_filtro);
                            idRegar = "sin filtro";
                            break;
                    }
                    //filtrar_tareas();
                    break;
                case R.id.botonLimpiar:
                    switch (idLimpiar){
                        case "sin filtro":
                            limpiar.setBackgroundResource(R.drawable.borde_imagen_accent);
                            idLimpiar = "JA";
                            break;
                        case "JA":
                            limpiar.setAlpha(0.2f);
                            idLimpiar = "PENDIENTE";
                            break;
                        case "PENDIENTE":
                            limpiar.setAlpha(1.0f);
                            limpiar.setBackgroundResource(R.drawable.borde_imagen_sin_filtro);
                            idLimpiar = "sin filtro";
                            break;
                    }
                    //filtrar_tareas();
                    break;
                case R.id.botonDecorar:
                    switch (idDecorar){
                        case "sin filtro":
                            decorar.setBackgroundResource(R.drawable.borde_imagen_accent);
                            idDecorar = "JA";
                            break;
                        case "JA":
                            decorar.setAlpha(0.2f);
                            idDecorar = "PENDIENTE";
                            break;
                        case "PENDIENTE":
                            decorar.setAlpha(1.0f);
                            decorar.setBackgroundResource(R.drawable.borde_imagen_sin_filtro);
                            idDecorar = "sin filtro";
                            break;
                    }
                    filtrar_tareas();
                    break;
                case R.id.botonPflege:
                    switch (idPflege){
                        case "sin filtro":
                            pflege.setBackgroundResource(R.drawable.borde_imagen_accent);
                            idPflege = "JA";
                            break;
                        case "JA":
                            pflege.setAlpha(0.2f);
                            idPflege = "PENDIENTE";
                            break;
                        case "PENDIENTE":
                            pflege.setAlpha(1.0f);
                            pflege.setBackgroundResource(R.drawable.borde_imagen_sin_filtro);
                            idPflege = "sin filtro";
                            break;
                    }
                    filtrar_tareas();
                    break;
            }

        }
    };

}
