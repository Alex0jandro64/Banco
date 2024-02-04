package ProyectoFinal.Banco.dto;

import java.time.LocalDateTime;
import ProyectoFinal.Banco.dao.Oficina;
import ProyectoFinal.Banco.dao.Usuario;

public class CitaDTO {
	
		//ATRIBUTOS
	    private long idCita;
	    private Usuario usuarioCita;
	    private Oficina oficinaCita;
	    private LocalDateTime fechaCita;
	    private String motivoCita;
	    
	  //CONSTRUCTOR
		public CitaDTO(long idCita, Usuario usuarioCita, Oficina oficinaCita, LocalDateTime fechaCita) {
			super();
			this.idCita = idCita;
			this.usuarioCita = usuarioCita;
			this.oficinaCita = oficinaCita;
			this.fechaCita = fechaCita;
		}

		public CitaDTO() {
			super();
		}

		//GETTERS Y SETTERS
		public long getIdCita() {
			return idCita;
		}
		

		public String getMotivoCita() {
			return motivoCita;
		}

		public void setMotivoCita(String motivoCita) {
			this.motivoCita = motivoCita;
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

		public LocalDateTime getFechaCita() {
			return fechaCita;
		}

		public void setFechaCita(LocalDateTime fechaCita) {
			this.fechaCita = fechaCita;
		}
	    
	    
	    
	    }
