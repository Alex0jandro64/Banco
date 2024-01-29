package ProyectoFinal.Banco.servicios;

import java.util.List;

import ProyectoFinal.Banco.dao.Transaccion;

public interface ITransaccionServicio {

	public List<Transaccion> obtenerTransaccionesDeUsuario(Long usuarioId);
}
