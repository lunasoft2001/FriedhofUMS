package at.ums.luna.friedhofums.actividades;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

public class ListadoArbeit extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_arbeit);


    }

}
