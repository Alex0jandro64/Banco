package ProyectoFinal.Banco.dto;


import java.time.LocalDateTime;


public class PrestamoDTO {

	//Atributos
	private long idPrestamo;
    private String cuentaPrestamo;
    private double cantidadPrestamo;
    private String motivoPrestamo;
    private LocalDateTime fechaPrestamo;

    
    //Contructores
    public PrestamoDTO(long idPrestamo, String cuentaPrestamo, double cantidadPrestamo, String motivoPrestamo,
			LocalDateTime fechaPrestamo) {
		super();
		this.idPrestamo = idPrestamo;
		this.cuentaPrestamo = cuentaPrestamo;
		this.cantidadPrestamo = cantidadPrestamo;
		this.motivoPrestamo = motivoPrestamo;
		this.fechaPrestamo = fechaPrestamo;
	}
    
    public PrestamoDTO() {
		super();
	}


	//Getters & Setters
	public long getIdPrestamo() {
		return idPrestamo;
	}
	public void setIdPrestamo(long idPrestamo) {
		this.idPrestamo = idPrestamo;
	}
	public String getCuentaPrestamo() {
		return cuentaPrestamo;
	}
	public void setCuentaPrestamo(String cuentaPrestamo) {
		this.cuentaPrestamo = cuentaPrestamo;
	}
	public double getCantidadPrestamo() {
		return cantidadPrestamo;
	}
	public void setCantidadPrestamo(double cantidadPrestamo) {
		this.cantidadPrestamo = cantidadPrestamo;
	}
	public String getMotivoPrestamo() {
		return motivoPrestamo;
	}
	public void setMotivoPrestamo(String motivoPrestamo) {
		this.motivoPrestamo = motivoPrestamo;
	}

	public LocalDateTime fechaPrestamo() {
		return fechaPrestamo;
	}

	public void setFechaTransaccion(LocalDateTime fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}
	
    
    
}
