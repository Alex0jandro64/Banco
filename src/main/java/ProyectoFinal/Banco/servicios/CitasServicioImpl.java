package ProyectoFinal.Banco.servicios;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.Cita;
import ProyectoFinal.Banco.dao.Oficina;
import ProyectoFinal.Banco.dto.CitaDTO;
import ProyectoFinal.Banco.dto.CitaDTOLong;
import ProyectoFinal.Banco.repositorios.CitaRepositorio;
import ProyectoFinal.Banco.repositorios.OficinaRepositorio;

import jakarta.transaction.Transactional;

/** 
 * Servicio para gestionar citas
 */
@Service
@Transactional
public class CitasServicioImpl implements ICitasServicio {

    @Autowired
    private CitaRepositorio citaRepository;

    @Autowired
    private OficinaRepositorio oficinaRepository;

    /** 
     * Registra una nueva cita
     * @param citaDTOLong Datos de la cita a registrar
     * @return 0 si la operación se realizó correctamente, -1 si hubo un error
     */
    @Override
    public int registrarCita(CitaDTOLong citaDTOLong) {
        try {
            Cita citaDao = new Cita();
            Oficina oficina = oficinaRepository.findByIdOficina(citaDTOLong.getOficinaCita());
            citaDao.setOficinaCita(oficina);
            citaDao.setUsuarioCita(citaDTOLong.getUsuarioCita());
            citaDao.setFechaCita(convertToCalendar(citaDTOLong.getFechaCita()));
            citaDao.setMotivoCita(citaDTOLong.getMotivoCita());

            int error = 0;
            citaRepository.save(citaDao);
            System.out.println("Cita registrada");
            return error;
        } catch (Exception e) {
            System.out.println("[Error en CitasServicioImpl - registrarCita()]: " + e.getMessage());
            return -1; // Indica un error
        }
    }
    
    @Override
    public int editarCita(CitaDTOLong citaDTOLong) {
        try {
            Cita citaDao = citaRepository.findFirstByIdCita(citaDTOLong.getIdCita());
            Oficina oficina = oficinaRepository.findByIdOficina(citaDTOLong.getOficinaCita());
            citaDao.setOficinaCita(oficina);
            citaDao.setFechaCita(convertToCalendar(citaDTOLong.getFechaCita()));
            citaDao.setMotivoCita(citaDTOLong.getMotivoCita());

            int error = 0;
            citaRepository.save(citaDao);
            System.out.println("Cita modificada");
            return error;
        } catch (Exception e) {
            System.out.println("[Error en CitasServicioImpl - editarCita()]: " + e.getMessage());
            return -1; // Indica un error
        }
    }

    /** 
     * Elimina una cita por su ID
     * @param id ID de la cita a eliminar
     * @return La cita eliminada, o null si no se encontró la cita o hubo un error
     */
    @Override
    public Cita eliminar(long id) {
        try {
            Cita cita = citaRepository.findById(id).orElse(null);
            if (cita != null) {
                citaRepository.delete(cita);
            }
            return cita;
        } catch (Exception e) {
            System.out.println("[Error en CitasServicioImpl - eliminar()]: " + e.getMessage());
            return null; // Manejo de error: devolver null si ocurre una excepción
        }
    }

    /** 
     * Busca y retorna todas las citas
     * @return Lista de todas las citas encontradas, o null si hubo un error
     */
    @Override
    public List<CitaDTO> buscarTodos() {
        try {
            return Util.listaCitasToDto(citaRepository.findAll());
        } catch (Exception e) {
            System.out.println("[Error en CitasServicioImpl - buscarTodos()]: " + e.getMessage());
            return null; // Manejo de error: devolver null si ocurre una excepción
        }
    }

    /** 
     * Convierte LocalDateTime a Calendar
     * @param localDateTime LocalDateTime a convertir
     * @return Objeto Calendar representando el mismo instante de tiempo que el LocalDateTime proporcionado
     */
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