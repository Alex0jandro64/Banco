package ProyectoFinal.Banco.dto;

import java.time.LocalDateTime;

import ProyectoFinal.Banco.dao.CuentaBancaria;

/**
 * Clase que representa un objeto de transferencia de datos (DTO) para transacciones.
 */
public class TransaccionDTO {

    // ATRIBUTOS
    private long idTransaccion;
    private CuentaBancaria usuarioTransaccionRemitente;
    private CuentaBancaria usuarioTransaccionDestinatario;
    private double cantidadTransaccion;
    private LocalDateTime fechaTransaccion;

    // CONSTRUCTORES
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

    // GETTERS Y SETTERS 
    public long getIdTransaccion() {
        return idTransaccion;
    }

    public LocalDateTime getFechaTransaccion() {
		return fechaTransaccion;
	}

	public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
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
