package ProyectoFinal.Banco.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

		@Column(name = "cantidad_transaccion", nullable = true, length = 100)
		private double cantidadTransaccion;

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
