package ProyectoFinal.Banco.servicios;

import java.util.List;

import ProyectoFinal.Banco.dao.Cita;
import ProyectoFinal.Banco.dto.CitaDTO;
import ProyectoFinal.Banco.dto.CitaDTOLong;

/**
 * Interfaz del servicio para la gestión de citas bancarias.
 */
public interface ICitasServicio {

    /**
     * Registra una nueva cita bancaria.
     * 
     * @param citaDTOString El DTO de la cita bancaria a registrar.
     * @return Un código de estado que indica el resultado de la operación de registro:
     *         1 si la cita se registró correctamente,
     *         Otro valor si ocurrió un error durante la operación.
     */
    public int registrarCita(CitaDTOLong citaDTOString);
    public int editarCita(CitaDTOLong citaDTOLong);
    /**
     * Elimina una cita bancaria.
     * 
     * @param id El identificador de la cita bancaria a eliminar.
     * @return La cita bancaria eliminada, o null si no se encuentra ninguna cita con el identificador proporcionado.
     */
    public Cita eliminar(long id);
    
    /**
     * Busca todas las citas bancarias.
     * 
     * @return Una lista de todas las citas bancarias registradas.
     */
    public List<CitaDTO> buscarTodos();
}
