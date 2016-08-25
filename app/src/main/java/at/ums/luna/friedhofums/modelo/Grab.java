package at.ums.luna.friedhofums.modelo;

import android.content.Intent;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.Serializable;

import at.ums.luna.friedhofums.R;

/**
 * Created by luna-aleixos on 15.07.2016.
 */
public class Grab {

    private String idGrab;
    private String grabname;
    private String friedhof;
    private String feld;
    private String reihe;
    private String nummer;
    private String kunde;
    private String grabart;
    private String bemerkung;
    private String telefon1;
    private String telefon2;
    private double latitud;
    private double longitud;

    public Grab(){}

    public Grab(String idGrab, String grabname, String friedhof, String feld, String reihe, String nummer,
                String kunde, String grabart, String bemerkung, String telefon1, String telefon2,
                double latitud, double longitud){
        this.idGrab = idGrab;
        this.grabname = grabname;
        this.friedhof = friedhof;
        this.feld = feld;
        this.reihe = reihe;
        this.nummer = nummer;
        this.kunde = kunde;
        this.grabart = grabart;
        this.bemerkung = bemerkung;
        this.telefon1 = telefon1;
        this.telefon2 = telefon2;
        this.latitud = latitud;
        this.longitud = longitud;
    }


    public String getIdGrab() {
        return idGrab;
    }

    public void setIdGrab(String idGrab) {
        this.idGrab = idGrab;
    }

    public String getGrabname() {
        return grabname;
    }

    public void setGrabname(String grabname) {
        this.grabname = grabname;
    }

    public String getFriedhof() {
        return friedhof;
    }

    public void setFriedhof(String friedhof) {
        this.friedhof = friedhof;
    }

    public String getFeld() {
        return feld;
    }

    public void setFeld(String feld) {
        this.feld = feld;
    }

    public String getReihe() {
        return reihe;
    }

    public void setReihe(String reihe) {
        this.reihe = reihe;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getKunde() {
        return kunde;
    }

    public void setKunde(String kunde) {
        this.kunde = kunde;
    }

    public String getGrabart() {
        return grabart;
    }

    public void setGrabart(String grabart) {
        this.grabart = grabart;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public String getTelefon1() {
        return telefon1;
    }

    public void setTelefon1(String telefon1) {
        this.telefon1 = telefon1;
    }

    public String getTelefon2() {
        return telefon2;
    }

    public void setTelefon2(String telefon2) {
        this.telefon2 = telefon2;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public BitmapDescriptor iconoDeTumba(){
        BitmapDescriptor icono;
        switch (grabart){
            case "Einzelgrab":
                icono = BitmapDescriptorFactory.fromResource(R.drawable.grab_normal);
                break;
            case "Doppelgrab":
                icono = BitmapDescriptorFactory.fromResource(R.drawable.grab_doble);
                break;
            case "Urnengrab":
                icono = BitmapDescriptorFactory.fromResource(R.drawable.grab_urne);
                break;
            case "Kindergrab":
                icono = BitmapDescriptorFactory.fromResource(R.drawable.grab_bebe);
                break;
            default:
                icono = BitmapDescriptorFactory.fromResource(R.drawable.grab_vacia);
                break;
        }

        return icono;
    }

    public int rutaDeIcono(){
        int ruta;
        switch (grabart){
            case "Einzelgrab":
                ruta = R.drawable.grab_normal;
                break;
            case "Doppelgrab":
                ruta = R.drawable.grab_doble;
                break;
            case "Urnengrab":
                ruta = R.drawable.grab_urne;
                break;
            case "Kindergrab":
                ruta = R.drawable.grab_bebe;
                break;
            default:
                ruta = R.drawable.grab_vacia;
                break;
        }

        return ruta;
    }
}


