package modelo;

import android.provider.BaseColumns;

/**
 * Created by tributo on 31/12/14.
 */
public class PuntoRuta implements BaseColumns {

    private String idPuntoInteres="";
    private String idRuta="";
    private String orden="";


    public static final String COL_ID = "_id";
    public static final String TABLE_NAME = "puntosRutas";
    public static final String COLUMN_NAME_ID_PUNTO = "idPunto";
    public static final String COLUMN_NAME_ID_RUTA = "idRuta";
    public static final String COLUMN_NAME_ORDEN= "orden";


    public String getIdPuntoInteres() {
        return idPuntoInteres;
    }

    public void setIdPuntoInteres(String idPuntoInteres) {
        this.idPuntoInteres = idPuntoInteres;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }
}
