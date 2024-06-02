package ProyectoFinal.Banco.servicios;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Prestamo;
import ProyectoFinal.Banco.dto.PrestamoDTO;
import ProyectoFinal.Banco.repositorios.CuentaRepositorio;
import ProyectoFinal.Banco.repositorios.PrestamoRepositorio;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrestamoServicioImpl implements IPrestamoServicio{

	@Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private CuentaRepositorio cuentaRepository;
    
	@Override
    public int registrarPrestamo(PrestamoDTO prestamoDTO) {
        try {
            CuentaBancaria cuentaPrestamo = cuentaRepository.findFirstBycodigoIban(prestamoDTO.getCuentaPrestamo());
            
            
            Prestamo prestamo = new Prestamo();
            prestamo.setCantidadPrestamo(prestamoDTO.getCantidadPrestamo());
            prestamo.setCuentaBancariaPrestamo(cuentaPrestamo);
            prestamo.setFechaPrestamo(Calendar.getInstance());
            prestamo.setMotivoPrestamo(prestamoDTO.getMotivoPrestamo());
            
            cuentaPrestamo.setSaldoCuenta(cuentaPrestamo.getSaldoCuenta() + prestamoDTO.getCantidadPrestamo());
            cuentaRepository.save(cuentaPrestamo);
            prestamoRepositorio.save(prestamo);
            
            return 1; // Transacción exitosa
        } catch (Exception e) {
            System.out.println("[Error en PrestamoServicioImpl - registrarPrestamo()]: " + e.getMessage());
            return -1; // Código de error para error general
        }
    }
}
