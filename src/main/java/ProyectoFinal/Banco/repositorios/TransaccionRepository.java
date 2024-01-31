package ProyectoFinal.Banco.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Transaccion;

/**
 * Interfaz que define un repositorio para la entidad {@link Transaccion}. Extiende
 * la interfaz JpaRepository de Spring Data para realizar operaciones CRUD y
 * otras consultas relacionadas con la entidad Transaccion en la base de datos.
 */
public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{

    /**
     * Busca todas las transacciones en las que el usuario sea remitente o destinatario.
     * @param remitenteId El identificador de la cuenta bancaria del remitente.
     * @param destinatarioId El identificador de la cuenta bancaria del destinatario.
     * @return Lista de transacciones en las que el usuario sea remitente o destinatario.
     */
    List<Transaccion> findByUsuarioTransaccionRemitenteOrUsuarioTransaccionDestinatario(CuentaBancaria remitenteId, CuentaBancaria destinatarioId);

}
