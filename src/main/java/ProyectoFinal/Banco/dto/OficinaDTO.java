package ProyectoFinal.Banco.dto;

import java.util.List;

import ProyectoFinal.Banco.dao.Cita;

public class OficinaDTO {

	//ATRIBUTOS
	private long idOficina;
    private List<Cita> citasOficina;
    private String direccionOficina;
    
    //CONSTRUCTORES
	public OficinaDTO(long idOficina, List<Cita> citasOficina, String direccionOficina) {
		super();
		this.idOficina = idOficina;
		this.citasOficina = citasOficina;
		this.direccionOficina = direccionOficina;
	}

	public OficinaDTO() {
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
