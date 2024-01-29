package ProyectoFinal.Banco.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.repositorios.TransaccionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransaccionServicioImpl implements ITransaccionServicio{

	
	@Autowired
    private TransaccionRepository transaccionRepository;
	
	@Override
	public List<Transaccion> obtenerTransaccionesDeUsuario(Long usuarioId) {
        // Consultar todas las transacciones donde el usuario sea remitente o destinatario
        return transaccionRepository.findByUsuarioTransaccionRemitenteIdOrUsuarioTransaccionDestinatarioId(usuarioId, usuarioId);
    
    }
	}
