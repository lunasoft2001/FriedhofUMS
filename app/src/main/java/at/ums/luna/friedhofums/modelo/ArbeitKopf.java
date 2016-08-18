package at.ums.luna.friedhofums.modelo;

import java.util.Date;

/**
 * Created by luna-aleixos on 16.08.2016.
 */
public class ArbeitKopf {

    private String title;
    private String mitarbeiter;
    private Date fecha;
    private boolean terminado;

    public ArbeitKopf() {}

    public ArbeitKopf(String title, String mitarbeiter, Date fecha, boolean terminado) {
        this.title = title;
        this.mitarbeiter = mitarbeiter;
        this.fecha = fecha;
        this.terminado = terminado;
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
}
