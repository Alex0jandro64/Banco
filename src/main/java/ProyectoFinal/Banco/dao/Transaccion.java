package ProyectoFinal.Banco.dao;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Clase DAO (Data Access Object) que representa la tabla transacciones de la BBDD,
 * mapea con esta 1:1 y ejerce como modelo virtual de la tabla en la aplicación.
 */
@Entity
@Table(name = "transacciones", schema = "bf_operacional")
public class Transaccion {

    // ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion", nullable = false)
    private long idTransaccion;

    @ManyToOne
    @JoinColumn(name = "usuarioTransaccionRemitente")
    private CuentaBancaria usuarioTransaccionRemitente;

    @ManyToOne
    @JoinColumn(name = "usuarioTransaccionDestinatario")
    private CuentaBancaria usuarioTransaccionDestinatario;

    @Column(name = "cantidad_transaccion", nullable = true)
    private double cantidadTransaccion;
    
    @Column(name = "fecha_transaccion", nullable = true)
    private Calendar fechaTransaccion;

    // GETTERS Y SETTERS
    public long getIdTransaccion() {
        return idTransaccion;
    }

    public Calendar getFechaTransaccion() {
		return fechaTransaccion;
	}

	public void setFechaTransaccion(Calendar fechaTransaccion) {
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
