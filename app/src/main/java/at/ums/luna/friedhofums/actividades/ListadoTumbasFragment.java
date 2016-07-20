package at.ums.luna.friedhofums.actividades;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import java.util.List;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListadoTumbasFragment extends Fragment {

    private ListView mListViewTumbas;
    private List<Grab> mListaTumbas;

    private Context esteContexto;

    public ListadoTumbasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        esteContexto = getContext();
        View viewFragmento = inflater.inflate(R.layout.fragment_listado_tumbas, container, false);

        mListViewTumbas = (ListView) viewFragmento.findViewById(R.id.miListView);

        return obtenerListadoTumbas(viewFragmento);

    }

    private View obtenerListadoTumbas(View viewFragmento) {
        mListaTumbas = new ArrayList<>();

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
                        mListaTumbas.add(tl);
                        Log.i("MENSAJES", "Encontrada la tumba " + tl.getIdGrab());
                    }
                }
               listaGrabBack.getCurrentPage();

                mListViewTumbas.setAdapter(new AdaptadorTumbas());

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("MENSAJES","Error num " + backendlessFault.getCode());
            }

        });


        mListViewTumbas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grab tumbaPresionada = mListaTumbas.get(position);
                Toast.makeText(esteContexto, tumbaPresionada.getGrabname().toString(), Toast.LENGTH_LONG).show();
            }
        });

        return viewFragmento;
    }

    private class AdaptadorTumbas extends BaseAdapter{

        @Override
        public int getCount() {
            return mListaTumbas.size();
        }

        @Override
        public Object getItem(int position) {
            return mListaTumbas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View filaView = getActivity().getLayoutInflater().inflate(R.layout.fila_lista_tumbas,null);

            Grab nuevaTumba = mListaTumbas.get(position);

            TextView tvidGrab = (TextView) filaView.findViewById(R.id.textViewIdGrab);
            tvidGrab.setText(nuevaTumba.getIdGrab());

            TextView tvGrabname = (TextView) filaView.findViewById(R.id.textViewGrabname);
            tvGrabname.setText(nuevaTumba.getGrabname());

            TextView tvTlf1 = (TextView) filaView.findViewById(R.id.textViewTelefon1);
            tvTlf1.setText(nuevaTumba.getTelefon1());

            return filaView;
        }
    }

}
