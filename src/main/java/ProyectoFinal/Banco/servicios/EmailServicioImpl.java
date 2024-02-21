package ProyectoFinal.Banco.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio que implementa los métodos de la interface {@link IEmailServicio} 
 * para la gestión de envío de emails.
 */
@Service
public class EmailServicioImpl implements IEmailServicio {

    @Autowired
    private JavaMailSender javaMailSender;    

    /**
     * Envía un correo electrónico de recuperación de contraseña.
     * 
     * @param emailDestino Dirección de correo electrónico del usuario destinatario.
     * @param nombreUsuario Nombre del usuario para mostrarlo en el correo electrónico enviado.
     * @param token Token asociado a la recuperación de contraseña.
     */
    @Override
    public void enviarEmailRecuperacion(String emailDestino, String nombreUsuario, String token) {
        try {
            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            // Configuración del mensaje
            helper.setFrom("alejandroalcerreca3@gmail.com");
            helper.setTo(emailDestino);
            helper.setSubject("Restablecer Contraseña Banco Final");

            // Construcción del cuerpo del mensaje
            String urlDominio = "http://localhost:8080";
            String urlDeRecuperacion = String.format("%s/auth/recuperar?token=%s", urlDominio, token);
            String cuerpoMensaje = String.format(
                "<!DOCTYPE html><html lang='es'><body><div style='width: 600px; padding: 20px; border: 2px solid #ff9900; border-radius: 12px; font-family: Sans-serif'><h1 style='color:#192255'>Restablecer contraseña<b style='color:#ff9900'> Banco Final</b></h1><p style='margin-bottom:25px'>Estimado/a&nbsp;<b>%s</b>:</p><p style='margin-bottom:25px'>Recibiste este correo porque se solicitó un restablecimiento de contraseña para tu cuenta. Haz clic en el botón que aparece a continuación para cambiar tu contraseña.</p><a style='padding: 10px 15px; border-radius: 20px; background-color: #285845; color: white; text-decoration: none' href='%s' target='_blank'>Cambiar contraseña</a><p style='margin-top:25px'>Si no solicitaste este restablecimiento de contraseña, puedes ignorar este correo de forma segura.</p><p>Gracias por utilizar nuestros servicios.</p></div></body></html>",
                nombreUsuario, urlDeRecuperacion
            );

            // Configuración del cuerpo del mensaje
            helper.setText(cuerpoMensaje, true);

            // Envío del correo electrónico
            javaMailSender.send(mensaje);

        } catch (MailException me) {
            System.out.println("[Error EmailServicioImpl - enviarEmailRecuperacion()] Ha ocurrido un error al enviar el email! " + me.getMessage());
        } catch (MessagingException e) {
            System.out.println("[Error EmailServicioImpl - enviarEmailRecuperacion()] Ha ocurrido un error al enviar el email! " + e.getMessage());
        }   
    }
    
    /**
     * Envía un correo electrónico de confirmación de cuenta.
     * 
     * @param emailDestino Dirección de correo electrónico del usuario destinatario.
     * @param nombreUsuario Nombre del usuario para mostrarlo en el correo electrónico enviado.
     * @param token Token asociado a la confirmación de cuenta.
     */
    @Override
    public void enviarEmailConfirmacion(String emailDestino, String nombreUsuario, String token) {
        try {
            // Construcción del mensaje
            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            // Configuración del mensaje
            helper.setFrom("alejandroalcerreca3@gmail.com");
            helper.setTo(emailDestino);
            helper.setSubject("Confirmación de cuenta Banco Final");

            // Construcción del cuerpo del mensaje
            String urlDominio = "http://localhost:8080";
            String urlDeConfirmacion = String.format("%s/auth/confirmar-cuenta?token=%s", urlDominio, token);
            String cuerpoMensaje = String.format(
                "<!DOCTYPE html><html lang='es'><body><div style='width: 600px; padding: 20px; border: 2px solid #ff9900; border-radius: 12px; font-family: Sans-serif'><h1 style='color:#192255'>Crear Cuenta<b style='color:#ff9900'> Banco Final</b></h1><p style='margin-bottom:25px'>Estimado/a&nbsp;<b>%s</b>:</p><p style='margin-bottom:25px'>Recibiste este correo porque esta intentando crear una cuenta. Haz clic en el botón que aparece a continuación para terminar de crear su cuenta.</p><a style='padding: 10px 15px; border-radius: 20px; background-color: #285845; color: white; text-decoration: none' href='%s' target='_blank'>Crear Cuenta</a><p style='margin-top:25px'>Si no solicitaste crear la cuenta, puedes ignorar este correo de forma segura.</p><p>Gracias por utilizar nuestros servicios.</p></div></body></html>",
                nombreUsuario, urlDeConfirmacion
            );

            // Configuración del cuerpo del mensaje
            helper.setText(cuerpoMensaje, true);

            // Envío del correo electrónico
            javaMailSender.send(mensaje);

        } catch (MailException me) {
            System.out.println("[Error EmailServicioImpl - enviarEmailConfirmacion()] Ha ocurrido un error al enviar el email! " + me.getMessage());
        } catch (MessagingException e) {
            System.out.println("[Error EmailServicioImpl - enviarEmailConfirmacion()] Ha ocurrido un error al enviar el email! " + e.getMessage());
        }
    }
}