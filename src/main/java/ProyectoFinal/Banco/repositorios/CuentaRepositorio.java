package ProyectoFinal.Banco.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import ProyectoFinal.Banco.dao.CuentaBancaria;

public interface CuentaRepositorio extends JpaRepository<CuentaBancaria, Long>{

}
