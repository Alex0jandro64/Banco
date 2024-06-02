package ProyectoFinal.Banco.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import ProyectoFinal.Banco.dao.Prestamo;

public interface PrestamoRepositorio extends JpaRepository<Prestamo, Long>{ 

}
