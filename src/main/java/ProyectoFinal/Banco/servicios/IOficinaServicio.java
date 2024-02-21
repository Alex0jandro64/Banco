package ProyectoFinal.Banco.servicios;

import java.util.List;

import ProyectoFinal.Banco.dao.Oficina;

/**
 * Interfaz que define los métodos para el servicio de gestión de oficinas.
 */
public interface IOficinaServicio {

    /**
     * Obtiene todas las oficinas.
     * 
     * @return Una lista de todas las oficinas registradas.
     */
    public List<Oficina> obtenerOficinas();
}