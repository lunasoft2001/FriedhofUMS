package at.ums.luna.friedhofums.actividades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SearchView;
import android.text.TextUtils;
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
import java.util.List;

import at.ums.luna.friedhofums.GPS.MiPosicion;
import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.adaptadores.AdaptadorTumbas;
import at.ums.luna.friedhofums.modelo.Grab;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListadoTumbasFragment extends Fragment implements SearchView.OnQueryTextListener{

    private ListView mListViewTumbas;
    private ArrayList<Grab> mListaTumbas;
    private Context esteContexto;
    private AdaptadorTumbas adaptadorTumbas;
    private String filtro;
    private String[] argumentos;
    private SearchView mSearchView;

    public ListadoTumbasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        esteContexto = getContext();
        View viewFragmento = inflater.inflate(R.layout.fragment_listado_tumbas, container, false);

        mSearchView = (SearchView) viewFragmento.findViewById(R.id.search_view);
        mListViewTumbas = (ListView) viewFragmento.findViewById(R.id.miListView);

        filtro = getArguments().getString("filtro");
        argumentos = getArguments().getStringArray("argumentos");

        return obtenerListadoTumbas(viewFragmento);

    }

    private View obtenerListadoTumbas(View viewFragmento) {
        mListaTumbas = new ArrayList<Grab>();
        OperacionesBaseDatos db = new OperacionesBaseDatos(esteContexto);
        mListaTumbas =  db.verListaGrabFiltrada(filtro,argumentos);

        adaptadorTumbas = new AdaptadorTumbas(esteContexto,mListaTumbas);
        mListViewTumbas.setAdapter(adaptadorTumbas);

        mListViewTumbas.setTextFilterEnabled(true);


        mListViewTumbas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Grab tumbaPresionada = mListaTumbas.get(position);

                Toast.makeText(esteContexto, tumbaPresionada.getIdGrab(), Toast.LENGTH_SHORT).show();

                //Abre una actividad
                Intent intento = new Intent(esteContexto, MiPosicion.class);
                intento.putExtra("idGrab",tumbaPresionada.getIdGrab());
                startActivity(intento);

            }
        });

        setupSearchView();

        return viewFragmento;
    }

    private void setupSearchView(){
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        // mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Suchen hear");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)){
            mListViewTumbas.clearTextFilter();
        } else {
            mListViewTumbas.setFilterText(newText.toString());
        }

        mListViewTumbas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Grab tumbaPresionada = adaptadorTumbas.grabArrayList.get(position);

                Toast.makeText(esteContexto, tumbaPresionada.getIdGrab(), Toast.LENGTH_SHORT).show();

                //Abre una actividad
                Intent intento = new Intent(esteContexto, MiPosicion.class);
                intento.putExtra("idGrab",tumbaPresionada.getIdGrab());
                startActivity(intento);

            }
        });


        return true;
    }



    // ----------------------- METODO ANTIGUO DIRECTO AL SERVIDOR
//    private View obtenerListadoTumbas(View viewFragmento) {
//        mListaTumbas = new ArrayList<>();
//
//        final int PAGESIZE = 100;
//        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
//        QueryOptions queryOptions = new QueryOptions();
//        queryOptions.setPageSize(PAGESIZE);
//        queryOptions.addSortByOption("idGrab ASC");
//        dataQuery.setQueryOptions(queryOptions);
//
//
//        Backendless.Persistence.of(Grab.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Grab>>() {
//
//            private int offset = 0;
//            private boolean firstResponse = true;
//
//
//            @Override
//            public void handleResponse(BackendlessCollection<Grab> listaGrabBack) {
//
//                if(firstResponse){
//                    Log.i("MENSAJES",  "En total hay " + listaGrabBack.getTotalObjects() + " tumbas en la lista");
//                    firstResponse = false;
//                }
//
//                int size = listaGrabBack.getCurrentPage().size();
//                Log.i("MENSAJES",  "Obtenidas " + size + " tumbas en la lista");
//
//                if (size>0){
//                    offset+= listaGrabBack.getCurrentPage().size();
//                    listaGrabBack.getPage(PAGESIZE,offset,this);
//
//                    for (Grab tl  : listaGrabBack.getCurrentPage()){
//                        mListaTumbas.add(tl);
//
//
//                    }
//                }
//               listaGrabBack.getCurrentPage();
//
//                mListViewTumbas.setAdapter(new AdaptadorTumbas(esteContexto,mListaTumbas));
//                mListViewTumbas.setTextFilterEnabled(true);
//
//            }
//
//            @Override
//            public void handleFault(BackendlessFault backendlessFault) {
//                Log.i("MENSAJES","Error num " + backendlessFault.getCode());
//            }
//
//        });
//
//
//        mListViewTumbas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Grab tumbaPresionada = mListaTumbas.get(position);
//
//                //Abre una actividad
//                Intent intento = new Intent(esteContexto, MiPosicion.class);
//                intento.putExtra("idGrab",tumbaPresionada.getIdGrab());
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
