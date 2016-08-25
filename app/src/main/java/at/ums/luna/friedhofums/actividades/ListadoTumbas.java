package at.ums.luna.friedhofums.actividades;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;
import at.ums.luna.friedhofums.modelo.GrabList;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

public class ListadoTumbas extends AppCompatActivity implements ListadoTumbasFragment.OnHeadlineSelectedListener {

    private int MODO;

    private boolean primeraVez;

    private ListadoTumbasFragment listadoTumbasFragment;
    private MapaFragment mapaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tumbas);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MODO = getIntent().getIntExtra("modo",1);
        primeraVez = true;

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AdaptadorPager(getSupportFragmentManager()));


    }

    /**
     * para recibir y enviar informacion a los fragmentos
     * @param lista
     */
    @Override
    public void onListaObtenida(GrabList lista) {

        if (primeraVez) {
            Log.i("MENSAJES", "---- PRIMERA VEZ ----");
            Log.i("MENSAJES","Enviando lista con " + lista.getGrabList().size() + " registros.");
            mapaFragment.recibeListadoTumbas(lista);
            primeraVez = false;
        } else {
            Log.i("MENSAJES", "---- SEGUNDA VEZ ----");
            Log.i("MENSAJES","Enviando lista con " + lista.getGrabList().size() + " registros.");
            mapaFragment.borrarMarcas();
            mapaFragment.recibeListadoTumbas(lista);
            mapaFragment.actualizaMarcas();
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
            //define que busqueda haremos, 1 es sin filtros
            switch (MODO) {
                case 1:
                    args.putString("filtro",null);
                    args.putStringArray("argumentos",null);
                    break;
                case 2:
                    args.putString("filtro","friedhof = ?");
                    String[] valoresDeArgs = {"Deutsch Goritz"};
                    args.putStringArray("argumentos",valoresDeArgs);
                    break;
            }

            switch (position) {
                case 0:
                    listadoTumbasFragment = new ListadoTumbasFragment();
                    listadoTumbasFragment.setArguments(args);
                    return listadoTumbasFragment;
                case 1:
                    mapaFragment = new MapaFragment();
                    mapaFragment.setArguments(args);
                    return mapaFragment;
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
