package ProyectoFinal.Banco.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;

import ProyectoFinal.Banco.dao.CuentaBancaria;

/**
 * Interfaz que define un repositorio para la entidad {@link CuentaBancaria}. Extiende
 * la interfaz JpaRepository de Spring Data para realizar operaciones CRUD y
 * otras consultas relacionadas con la entidad CuentaBancaria en la base de datos.
 */
public interface CuentaRepositorio extends JpaRepository<CuentaBancaria, Long>{

    /**
     * Busca la primera cuenta bancaria que tenga el código IBAN especificado.
     * @param codigoIban El código IBAN de la cuenta bancaria a buscar.
     * @return La primera cuenta bancaria encontrada con el código IBAN especificado.
     */
    public CuentaBancaria findFirstBycodigoIban(String codigoIban);
}