package at.ums.luna.friedhofums.actividades;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.ArbeitDetail;
import at.ums.luna.friedhofums.modelo.GrabList;

public class ListadoDetail extends AppCompatActivity implements ListadoDetailFragment.OnHeadlineSelectedListener {

    private int MODO;

    private boolean primeraVez;
    private String idTarea;

    private ListadoDetailFragment listadoDetailFragment;
    private MapaDetailFragment mapaDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_detail);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MODO = getIntent().getIntExtra("MODO",1);
        idTarea = getIntent().getStringExtra("idTarea");

        primeraVez = true;

        mapaDetailFragment = new MapaDetailFragment();
        listadoDetailFragment = new ListadoDetailFragment();


        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AdaptadorPager(getSupportFragmentManager()));

    }


    /**
     * para recibir y enviar informacion a los fragmentos
     * @param lista
     */
    @Override
    public void onListaObtenida(ArrayList<ArbeitDetail> lista) {

        if (primeraVez) {
            Log.i("MENSAJES", "---- PRIMERA VEZ ----");
            Log.i("MENSAJES","Enviando lista con " + lista.size() + " registros.");
            mapaDetailFragment.recibeListadoTumbas(lista);
            mapaDetailFragment.actualizaMarcas();
            primeraVez = false;
        } else {
            Log.i("MENSAJES", "---- SEGUNDA VEZ ----");
            Log.i("MENSAJES","Enviando lista con " + lista.size() + " registros.");
            mapaDetailFragment.borrarMarcas();
            mapaDetailFragment.recibeListadoTumbas(lista);
            mapaDetailFragment.actualizaMarcas();
        }
    }

    /**
     * Fin enviar informacion entre fragmentos
     */


    private class AdaptadorPager extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.LISTA);
                case 1:
                    return getString(R.string.MAPA);
                default:
                    return "---";
            }
        }

        public AdaptadorPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle args = new Bundle();

            StringBuilder whereClause = new StringBuilder();
            whereClause.append( "ArbeitKopf[arbeitDetail]" );
            whereClause.append( ".objectId='" ).append(idTarea).append( "'" );

            //define que busqueda haremos, 1 es sin filtros
            switch (MODO) {
                case 1:
                    args.putString("filtro",null);
                    args.putStringArray("argumentos",null);
                    args.putString("idTarea", idTarea);
                    break;
                case 2:
                    args.putString("filtro", whereClause.toString());
                    args.putStringArray("argumentos",null);
                    args.putString("idTarea", idTarea);

                    break;
            }

            switch (position) {
                case 0:
                    listadoDetailFragment = new ListadoDetailFragment();
                    listadoDetailFragment.setArguments(args);
                    return listadoDetailFragment;
                case 1:
                    mapaDetailFragment.setArguments(args);
                    return mapaDetailFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
