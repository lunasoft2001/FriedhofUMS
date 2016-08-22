package at.ums.luna.friedhofums.actividades;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

        View viewFragmento = inflater.inflate(R.layout.fragment_listado_detail, container, false);

        mSearchView = (SearchView) viewFragmento.findViewById(R.id.search_view_detail);
        mListViewDetail = (ListView) viewFragmento.findViewById(R.id.miListView_detail);

        return obtenerListadoDetalle(viewFragmento);
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
            mListViewDetail.clearTextFilter();
        } else {
            mListViewDetail.setFilterText(newText.toString());
        }

        clickEnLista();

        return true;
    }

    // ----------------------- METODO ANTIGUO DIRECTO AL SERVIDOR
    private View obtenerListadoDetalle(View viewFragmento) {
        mListaDetalle = new ArrayList<>();


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
                    Log.i("MENSAJES",  "En total hay " + listaTareasBack.getTotalObjects() + " detalles en la lista");
                    firstResponse = false;
                }

                int size = listaTareasBack.getCurrentPage().size();
                Log.i("MENSAJES",  "Obtenidos " + size + " detalles en la lista");

                if (size>0){
                    offset+= listaTareasBack.getCurrentPage().size();
                    listaTareasBack.getPage(PAGESIZE,offset,this);

                    for (ArbeitDetail tl  : listaTareasBack.getCurrentPage()){
                        mListaDetalle.add(tl);

                    }
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

                Toast.makeText(esteContexto,detallePresionado.getIdGrab(),Toast.LENGTH_SHORT).show();
                //Abre una actividad
//                        Intent intento = new Intent(esteContexto, ListadoDetail.class);
//                        intento.putExtra("title",detallePresionado.getObjectId());
//                        startActivity(intento);


            }
        });
    }

}
