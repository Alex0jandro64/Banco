package ProyectoFinal.Banco.servicios;

import ProyectoFinal.Banco.dao.Cita;
import ProyectoFinal.Banco.dto.CitaDTOLong;

public interface ICitasServicio {

	public int registrarCita(CitaDTOLong citaDTOString);
	public Cita eliminar(long id);
}
