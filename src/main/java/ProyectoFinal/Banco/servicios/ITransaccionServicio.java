package ProyectoFinal.Banco.servicios;

import java.util.List;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dto.TransaccionDTOString;

/**
 * Interfaz del servicio para la gestión de transacciones, donde se declaran los
 * métodos correspondientes que serán implementados.
 */
public interface ITransaccionServicio {

	/**
	 * Obtiene todas las transacciones asociadas a un usuario.
	 * 
	 * @param usuarioId El identificador de la cuenta bancaria del usuario.
	 * @return Una lista de transacciones asociadas al usuario.
	 */
	public List<Transaccion> obtenerTransaccionesDeUsuario(CuentaBancaria usuarioId);
	
	/**
	 * Registra una nueva transacción.
	 * 
	 * @param transaccionDTOString El objeto de transferencia de datos de la transacción.
	 * @return La transacción registrada.
	 */
	public Transaccion registrarTransaccion(TransaccionDTOString transaccionDTOString);
}
