package ProyectoFinal.Banco.dao;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "oficinas", schema = "bf_operacional")
public class Oficina {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oficina", nullable = false)
    private long idOficina;
	
	@OneToMany(mappedBy = "oficinaCita")
    private List<Cita> citasOficina;
	
	@Column(name = "direccion_oficina", nullable = false, length = 100)
    private String direccionOficina;
}
