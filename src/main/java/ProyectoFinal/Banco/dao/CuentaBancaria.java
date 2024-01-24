package ProyectoFinal.Banco.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuentasBancarias", schema = "bf_operacional")
public class CuentaBancaria {

	// ATRIBUTOS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cuenta", nullable = false)
	private long idCuenta;

	@ManyToOne
	private Usuario usuarioCuenta;

	@Column(name = "saldo_cuenta", nullable = true, length = 100)
	private double saldoCuenta;
	
	@Column(name = "codigo_iban", nullable = true, length = 100)
	private String codigoIban;

	public CuentaBancaria(long idCuenta, Usuario usuarioCuenta, double saldoCuenta, String codigoIban) {
		super();
		this.idCuenta = idCuenta;
		this.usuarioCuenta = usuarioCuenta;
		this.saldoCuenta = saldoCuenta;
		this.codigoIban = codigoIban;
	}

	public CuentaBancaria() {
		super();
	}

	public Usuario getUsuarioCuenta() {
		return usuarioCuenta;
	}

	public void setUsuarioCuenta(Usuario usuarioCuenta) {
		this.usuarioCuenta = usuarioCuenta;
	}

	public double getSaldoCuenta() {
		return saldoCuenta;
	}

	public void setSaldoCuenta(double saldoCuenta) {
		this.saldoCuenta = saldoCuenta;
	}

	public long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getCodigoIban() {
		return codigoIban;
	}

	public void setCodigoIban(String codigoIban) {
		this.codigoIban = codigoIban;
	}
	
	

	
}
