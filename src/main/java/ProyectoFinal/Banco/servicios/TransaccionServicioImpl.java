package ProyectoFinal.Banco.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.repositorios.TransaccionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransaccionServicioImpl implements ITransaccionServicio{

	
	@Autowired
    private TransaccionRepository transaccionRepository;
	
	@Override
	public List<Transaccion> obtenerTransaccionesDeUsuario(CuentaBancaria usuarioId) {
        // Consultar todas las transacciones donde el usuario sea remitente o destinatario
        return transaccionRepository.findByUsuarioTransaccionRemitenteOrUsuarioTransaccionDestinatario(usuarioId, usuarioId);
    }
	
	@Override
	public Transaccion registrarTransaccion(Transaccion transaccion) {

		try {
			transaccion.
			transaccionRepository.save(transaccion);


			return transaccion;
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error TransaccionServicioImpl - registrar() ]" + iae.getMessage());
		} catch (Exception e) {
			System.out.println("[Error TransaccionServicioImpl - registrar() ]" + e.getMessage());
		}
		return null;
	}
	
	}
