package ProyectoFinal.Banco.servicios;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dto.TransaccionDTOString;
import ProyectoFinal.Banco.repositorios.CuentaRepositorio;
import ProyectoFinal.Banco.repositorios.TransaccionRepository;
import jakarta.transaction.Transactional;

/**
 * Servicio que implementa los métodos de la interfaz {@link ITransaccionServicio}
 * para la gestión de transacciones.
 */
@Service
@Transactional
public class TransaccionServicioImpl implements ITransaccionServicio {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private CuentaRepositorio cuentaRepository;

    /**
     * Obtiene todas las transacciones de un usuario.
     * 
     * @param usuarioId La cuenta bancaria del usuario.
     * @return Una lista de transacciones del usuario.
     */
    @Override
    public List<Transaccion> obtenerTransaccionesDeUsuario(CuentaBancaria usuarioId) {
        try {
            // Consultar todas las transacciones donde el usuario sea remitente o destinatario
            return transaccionRepository.findByUsuarioTransaccionRemitenteOrUsuarioTransaccionDestinatario(usuarioId, usuarioId);
        } catch (Exception e) {
            System.out.println("[Error en TransaccionServicioImpl - obtenerTransaccionesDeUsuario()]: " + e.getMessage());
            return null; // Devuelve null en caso de error
        }
    }

    /**
     * Registra una nueva transacción.
     * 
     * @param transaccionDTOString El DTO de la transacción.
     * @return Un código de error que indica el resultado de la operación:
     *         1 si la transacción se realizó correctamente,
     *         -1 si ocurrió un error durante la operación.
     */
    @Override
    public int registrarTransaccion(TransaccionDTOString transaccionDTOString) {
        try {
            CuentaBancaria cuentaDestino = cuentaRepository.findFirstBycodigoIban(transaccionDTOString.getUsuarioTransaccionDestinatario());
            CuentaBancaria cuentaRemitente = cuentaRepository.findFirstBycodigoIban(transaccionDTOString.getUsuarioTransaccionRemitente());
            
            // Verificar si la cuenta destino existe
            if (cuentaDestino == null) {
                return 3; // Código de error para cuenta destino no encontrada
            }
            
            // Verificar si la cuenta remitente y destino son la misma
            if (cuentaRemitente.getCodigoIban().equals(cuentaDestino.getCodigoIban())) {
                return 2; // Código de error para cuenta remitente y destino iguales
            }
            
            Transaccion transaccion = new Transaccion();
            transaccion.setCantidadTransaccion(transaccionDTOString.getCantidadTransaccion());
            transaccion.setUsuarioTransaccionDestinatario(cuentaDestino);
            transaccion.setUsuarioTransaccionRemitente(cuentaRemitente);
            
            // Verificar si el remitente tiene saldo suficiente
            if (cuentaRemitente.getSaldoCuenta() - transaccion.getCantidadTransaccion() < 0) {
                return -1; // Código de error para saldo insuficiente
            }
            
            // Actualizar los saldos y guardar la transacción
            cuentaRemitente.setSaldoCuenta(cuentaRemitente.getSaldoCuenta() - transaccion.getCantidadTransaccion());
            cuentaDestino.setSaldoCuenta(cuentaDestino.getSaldoCuenta() + transaccion.getCantidadTransaccion());
            transaccion.setFechaTransaccion(Calendar.getInstance());
            cuentaRepository.save(cuentaDestino);
            cuentaRepository.save(cuentaRemitente);
            transaccionRepository.save(transaccion);
            
            return 1; // Transacción exitosa
        } catch (Exception e) {
            System.out.println("[Error en TransaccionServicioImpl - registrarTransaccion()]: " + e.getMessage());
            return -1; // Código de error para error general
        }
    }
}