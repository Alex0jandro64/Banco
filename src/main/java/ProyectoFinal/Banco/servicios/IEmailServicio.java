package ProyectoFinal.Banco.servicios;

/**
 * Interface donde se declaran los métodos necesarios para la gestión de correos electrónicos.
 */
public interface IEmailServicio {
	
    /**
     * Envía un correo electrónico de recuperación de contraseña.
     * 
     * @param emailDestino Dirección de correo electrónico del usuario destinatario.
     * @param nombreUsuario Nombre del usuario para mostrarlo en el correo electrónico enviado.
     * @param token Token asociado a la recuperación de contraseña.
     */
    public void enviarEmailRecuperacion(String emailDestino, String nombreUsuario, String token);
    
    /**
     * Envía un correo electrónico de confirmación.
     * 
     * @param emailDestino Dirección de correo electrónico del usuario destinatario.
     * @param nombreUsuario Nombre del usuario para mostrarlo en el correo electrónico enviado.
     * @param token Token asociado a la confirmación.
     */
    public void enviarEmailConfirmacion(String emailDestino, String nombreUsuario, String token);
}