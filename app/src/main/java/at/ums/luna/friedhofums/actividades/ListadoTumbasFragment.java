package at.ums.luna.friedhofums.actividades;

import android.content.ContentValues;
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

        mListaTumbas = new ArrayList<>();

        Backendless.Persistence.of( Grab.class).find(new AsyncCallback<BackendlessCollection<Grab>>() {
            @Override
            public void handleResponse(BackendlessCollection<Grab> listaTumbasBack) {
                Log.i("MENSAJES", "Obtenidas " + listaTumbasBack.getCurrentPage().size() + " tumbas en la lista");

                for(Grab tl : listaTumbasBack.getCurrentPage()){
                    mListaTumbas.add(tl);
                    Log.i("MENSAJES", "Encontrado el evento " + tl.getIdGrab());
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("MENSAJES","Error num " + backendlessFault.getCode());
                // an error has occurred, the error code can be retrieved with fault.getCode()

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

            TextView tvFila = (TextView) filaView.findViewById(R.id.textView);
            tvFila.setText(nuevaTumba.getGrabname());

            return filaView;
        }
    }

}
