package at.ums.luna.friedhofums.modelo;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import at.ums.luna.friedhofums.R;

/**
 * Created by luna-aleixos on 19.08.2016.
 */
public class ArbeitDetail{

    private String objectId;
    private String detalle;
    private Grab grab;
    private String idGrab;
    private String tierra;
    private String recoger;
    private String regar;
    private String plantar;
    private String pflege;
    private String observaciones;
    private String limpiar;
    private String decorar;
    private int cantidad;


    public ArbeitDetail() {
    }

    public ArbeitDetail(String objectId, String detalle, Grab grab, String idGrab, String tierra,
                        String recoger, String regar, String plantar, String pflege,
                        String observaciones, String limpiar, String decorar, int cantidad) {
        this.objectId = objectId;
        this.detalle = detalle;
        this.grab = grab;
        this.idGrab = idGrab;
        this.tierra = tierra;
        this.recoger = recoger;
        this.regar = regar;
        this.plantar = plantar;
        this.pflege = pflege;
        this.observaciones = observaciones;
        this.limpiar = limpiar;
        this.decorar = decorar;
        this.cantidad = cantidad;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Grab getGrab() {
        return grab;
    }

    public void setGrab(Grab grab) {
        this.grab = grab;
    }

    public String getIdGrab() {
        return idGrab;
    }

    public void setIdGrab(String idGrab) {
        this.idGrab = idGrab;
    }

    public String getTierra() {
        return tierra;
    }

    public void setTierra(String tierra) {
        this.tierra = tierra;
    }

    public String getRecoger() {
        return recoger;
    }

    public void setRecoger(String recoger) {
        this.recoger = recoger;
    }

    public String getRegar() {
        return regar;
    }

    public void setRegar(String regar) {
        this.regar = regar;
    }

    public String getPlantar() {
        return plantar;
    }

    public void setPlantar(String plantar) {
        this.plantar = plantar;
    }

    public String getPflege() {
        return pflege;
    }

    public void setPflege(String pflege) {
        this.pflege = pflege;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getLimpiar() {
        return limpiar;
    }

    public void setLimpiar(String limpiar) {
        this.limpiar = limpiar;
    }

    public String getDecorar() {
        return decorar;
    }

    public void setDecorar(String decorar) {
        this.decorar = decorar;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * GETTERS DE ICONOS DE TAREAS
     */

    public Float transparenciaTarea(String tarea){
        Float transparencia;
        switch (tarea){
            case "JA":
                transparencia = 1.0f;
                break;
            case "NEIN":
                transparencia = 0.0f;
                break;
            default:
                transparencia = 0.2f;
                break;
        }

        return transparencia;
    }

}
