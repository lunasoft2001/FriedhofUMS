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

import at.ums.luna.friedhofums.R;

public class ListadoTumbas extends AppCompatActivity {

    double miLatitud;
    double miLongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tumbas);

        miLatitud = getIntent().getDoubleExtra("miLatitud",0);
        miLongitud = getIntent().getDoubleExtra("miLongitud",0);

//        Log.i("MENSAJES", getClass() + " Estas en  "  + miLatitud + " - " + miLongitud );


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
            args.putDouble("miLatitud",miLatitud);
            args.putDouble("miLongitud",miLongitud);


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
