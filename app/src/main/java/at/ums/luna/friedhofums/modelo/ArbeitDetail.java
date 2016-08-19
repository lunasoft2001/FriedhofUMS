package at.ums.luna.friedhofums.modelo;

/**
 * Created by luna-aleixos on 19.08.2016.
 */
public class ArbeitDetail {

    private String objectId;
    private String detalle;
    private Grab grab;


    public ArbeitDetail() {
    }

    public ArbeitDetail(String objectId, String detalle, Grab grab, String idGrab) {
        this.objectId = objectId;
        this.detalle = detalle;
        this.grab = grab;
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

}
