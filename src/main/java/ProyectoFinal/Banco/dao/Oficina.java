package ProyectoFinal.Banco.dao;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
/**
 * Clase DAO (Data Access Object) que representa la tabla de oficinas en la base de datos,
 * mapea con esta 1:1 y ejerce como modelo virtual de la tabla en la aplicación.
 */
@Entity
@Table(name = "oficinas", schema = "bf_operacional")
public class Oficina {

	//ATRIBUTOS
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oficina", nullable = false)
    private long idOficina;
	
	@OneToMany(mappedBy = "oficinaCita")
    private List<Cita> citasOficina;
	
	@Column(name = "direccion_oficina", nullable = false, length = 100)
    private String direccionOficina;

	//CONSTRUCTORES
	public Oficina(long idOficina, List<Cita> citasOficina, String direccionOficina) {
		super();
		this.idOficina = idOficina;
		this.citasOficina = citasOficina;
		this.direccionOficina = direccionOficina;
	}

	public Oficina() {
		super();
	}

	//GETTERS Y SETTERS
	public long getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(long idOficina) {
		this.idOficina = idOficina;
	}

	public List<Cita> getCitasOficina() {
		return citasOficina;
	}

	public void setCitasOficina(List<Cita> citasOficina) {
		this.citasOficina = citasOficina;
	}

	public String getDireccionOficina() {
		return direccionOficina;
	}

	public void setDireccionOficina(String direccionOficina) {
		this.direccionOficina = direccionOficina;
	}
	
	
	
	
	
}
