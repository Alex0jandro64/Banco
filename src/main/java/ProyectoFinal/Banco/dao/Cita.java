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

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita", nullable = false)
    private long idCita;
	
	@ManyToOne
    private Usuario usuarioCita;
	
	@ManyToOne
    private Oficina oficinaCita;
	
	
	
}
