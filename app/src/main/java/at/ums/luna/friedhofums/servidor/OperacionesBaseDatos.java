package at.ums.luna.friedhofums.servidor;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.modelo.ArbeitKopf;
import at.ums.luna.friedhofums.modelo.Grab;

/**
 * Created by luna-aleixos on 03.08.2016.
 */
public class OperacionesBaseDatos {
    DBHelper dbHelper;
    SQLiteDatabase db;
    int contadorProgreso;

    public OperacionesBaseDatos(Context context){
        dbHelper = new DBHelper(context);
    }

    public void abrir(){
        Log.i("JUANJO", "BD abierta para escribir");
        db = dbHelper.getWritableDatabase();
        // db.execSQL("PRAGMA foreign_keys=ON");
    }

    public void leer(){
        Log.i("JUANJO", "BD abierta para leer");
        db = dbHelper.getReadableDatabase();
        // db.execSQL("PRAGMA foreign_keys=ON");
    }

    public void cerrar(){
        Log.i("JUANJO", "BD cerrada");
        dbHelper.close();
    }


    /**
     * GRAB
     */
    public static final String[] todasColumnasGrab = {
            DBValores.ColumnasGrab.ID_GRAB,
            DBValores.ColumnasGrab.GRABNAME,
            DBValores.ColumnasGrab.GRABART,
            DBValores.ColumnasGrab.FRIEDHOF,
            DBValores.ColumnasGrab.FELD,
            DBValores.ColumnasGrab.REIHE,
            DBValores.ColumnasGrab.NUMMER,
            DBValores.ColumnasGrab.BEMERKUNG,
            DBValores.ColumnasGrab.KUNDE,
            DBValores.ColumnasGrab.TELEFON1,
            DBValores.ColumnasGrab.TELEFON2,
            DBValores.ColumnasGrab.LATITUD,
            DBValores.ColumnasGrab.LONGITUD
    };

    public ArrayList<Grab> verListaGrabFiltrada(String filtro, String[] argumentos){
        leer();

        Cursor cursor = db.query(DBValores.Tablas.GRAB,todasColumnasGrab,filtro,argumentos,null,null,null,null);

        ArrayList<Grab> listaGrab = new ArrayList<>();
        while (cursor.moveToNext()){
            Grab grab = new Grab();
            grab.setIdGrab(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.ID_GRAB)));
            grab.setGrabname(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.GRABNAME)));
            grab.setGrabart(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.GRABART)));
            grab.setFriedhof(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.FRIEDHOF)));
            grab.setFeld(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.FELD)));
            grab.setReihe(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.REIHE)));
            grab.setNummer(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.NUMMER)));
            grab.setKunde(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.KUNDE)));
            grab.setTelefon1(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.TELEFON1)));
            grab.setTelefon2(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.TELEFON2)));
            grab.setBemerkung(cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.BEMERKUNG)));
            grab.setLatitud(cursor.getDouble(cursor.getColumnIndex(DBValores.ColumnasGrab.LATITUD)));
            grab.setLongitud(cursor.getDouble(cursor.getColumnIndex(DBValores.ColumnasGrab.LONGITUD)));
            listaGrab.add(grab);
        }
        cerrar();
        cursor.close();
        return listaGrab;

    }

    public void actualizaCoordenadasTumba(String idGrab, final double miLatitud, final double miLongitud){
        abrir();

        String[] args = {idGrab};

        ContentValues actualizar = new ContentValues();
        actualizar.put(DBValores.ColumnasGrab.LATITUD, miLatitud);
        actualizar.put(DBValores.ColumnasGrab.LONGITUD, miLongitud);

        db.update(DBValores.Tablas.GRAB, actualizar ,
                DBValores.ColumnasGrab.ID_GRAB + "=?", args);

        cerrar();

        //conecta Backendless

        String whereClause = "idGrab = '" + idGrab + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Backendless.Persistence.of(Grab.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Grab>>() {
            @Override
            public void handleResponse(BackendlessCollection<Grab> grabBackendlessCollection) {
                final Grab tumbaObtenida = grabBackendlessCollection.getCurrentPage().get(0);

                Log.i("MENSAJES", tumbaObtenida.getIdGrab() + " " + tumbaObtenida.getGrabname());

                tumbaObtenida.setLatitud(miLatitud);
                tumbaObtenida.setLongitud(miLongitud);

                Backendless.Persistence.save(tumbaObtenida, new AsyncCallback<Grab>() {
                    @Override
                    public void handleResponse(Grab grab) {
                        Log.i("MENSAJES", String.format("La tumba %s tiene la posicion %s - %s",
                                tumbaObtenida.getIdGrab(), tumbaObtenida.getLatitud(),tumbaObtenida.getLongitud()));

                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });

    }

    public ArrayList<String> datosCampoTumbas(String[] nombreColumna){
        leer();
        Cursor cursor = db.query(DBValores.Tablas.GRAB,nombreColumna,null,null,null,null,null);
        cursor.moveToFirst();
        ArrayList<String> lista = new ArrayList<String>();
        do{
            lista.add(cursor.getString(0));
        }while (cursor.moveToNext());

        return lista;
    }

    public Grab obtenerGrabAlElegirEnDialogo(String textoObtenido) {
        leer();

        String[] columnasAMostrar = {DBValores.ColumnasGrab.ID_GRAB};
        String[] args ={textoObtenido};
        String criterioSeleccion = DBValores.ColumnasGrab.GRABNAME + "=?";

        Cursor cursor = db.query(DBValores.Tablas.GRAB,columnasAMostrar,criterioSeleccion,args,null,null,null);

        cursor.moveToFirst();

        String resultado = cursor.getString(cursor.getColumnIndex(DBValores.ColumnasGrab.ID_GRAB));

        cursor.close();

        String[] args1 ={resultado};
        String criterioSeleccion1 = DBValores.ColumnasGrab.ID_GRAB + "=?";

        Cursor cursor1 = db.query(DBValores.Tablas.GRAB,todasColumnasGrab,criterioSeleccion1,args1,null,null,null);

        cursor1.moveToFirst();

        Grab grab = new Grab();
        grab.setIdGrab(cursor1.getString((cursor1.getColumnIndex(DBValores.ColumnasGrab.ID_GRAB))));
        grab.setGrabname(cursor1.getString((cursor1.getColumnIndex(DBValores.ColumnasGrab.GRABNAME))));

        cursor1.close();
        cerrar();
        return grab;
    }

    public void actualizarGrab(final Context context){

        //Prepara la barra de progreso
        final ProgressDialog progress;
        progress =new ProgressDialog(context);
        progress.setMessage("Suchen Grabe auf Server...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgress(0);
        progress.setCancelable(false);
        contadorProgreso = 0;


        abrir();

        //borra los datos
       db.delete(DBValores.Tablas.GRAB, null, null);


        //actualiza los datos


        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setPageSize(PAGESIZE);
        queryOptions.addSortByOption("idGrab ASC");
        dataQuery.setQueryOptions(queryOptions);


        Backendless.Persistence.of(Grab.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Grab>>() {

            private int offset = 0;
            private boolean firstResponse =true;
            private List<Grab> listaGrabe;

            @Override
            public void handleResponse(BackendlessCollection<Grab> tumbassObtenidas) {

                if(firstResponse){
                    Log.i("JUANJO", String.valueOf(tumbassObtenidas.getTotalObjects()));
                    progress.setMax(tumbassObtenidas.getTotalObjects());
                    progress.show();
                    firstResponse = false;
                }

                int size = tumbassObtenidas.getCurrentPage().size();
                Log.i("JUANJO", "Cargados " + size + " clientes en la pagina actual");
                if (size > 0)
                {
                    offset+= tumbassObtenidas.getCurrentPage().size();
                    tumbassObtenidas.getPage(PAGESIZE,offset,this);

                    listaGrabe = tumbassObtenidas.getCurrentPage();
                    ContentValues valores = new ContentValues();


                    for(int i= 0;i<size;i++) {

                        valores.put(DBValores.ColumnasGrab.ID_GRAB, listaGrabe.get(i).getIdGrab());
                        valores.put(DBValores.ColumnasGrab.GRABNAME, listaGrabe.get(i).getGrabname());
                        valores.put(DBValores.ColumnasGrab.GRABART, listaGrabe.get(i).getGrabart());
                        valores.put(DBValores.ColumnasGrab.FRIEDHOF, listaGrabe.get(i).getFriedhof());
                        valores.put(DBValores.ColumnasGrab.FELD, listaGrabe.get(i).getFeld());
                        valores.put(DBValores.ColumnasGrab.REIHE, listaGrabe.get(i).getReihe());
                        valores.put(DBValores.ColumnasGrab.NUMMER, listaGrabe.get(i).getNummer());
                        valores.put(DBValores.ColumnasGrab.KUNDE, listaGrabe.get(i).getKunde());
                        valores.put(DBValores.ColumnasGrab.TELEFON1, listaGrabe.get(i).getTelefon1());
                        valores.put(DBValores.ColumnasGrab.TELEFON2, listaGrabe.get(i).getTelefon2());
                        valores.put(DBValores.ColumnasGrab.BEMERKUNG, listaGrabe.get(i).getBemerkung());
                        valores.put(DBValores.ColumnasGrab.LATITUD, listaGrabe.get(i).getLatitud());
                        valores.put(DBValores.ColumnasGrab.LONGITUD, listaGrabe.get(i).getLongitud());

                        db.insert(DBValores.Tablas.GRAB,null,valores);
                        Log.i("MENSAJES", "Tumba " + listaGrabe.get(i).getIdGrab());

                        progress.setProgress(contadorProgreso);
                        contadorProgreso++;
                    }
                } else{
                    progress.hide();
                }

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("JUANJO", backendlessFault.getMessage());

            }
        });

    }

    /**
     * ARBEIT_KOPF
     */

    public static final String[] todasColumnasArbeit = {
            DBValores.ColumnasArbeitKopf.TITLE,
            DBValores.ColumnasArbeitKopf.MITARBEITER,
            DBValores.ColumnasArbeitKopf.FECHA,
            DBValores.ColumnasArbeitKopf.TERMINADO
    };


    public ArrayList<ArbeitKopf> verArbeitListFiltrada(String filtro, String[] argumentos){
        final ArrayList<ArbeitKopf> listaTrabajos = new ArrayList<>();

//        Valores para intentar controlar el tiempo de carga


        final int PAGESIZE = 100;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setPageSize(PAGESIZE);
        queryOptions.addSortByOption("fecha ASC");
        dataQuery.setQueryOptions(queryOptions);


        Backendless.Persistence.of(ArbeitKopf.class).find(dataQuery, new AsyncCallback<BackendlessCollection<ArbeitKopf>>() {

            private int offset = 0;
            private boolean firstResponse =true;

            @Override
            public void handleResponse(BackendlessCollection<ArbeitKopf> trabajosObtenidos) {

                if(firstResponse){

                    Log.i("JUANJO", String.valueOf(trabajosObtenidos.getTotalObjects()));
                    firstResponse = false;
                }

                int size = trabajosObtenidos.getCurrentPage().size();
                Log.i("JUANJO", "Cargados " + size + " trabajos en la pagina actual");
                if (size > 0)
                {
                    offset+= trabajosObtenidos.getCurrentPage().size();
                    trabajosObtenidos.getPage(PAGESIZE,offset,this);

                    for (ArbeitKopf cl : trabajosObtenidos.getCurrentPage()){
                        listaTrabajos.add(cl);
                    }
                    trabajosObtenidos.getCurrentPage();
                } else {
                    Log.i("JUANJO", "se van a obtener FINAL " + listaTrabajos.size() + " registros");

                }

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i("JUANJO","Error num " + backendlessFault.getCode());
            }
        });



        Log.i("JUANJO", "DATOS QUE VOY A DEVOLVER " + listaTrabajos.size() + " registros");
        return listaTrabajos;
    }

}

