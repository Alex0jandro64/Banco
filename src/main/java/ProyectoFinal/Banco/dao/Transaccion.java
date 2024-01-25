package ProyectoFinal.Banco.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
		private CuentaBancaria usuarioTransaccionRemitente;
		
		@ManyToOne
		private CuentaBancaria usuarioTransaccionDestinatario;

		@Column(name = "cantidad_transaccion", nullable = true, length = 100)
		private double cantidadTransaccion;
		
}
