package ProyectoFinal.Banco.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;


/**
 * Clase controlador para gestionar la autenticación y registro de usuarios.
 */
@Controller
public class LoginControlador {

	@Autowired
    private IUsuarioServicio usuarioServicio;
	
    @GetMapping("/auth/login")
    public String login(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        
        return "login";
    }
    @PostMapping("/auth/login-post")
    public String registrarPost(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
        try {
        	
            //Usuario nuevoUsuario = usuarioServicio.registrar(usuarioDTO);
            
            return "home";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al registrar el usuario: " + e.getMessage());
            return "registro";
        }
    }
    

    @GetMapping("/auth/confirmar-cuenta")
    public String confirmarCuenta(Model model, @RequestParam("token") String token) {
        try {
            boolean confirmacionExitosa = usuarioServicio.confirmarCuenta(token);

            if (confirmacionExitosa) {
                model.addAttribute("cuentaVerificada", "Su dirección de correo ha sido confirmada correctamente");
            } else {
                model.addAttribute("cuentaNoVerificada", "Error al confirmar su email");
            }

            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar la solicitud. Por favor, inténtelo de nuevo.");
            return "login";
        }
    }
}
