package at.ums.luna.friedhofums.servidor;

/**
 * Created by luna-aleixos on 03.08.2016.
 */
public class DBValores {

    interface Tablas{
        String GRAB = "Grab";
        String ARBEIT_KOPF = "ArbeitKopf";
    }

    interface ColumnasGrab{
        String ID_GRAB = "idGrab";
        String GRABNAME = "grabname";
        String GRABART = "grabart";
        String FRIEDHOF = "friedhof";
        String KUNDE = "kunde";
        String TELEFON1 = "telefon1";
        String TELEFON2 = "telefon2";
        String BEMERKUNG = "bemerkung";
        String FELD = "feld";
        String REIHE = "reie";
        String NUMMER = "nummer";
        String LATITUD = "latitud";
        String LONGITUD = "longitud";
    }

    interface ColumnasArbeitKopf{
        String TITLE = "title";
        String MITARBEITER = "mitarbeiter";
        String FECHA = "fecha";
        String TERMINADO = "terminado";
    }
}
