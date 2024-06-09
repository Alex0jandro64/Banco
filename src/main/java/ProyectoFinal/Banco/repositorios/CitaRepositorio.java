	package ProyectoFinal.Banco.repositorios;
	
	import org.springframework.data.jpa.repository.JpaRepository;
	
	import ProyectoFinal.Banco.dao.Cita;
	import ProyectoFinal.Banco.dao.CuentaBancaria;
	
	/**
	 * Repositorio para la entidad Cita, que extiende JpaRepository para operaciones CRUD.
	 */
	public interface CitaRepositorio extends JpaRepository<Cita, Long> {
	    public Cita findFirstByIdCita(long id);  // Corregido a "findFirstByIdCita"
	}