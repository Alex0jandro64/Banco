package ProyectoFinal.Banco.dao;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase DAO (Data Access Object) que representa la tabla cuentasBancarias de la BBDD,
 * mapea con esta 1:1 y ejerce como modelo virtual de la tabla en la aplicación.
 */
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

    @Column(name = "saldo_cuenta", nullable = true)
    private double saldoCuenta;

    @Column(name = "codigo_iban", nullable = true, length = 100)
    private String codigoIban;

    @OneToMany(mappedBy = "usuarioTransaccionRemitente")
    private List<Transaccion> trasaccionesRemitentes;

    @OneToMany(mappedBy = "usuarioTransaccionDestinatario")
    private List<Transaccion> trasaccionesDestinatarios;

    // CONSTRUCTORES
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

    // GETTERS Y SETTERS
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

	public List<Transaccion> getTrasaccionesRemitentes() {
		return trasaccionesRemitentes;
	}

	public void setTrasaccionesRemitentes(List<Transaccion> trasaccionesRemitentes) {
		this.trasaccionesRemitentes = trasaccionesRemitentes;
	}

	public List<Transaccion> getTrasaccionesDestinatarios() {
		return trasaccionesDestinatarios;
	}

	public void setTrasaccionesDestinatarios(List<Transaccion> trasaccionesDestinatarios) {
		this.trasaccionesDestinatarios = trasaccionesDestinatarios;
	}

    
}
