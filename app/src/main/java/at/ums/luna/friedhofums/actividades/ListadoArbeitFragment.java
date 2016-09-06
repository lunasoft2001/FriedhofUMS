package at.ums.luna.friedhofums.actividades;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;

import at.ums.luna.friedhofums.GPS.MiPosicion;
import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.adaptadores.AdaptadorArbeitKopf;
import at.ums.luna.friedhofums.adaptadores.AdaptadorTumbas;
import at.ums.luna.friedhofums.modelo.ArbeitKopf;
import at.ums.luna.friedhofums.modelo.Grab;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListadoArbeitFragment extends Fragment implements SearchView.OnQueryTextListener{

    private ListView mListViewArbeit;
    private ArrayList<ArbeitKopf> mListaTareas;
    private Context esteContexto;
    private AdaptadorArbeitKopf adaptadorTareas;
    private String filtro;
    private String[] argumentos;
    private SearchView mSearchView;

    private String whereClause;

    public ListadoArbeitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        esteContexto = getContext();

        View viewFragmento = inflater.inflate(R.layout.fragment_listado_arbeit, container, false);

        mSearchView = (SearchView) viewFragmento.findViewById(R.id.search_view);
        mListViewArbeit = (ListView) viewFragmento.findViewById(R.id.miListView);


//        filtro = getArguments().getString("filtro");
//        argumentos = getArguments().getStringArray("argumentos");

        return obtenerListadoTareas(viewFragmento);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void setupSearchView(){
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Suchen hier");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)){
            mListViewArbeit.clearTextFilter();
        } else {
            mListViewArbeit.setFilterText(newText.toString());
        }

        clickEnLista();

        return true;
    }



    // ----------------------- METODO ANTIGUO DIRECTO AL SERVIDOR
    private View obtenerListadoTareas(View viewFragmento) {
        mListaTareas = new ArrayList<>();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        String idTrabajadorActual = pref.getString("prefijo","?");

        Log.i("MENSAJES", "idTrabajador = " + idTrabajadorActual);


        if (idTrabajadorActual.equals("AK")){
            Log.i("MENSAJES", "idTrabajador COINCIDE");
            whereClause = "";
        } else{
            Log.i("MENSAJES", "idTrabajador NO COINCIDE");
            whereClause = "terminado = False and mitarbeiter = '" + idTrabajadorActual + "'";
            whereClause = whereClause + " or terminado = False and mitarbeiter = ''";
        }


        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setPageSize(PAGESIZE);
        queryOptions.addSortByOption("title ASC");
        dataQuery.setWhereClause(whereClause);
        dataQuery.setQueryOptions(queryOptions);


        Backendless.Persistence.of(ArbeitKopf.class).find(dataQuery, new AsyncCallback<BackendlessCollection<ArbeitKopf>>() {

            private int offset = 0;
            private boolean firstResponse = true;


            @Override
            public void handleResponse(BackendlessCollection<ArbeitKopf> listaTareasBack) {

                if(firstResponse){
                    Log.i("MENSAJES",  "En total hay " + listaTareasBack.getTotalObjects() + " tareas en la lista");
                    firstResponse = false;
                }

                int size = listaTareasBack.getCurrentPage().size();
                Log.i("MENSAJES",  "Obtenidas " + size + " tareas en la lista");

                if (size>0){
                    offset+= listaTareasBack.getCurrentPage().size();
                    listaTareasBack.getPage(PAGESIZE,offset,this);

                    for (ArbeitKopf tl  : listaTareasBack.getCurrentPage()){
                        mListaTareas.add(tl);


                    }
                }
                listaTareasBack.getCurrentPage();

                adaptadorTareas = new AdaptadorArbeitKopf(esteContexto,mListaTareas);

                mListViewArbeit.setAdapter(adaptadorTareas);
                mListViewArbeit.setTextFilterEnabled(true);
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
        mListViewArbeit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArbeitKopf tareaPresionada = adaptadorTareas.objetoArrayList.get(position);

                Toast.makeText(esteContexto, tareaPresionada.getTitle(), Toast.LENGTH_SHORT).show();

                //Abre una actividad
                Intent intento = new Intent(esteContexto, ListadoDetail.class);
                intento.putExtra("idTarea",tareaPresionada.getObjectId());
                intento.putExtra("MODO", 2);
                startActivity(intento);

            }
        });
    }

    //    private View obtenerListadoTareas(View viewFragmento) {
//        mListaTareas = new ArrayList<ArbeitKopf>();
//        OperacionesBaseDatos db = new OperacionesBaseDatos(esteContexto);
//        mListaTareas =  db.verArbeitListFiltrada(filtro,argumentos);
//
//        adaptadorTareas = new AdaptadorArbeitDetalle(esteContexto,mListaTareas);
//        mListViewArbeit.setAdapter(adaptadorTareas);
//
//        mListViewArbeit.setTextFilterEnabled(true);
//
//
//        mListViewArbeit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                ArbeitKopf tareaPresionada = mListaTareas.get(position);
//
//                Toast.makeText(esteContexto, tareaPresionada.getTitle(), Toast.LENGTH_SHORT).show();
//
//                //Abre una actividad
//                Intent intento = new Intent(esteContexto, MiPosicion.class);
//                intento.putExtra("title",tareaPresionada.getTitle());
//                startActivity(intento);
//
//            }
//        });
//
//        setupSearchView();
//
//        return viewFragmento;
//    }

}



