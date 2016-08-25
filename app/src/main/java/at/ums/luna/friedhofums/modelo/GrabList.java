package at.ums.luna.friedhofums.modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by luna-aleixos on 24.08.2016.
 */
public class GrabList implements Serializable {

    private ArrayList<Grab> grabList;

    public GrabList(){

    }

    public GrabList(ArrayList<Grab> grabList) {
        this.grabList = grabList;
    }

    public ArrayList<Grab> getGrabList() {
        return grabList;
    }

    public void setGrabList(ArrayList<Grab> grabList) {
        this.grabList = grabList;
    }
}
