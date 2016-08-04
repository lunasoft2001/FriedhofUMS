package at.ums.luna.friedhofums.servidor;

import at.ums.luna.friedhofums.servidor.DBValores.Tablas;
import at.ums.luna.friedhofums.servidor.DBValores.ColumnasGrab;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by luna-aleixos on 03.08.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static int version = 1;
    private static String nombreBD = "friedhof";
    private static SQLiteDatabase.CursorFactory factory = null;

    public DBHelper(Context context){
        super(context, nombreBD, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(this.getClass().toString(), "Creando base de datos");

        /**
         * Creamos la tabla tumbas
         */


        db.execSQL("CREATE TABLE " + Tablas.GRAB +
                "( " +
                ColumnasGrab.ID_GRAB + " INT PRIMARY KEY," +
                ColumnasGrab.GRABNAME + " TEXT," +
                ColumnasGrab.GRABART + " TEXT," +
                ColumnasGrab.FRIEDHOF + " TEXT," +
                ColumnasGrab.KUNDE + " TEXT," +
                ColumnasGrab.TELEFON1 + " TEXT," +
                ColumnasGrab.TELEFON2 + " TEXT," +
                ColumnasGrab.FELD + " TEXT," +
                ColumnasGrab.REIHE + " TEXT," +
                ColumnasGrab.NUMMER + " TEXT," +
                ColumnasGrab.LATITUD + " DOUBLE," +
                ColumnasGrab.LONGITUD + " DOUBLE," +
                ColumnasGrab.BEMERKUNG + " TEXT)");

        db.execSQL("CREATE UNIQUE INDEX _id ON " + Tablas.GRAB + "(" + ColumnasGrab.ID_GRAB + " ASC)");

        Log.i(this.getClass().toString(), "La tabla " + Tablas.GRAB + " ha sido creada");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.GRAB);

        onCreate(db);

    }
}
