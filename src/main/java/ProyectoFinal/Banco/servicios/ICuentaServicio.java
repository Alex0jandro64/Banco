package ProyectoFinal.Banco.servicios;

import java.util.List;

import ProyectoFinal.Banco.dao.CuentaBancaria;

public interface ICuentaServicio {

	
	public List<CuentaBancaria> obtenerCuentasDeUsuario(Long usuarioId);
}
