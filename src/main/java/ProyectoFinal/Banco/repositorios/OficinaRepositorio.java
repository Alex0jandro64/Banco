package ProyectoFinal.Banco.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import ProyectoFinal.Banco.dao.Oficina;

public interface OficinaRepositorio extends JpaRepository<Oficina, Long>{ 

	public Oficina findByIdOficina(Long idOficina);
}
