package ProyectoFinal.Banco.servicios;

import java.util.List;

import ProyectoFinal.Banco.dao.Cita;
import ProyectoFinal.Banco.dto.CitaDTO;
import ProyectoFinal.Banco.dto.CitaDTOLong;

public interface ICitasServicio {

	public int registrarCita(CitaDTOLong citaDTOString);
	public Cita eliminar(long id);
	public List<CitaDTO> buscarTodos();
}
