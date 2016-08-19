package at.ums.luna.friedhofums.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luna-aleixos on 16.08.2016.
 */
public class ArbeitKopf {

    private String title;
    private String mitarbeiter;
    private Date fecha;
    private boolean terminado;
    private List<ArbeitDetail> arbeitDetail;

    private String objectId;

    public ArbeitKopf() {}

    public ArbeitKopf(String title, String mitarbeiter, Date fecha, boolean terminado, String objectId, List<ArbeitDetail> arbeitDetail) {
        this.title = title;
        this.mitarbeiter = mitarbeiter;
        this.fecha = fecha;
        this.terminado = terminado;
        this.objectId = objectId;
        this.arbeitDetail = arbeitDetail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMitarbeiter() {
        return mitarbeiter;
    }

    public void setMitarbeiter(String mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<ArbeitDetail> getArbeitDetail() {
        return arbeitDetail;
    }

    public void setArbeitDetail(List<ArbeitDetail> arbeitDetail) {
        this.arbeitDetail = arbeitDetail;
    }
}
