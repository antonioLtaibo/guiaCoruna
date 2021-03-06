package modelo;

import android.provider.BaseColumns;

/**
 * Created by tributo on 5/11/14.
 */
public class PuntoInteres implements BaseColumns {

    private String nombre="";
    private String direccion="";
    private String telefono="";
    private String tipo="";
    private String imageString="";
    private Integer id;
    private String coordenadas="";
    private String detalles="";
    private String url="";
    private String identificador="";


    public static final String COL_ID = "_id";
    public static final String TABLE_NAME = "puntosInteres";
    public static final String COLUMN_NAME_NOMBRE = "nombre";
    public static final String COLUMN_NAME_DIRECCION = "direccion";
    public static final String COLUMN_NAME_TELEFONO = "telefono";
    public static final String COLUMN_NAME_TIPO = "tipo";
    public static final String COLUMN_NAME_IMAGEN = "imagen";
    public static final String COLUMN_NAME_COORDENADAS = "coordenadas";
    public static final String COLUMN_NAME_DETALLES = "detalles";
    public static final String COLUMN_NAME_URL = "url";
    public static final String COLUMN_NAME_IDENTIFICADOR = "identificador";

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) { this.tipo = tipo;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getCoordenadas() {return coordenadas;}

    public void setCoordenadas(String coordenadas) {
        if (coordenadas != null){
            this.coordenadas = coordenadas;
        }
    }

    public String getDetalles() { return detalles;}

    public void setDetalles(String detalles) {
        if (detalles != null){
            this.detalles = detalles;
        }
    }

    public String getUrl() { return url;}

    public void setUrl(String url) {
        if (url != null){
            this.url = url;
        }
    }

    public String getIdentificador(){
        return this.identificador;
    }

    public void setIdentificador(String identificador){
        this.identificador = identificador;
    }


}
