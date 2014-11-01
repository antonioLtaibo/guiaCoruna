package es.udc.psi14.grupal.guiacoruna;

import java.util.List;

/**
 * Created by tributo on 29/10/14.
 */
public interface ModelInterface {

    //Se devuelve si se pudo o no insertar el elemento
    public boolean addPuntoInteres(PuntoInteresContainer pi);

    //Se usará el nombre como identificador.
    public PuntoInteresContainer findByName(String nombre);

    //Se usa el  identificador.
    public PuntoInteresContainer findByID(String id);

    //Se piden todos los puntos de interes de un tipo
    //Los tipos son constantes predefinidas
    public List<PuntoInteresContainer> findByType(String tipo);

    public List<PuntoInteresContainer> getAll();

}
