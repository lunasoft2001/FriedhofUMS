package at.ums.luna.friedhofums.actividades;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.Grab;
import at.ums.luna.friedhofums.servidor.OperacionesBaseDatos;

public class ListadoTumbas extends AppCompatActivity {

    private int MODO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tumbas);

        MODO = getIntent().getIntExtra("modo",1);


        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AdaptadorPager(getSupportFragmentManager()));


    }

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
                    ListadoTumbasFragment f1 = new ListadoTumbasFragment();
                    f1.setArguments(args);
                    return f1;
                case 1:
                    MapaFragment f2 = new MapaFragment();
                    f2.setArguments(args);
                    return f2;
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
