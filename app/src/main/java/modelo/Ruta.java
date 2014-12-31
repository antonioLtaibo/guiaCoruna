package modelo;

import android.provider.BaseColumns;

/**
 * Created by tributo on 31/12/14.
 */
public class Ruta implements BaseColumns {


    private String idRuta="";
    private String nombre="";
    private Integer id;


    public static final String COL_ID = "_id";
    public static final String TABLE_NAME = "Rutas";
    public static final String COLUMN_NAME_NOMBRE= "nombre";
    public static final String COLUMN_NAME_ID_RUTA = "idRuta";

    public String getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
