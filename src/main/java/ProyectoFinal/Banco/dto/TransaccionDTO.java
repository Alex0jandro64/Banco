package ProyectoFinal.Banco.dto;

import ProyectoFinal.Banco.dao.CuentaBancaria;

public class TransaccionDTO {

	//ATRIBUTOS
	private long idTransaccion;
	private CuentaBancaria usuarioTransaccionRemitente;
	private CuentaBancaria usuarioTransaccionDestinatario;
	private double cantidadTransaccion;
	
	//CONSTRUCTORES
	public TransaccionDTO() {
		super();
	}
	
	public TransaccionDTO(long idTransaccion, CuentaBancaria usuarioTransaccionRemitente,
			CuentaBancaria usuarioTransaccionDestinatario, double cantidadTransaccion) {
		super();
		this.idTransaccion = idTransaccion;
		this.usuarioTransaccionRemitente = usuarioTransaccionRemitente;
		this.usuarioTransaccionDestinatario = usuarioTransaccionDestinatario;
		this.cantidadTransaccion = cantidadTransaccion;
	}
	
	//GETTERS Y SETTERS 
	public long getIdTransaccion() {
		return idTransaccion;
	}
	public void setIdTransaccion(long idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	public CuentaBancaria getUsuarioTransaccionRemitente() {
		return usuarioTransaccionRemitente;
	}
	public void setUsuarioTransaccionRemitente(CuentaBancaria usuarioTransaccionRemitente) {
		this.usuarioTransaccionRemitente = usuarioTransaccionRemitente;
	}
	public CuentaBancaria getUsuarioTransaccionDestinatario() {
		return usuarioTransaccionDestinatario;
	}
	public void setUsuarioTransaccionDestinatario(CuentaBancaria usuarioTransaccionDestinatario) {
		this.usuarioTransaccionDestinatario = usuarioTransaccionDestinatario;
	}
	public double getCantidadTransaccion() {
		return cantidadTransaccion;
	}
	public void setCantidadTransaccion(double cantidadTransaccion) {
		this.cantidadTransaccion = cantidadTransaccion;
	}
	
	
}
