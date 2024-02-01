package ProyectoFinal.Banco.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "citas", schema = "bf_operacional")
public class Cita {

	
	//ATRIBUTOS
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita", nullable = false)
    private long idCita;
	
	@ManyToOne
    private Usuario usuarioCita;
	
	@ManyToOne
    private Oficina oficinaCita;

	//CONSTRUCTORES
	public Cita(long idCita, Usuario usuarioCita, Oficina oficinaCita) {
		super();
		this.idCita = idCita;
		this.usuarioCita = usuarioCita;
		this.oficinaCita = oficinaCita;
	}

	public Cita() {
		super();
	}

	//GETTERS Y SETTERS
	public long getIdCita() {
		return idCita;
	}

	public void setIdCita(long idCita) {
		this.idCita = idCita;
	}

	public Usuario getUsuarioCita() {
		return usuarioCita;
	}

	public void setUsuarioCita(Usuario usuarioCita) {
		this.usuarioCita = usuarioCita;
	}

	public Oficina getOficinaCita() {
		return oficinaCita;
	}

	public void setOficinaCita(Oficina oficinaCita) {
		this.oficinaCita = oficinaCita;
	}
	
	
	
}
