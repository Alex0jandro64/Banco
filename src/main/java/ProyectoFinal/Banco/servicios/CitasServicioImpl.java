package ProyectoFinal.Banco.servicios;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ProyectoFinal.Banco.dao.Cita;
import ProyectoFinal.Banco.dao.Oficina;
import ProyectoFinal.Banco.dto.CitaDTOLong;
import ProyectoFinal.Banco.repositorios.CitaRepositorio;
import ProyectoFinal.Banco.repositorios.OficinaRepositorio;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CitasServicioImpl implements ICitasServicio{

	 @Autowired
	 private CitaRepositorio citaRepository;
	 
	 @Autowired
	 private OficinaRepositorio oficinaRepository;
	 
	@Override
    public int registrarCita(CitaDTOLong citaDTOLong) {
        try {
        	
        	Cita citaDao = new Cita();
        	Oficina oficina = oficinaRepository.findByIdOficina(citaDTOLong.getOficinaCita());
        	citaDao.setOficinaCita(oficina);
        	citaDao.setUsuarioCita(citaDTOLong.getUsuarioCita());
        	citaDao.setFechaCita(convertToCalendar(citaDTOLong.getFechaCita()));
        	citaDao.setMotivoCita(citaDTOLong.getMotivoCita());
        	
            int error=0;
            	citaRepository.save(citaDao);

            return error;
        } catch (Exception e) {
            System.out.println("[Error en TransaccionServicioImpl - registrarTransaccion()]: " + e.getMessage());
            return -1;
        }
}
	
	public static Calendar convertToCalendar(LocalDateTime localDateTime) {
        // Obtener la zona horaria predeterminada del sistema
        ZoneId zoneId = ZoneId.systemDefault();
        
        // Convertir LocalDateTime a Instant y luego a Millis
        long millis = localDateTime.atZone(zoneId).toInstant().toEpochMilli();

        // Crear un objeto Calendar y establecer la fecha y hora
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return calendar;
    }

}

