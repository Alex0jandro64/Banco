package ProyectoFinal.Banco.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.Oficina;
import ProyectoFinal.Banco.repositorios.OficinaRepositorio;
import jakarta.transaction.Transactional;

/**
 * Servicio que implementa los métodos de la interfaz {@link IOficinaServicio}
 * para la gestión de oficinas.
 */
@Service
@Transactional
public class OficinaServicioImpl implements IOficinaServicio {

    @Autowired
    private OficinaRepositorio oficinaRepositorio;
	
    /**
     * Obtiene todas las oficinas.
     * 
     * @return Una lista de todas las oficinas registradas.
     */
    @Override
    public List<Oficina> obtenerOficinas() {
        try {
            return oficinaRepositorio.findAll();
        } catch (Exception e) {
            System.out.println("[Error en OficinaServicioImpl - obtenerOficinas()]: " + e.getMessage());
            return null; // Devuelve null en caso de error
        }
    }
}