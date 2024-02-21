package ProyectoFinal.Banco.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import ProyectoFinal.Banco.dao.Oficina;

/**
 * Repositorio para la entidad Oficina, que extiende JpaRepository para operaciones CRUD.
 */
public interface OficinaRepositorio extends JpaRepository<Oficina, Long>{ 

    /**
     * Método para encontrar una Oficina por su ID específico.
     * 
     * @param idOficina El ID de la Oficina a buscar.
     * @return La Oficina encontrada, o null si no se encuentra ninguna con el ID dado.
     */
    public Oficina findByIdOficina(Long idOficina);
}