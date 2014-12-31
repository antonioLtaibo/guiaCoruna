package modelo;

import java.util.List;

import util.InvalidPIException;

/**
 * Created by tributo on 29/10/14.
 */
public interface ModelInterface {

    //Se devuelve si se pudo o no insertar el elemento
    public boolean addPuntoInteres(PuntoInteres pi) throws InvalidPIException;

    public boolean removePuntoInteres(String id);

    //Se usará el nombre como identificador.
    public PuntoInteres findByName(String nombre);

    //Se usa el  identificador.
    public PuntoInteres findByID(String id);

    //Se piden todos los puntos de interes de un tipo
    //Los tipos son constantes predefinidas
    public List<PuntoInteres> findByType(String tipo);

    public List<PuntoInteres> getAll();

    public boolean updatePuntoInteres(PuntoInteres pi) throws InvalidPIException;

}
