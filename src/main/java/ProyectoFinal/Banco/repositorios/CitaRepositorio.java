package ProyectoFinal.Banco.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import ProyectoFinal.Banco.dao.Cita;

/**
 * Repositorio para la entidad Cita, que extiende JpaRepository para operaciones CRUD.
 */
public interface CitaRepositorio extends JpaRepository<Cita, Long>{

}