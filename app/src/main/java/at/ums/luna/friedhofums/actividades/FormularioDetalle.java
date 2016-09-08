package at.ums.luna.friedhofums.actividades;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.ArbeitDetail;

public class FormularioDetalle extends AppCompatActivity implements FormularioDetalleFragment.OnHeadlineSelectedListener{

    private String idGrab;
    private String idObjeto;

    private FormularioDetalleFragment formularioDetalleFragment;
    private MapaDetailFragment mapaDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_detalle);

        idGrab = getIntent().getStringExtra("idGrab");
        idObjeto = getIntent().getStringExtra("idObjeto");

        mapaDetailFragment = new MapaDetailFragment();
        formularioDetalleFragment = new FormularioDetalleFragment();

        ViewPager pager =(ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AdaptadorPager(getSupportFragmentManager()));

    }


    /**
     * para recibir y enviar informacion a los fragmentos
     * @param lista
     */
    @Override
    public void onListaObtenida(ArrayList<ArbeitDetail> lista) {


            mapaDetailFragment.borrarMarcas();
            mapaDetailFragment.recibeListadoTumbas(lista);
            mapaDetailFragment.actualizaMarcas();

    }

    /**
     * Fin enviar informacion entre fragmentos
     */






    private class AdaptadorPager extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return idGrab;
                case 1:
                    return getString(R.string.MAPA);
                default:
                    return "---";
            }
        }
        public AdaptadorPager(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString("idObjeto",idObjeto);
            args.putString("origen", "formulario");


            switch (position) {
                case 0:
                    formularioDetalleFragment = new FormularioDetalleFragment();
                    formularioDetalleFragment.setArguments(args);
                    return formularioDetalleFragment;
                case 1:
                    mapaDetailFragment = new MapaDetailFragment();
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
