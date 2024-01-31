package ProyectoFinal.Banco.servicios;

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

    @Override
    public List<Transaccion> obtenerTransaccionesDeUsuario(CuentaBancaria usuarioId) {
        try {
            // Consultar todas las transacciones donde el usuario sea remitente o destinatario
            return transaccionRepository.findByUsuarioTransaccionRemitenteOrUsuarioTransaccionDestinatario(usuarioId, usuarioId);
        } catch (Exception e) {
            System.out.println("[Error en TransaccionServicioImpl - obtenerTransaccionesDeUsuario()]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Transaccion registrarTransaccion(TransaccionDTOString transaccionDTOString) {
        try {
            CuentaBancaria cuentaDestino = cuentaRepository.findFirstBycodigoIban(transaccionDTOString.getUsuarioTransaccionDestinatario());
            CuentaBancaria cuentaRemitente = cuentaRepository.findFirstBycodigoIban(transaccionDTOString.getUsuarioTransaccionRemitente());
            Transaccion transaccion = new Transaccion();
            transaccion.setCantidadTransaccion(transaccionDTOString.getCantidadTransaccion());
            transaccion.setUsuarioTransaccionDestinatario(cuentaDestino);
            transaccion.setUsuarioTransaccionRemitente(cuentaRemitente);
            
            int error=0;
            if(cuentaRemitente.getSaldoCuenta()-transaccion.getCantidadTransaccion()<0){
            	//Si no tiene saldo suficiente le pongo -1 para posteriormente controlarlo y mostrar mensaje de error
            	//transaccion.setCantidadTransaccion(-1);
            	error =-1;              
            }else if(cuentaDestino == null){
            	
            	//transaccion.setCantidadTransaccion(3);
            	
            }else if(cuentaRemitente.getCodigoIban().equals(cuentaDestino.getCodigoIban())){
            	//Si la cuenta destino y la cuenta remitente es la misma le pongo 2 para posteriormente controlarlo y mostrar mensaje de error
            	//transaccion.setCantidadTransaccion(2);
            	
            }
            else {
            	cuentaRemitente.setSaldoCuenta(cuentaRemitente.getSaldoCuenta()-transaccion.getCantidadTransaccion());
                cuentaDestino.setSaldoCuenta(cuentaDestino.getSaldoCuenta()+transaccion.getCantidadTransaccion());
            	cuentaRepository.save(cuentaDestino);
                cuentaRepository.save(cuentaRemitente);
                transaccionRepository.save(transaccion);
                //transaccion.setCantidadTransaccion(1);

            }
            

            return transaccion;
        } catch (Exception e) {
            System.out.println("[Error en TransaccionServicioImpl - registrarTransaccion()]: " + e.getMessage());
            return null;
        }
    }
}