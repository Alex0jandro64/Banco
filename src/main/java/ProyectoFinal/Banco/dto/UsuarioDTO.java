package ProyectoFinal.Banco.dto;

import java.util.Calendar;
import java.util.Objects;

/**
 * Clase DTO (Data Transfer Object) para pasar información entre capas 
 * para la gestión de usuarios
 */
public class UsuarioDTO {

	//ATRIBUTOS
	private long id;
	private String nombreUsuario;
	private String apellidosUsuario;
	private String dniUsuario;
	private String tlfUsuario;
	private String emailUsuario;
	private String claveUsuario;
	private String rolUsuario;
	private String token;
	private boolean mailConfirmado =false;
	private Calendar fchAltaUsuario;
	private String password;
	private String password2;
	private String mensajeError;
	private Calendar expiracionToken;
	private String rutaImagenUsuario;
	//CONSTRUCTORES
	public UsuarioDTO() {
	}

	public UsuarioDTO(String dniUsuario, String nombreUsuario, String apellidosUsuario, String tlfUsuario,
			String emailUsuario, String claveUsuario) {
		this.dniUsuario = dniUsuario;
		this.nombreUsuario = nombreUsuario;
		this.apellidosUsuario = apellidosUsuario;
		this.tlfUsuario = tlfUsuario;
		this.emailUsuario = emailUsuario;
		this.claveUsuario = claveUsuario;
	}

	//GETTERS Y SETTERS
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public void setMailConfirmado(boolean mailConfirmado) {
		this.mailConfirmado = mailConfirmado;
	}

	public String getDniUsuario() {
		return dniUsuario;
	}

	public Boolean getMailConfirmado() {
		return mailConfirmado;
	}

	public void setMailConfirmado(Boolean mailConfirmado) {
		this.mailConfirmado = mailConfirmado;
	}

	public String getRolUsuario() {
		return rolUsuario;
	}

	public void setRolUsuario(String rolUsuario) {
		this.rolUsuario = rolUsuario;
	}

	public void setDniUsuario(String dniUsuario) {
		this.dniUsuario = dniUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getApellidosUsuario() {
		return apellidosUsuario;
	}

	public void setApellidosUsuario(String apellidosUsuario) {
		this.apellidosUsuario = apellidosUsuario;
	}

	public String getTlfUsuario() {
		return tlfUsuario;
	}

	public void setTlfUsuario(String tlfUsuario) {
		this.tlfUsuario = tlfUsuario;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public String getClaveUsuario() {
		return claveUsuario;
	}

	public void setClaveUsuario(String claveUsuario) {
		this.claveUsuario = claveUsuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	public Calendar getExpiracionToken() {
		return expiracionToken;
	}

	public void setExpiracionToken(Calendar expiracionToken) {
		this.expiracionToken = expiracionToken;
	}
	
	public String getRutaImagenUsuario() {
		return rutaImagenUsuario;
	}

	public void setRutaImagenUsuario(String rutaImagenUsuario) {
		this.rutaImagenUsuario = rutaImagenUsuario;
	}

	public Calendar getFchAltaUsuario() {
		return fchAltaUsuario;
	}

	public void setFchAltaUsuario(Calendar fchAltaUsuario) {
		this.fchAltaUsuario = fchAltaUsuario;
	}
	
	public Boolean isMailConfirmado() {
		return this.mailConfirmado;
	}

	//METODOS
	@Override
	public int hashCode() {
		return Objects.hash(apellidosUsuario, claveUsuario, dniUsuario, emailUsuario, expiracionToken, nombreUsuario,
				password, password2, tlfUsuario, token);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(apellidosUsuario, other.apellidosUsuario)
				&& Objects.equals(claveUsuario, other.claveUsuario) && Objects.equals(dniUsuario, other.dniUsuario)
				&& Objects.equals(emailUsuario, other.emailUsuario)
				&& Objects.equals(expiracionToken, other.expiracionToken)
				&& Objects.equals(nombreUsuario, other.nombreUsuario) && Objects.equals(password, other.password)
				&& Objects.equals(password2, other.password2) && Objects.equals(tlfUsuario, other.tlfUsuario)
				&& Objects.equals(token, other.token);
	}

	@Override
	public String toString() {
		return "UsuarioDTO [nombreUsuario=" + nombreUsuario + ", apellidosUsuario=" + apellidosUsuario + ", dniUsuario="
				+ dniUsuario + ", tlfUsuario=" + tlfUsuario + ", emailUsuario=" + emailUsuario + ", claveUsuario="
				+ claveUsuario + ", token=" + token + ", password=" + password + ", password2=" + password2
				+ ", expiracionToken=" + expiracionToken + "]";
	}
    
	
}
