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
@Table(name = "prestamos", schema = "bf_operacional")
public class Prestamo {

    // ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo", nullable = false)
    private long idPrestamo;

    @ManyToOne
    @JoinColumn(name = "cuentaBancariaPrestamo")
    private CuentaBancaria cuentaBancariaPrestamo;

    @Column(name = "cantidad_prestamo", nullable = true)
    private double cantidadPrestamo;
    
    @Column(name = "motivo_prestamo", nullable = true)
    private String motivoPrestamo;
    
    @Column(name = "fecha_prestamo", nullable = true)
    private Calendar fechaPrestamo;

    
    
	public Prestamo(long idPrestamo, CuentaBancaria cuentaBancariaPrestamo, double cantidadPrestamo,
			Calendar fechaPrestamo) {
		super();
		this.idPrestamo = idPrestamo;
		this.cuentaBancariaPrestamo = cuentaBancariaPrestamo;
		this.cantidadPrestamo = cantidadPrestamo;
		this.fechaPrestamo = fechaPrestamo;
	}
	
	

	public Prestamo() {
		super();
	}



	public long getIdPrestamo() {
		return idPrestamo;
	}

	public String getMotivoPrestamo() {
		return motivoPrestamo;
	}



	public void setMotivoPrestamo(String motivoPrestamo) {
		this.motivoPrestamo = motivoPrestamo;
	}



	public void setIdPrestamo(long idPrestamo) {
		this.idPrestamo = idPrestamo;
	}

	public CuentaBancaria getCuentaBancariaPrestamo() {
		return cuentaBancariaPrestamo;
	}

	public void setCuentaBancariaPrestamo(CuentaBancaria cuentaBancariaPrestamo) {
		this.cuentaBancariaPrestamo = cuentaBancariaPrestamo;
	}

	public double getCantidadPrestamo() {
		return cantidadPrestamo;
	}

	public void setCantidadPrestamo(double cantidadPrestamo) {
		this.cantidadPrestamo = cantidadPrestamo;
	}

	public Calendar getFechaPrestamo() {
		return fechaPrestamo;
	}

	public void setFechaPrestamo(Calendar fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}

    
}
