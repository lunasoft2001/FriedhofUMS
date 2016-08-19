package at.ums.luna.friedhofums.actividades;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import at.ums.luna.friedhofums.R;

public class ListadoDetail extends AppCompatActivity {

    private int MODO;
    private String idTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_detail);

        MODO = getIntent().getIntExtra("MODO",1);
        idTarea = getIntent().getStringExtra("idTarea");

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
                    ListadoDetailFragment f1 = new ListadoDetailFragment();
                    f1.setArguments(args);
                    return f1;
                case 1:
                    MapaDetailFragment f2 = new MapaDetailFragment();
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
