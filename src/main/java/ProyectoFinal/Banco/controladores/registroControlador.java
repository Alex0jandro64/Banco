package ProyectoFinal.Banco.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.servicios.ICuentaServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;

@Controller
public class registroControlador {
	
	@Autowired
    private ICuentaServicio cuentaServicio;
	
	@Autowired
    private IUsuarioServicio usuarioServicio;
	
	@GetMapping("/auth/registrar")
    public String registrarGet(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "registro";
    }
	
	@PostMapping("/auth/registrar")
    public String registrarPost(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
        try {
        	
            Usuario nuevoUsuario = usuarioServicio.registrar(usuarioDTO);
            
            if (nuevoUsuario != null && nuevoUsuario.getDniUsuario() != null ) {
            	model.addAttribute("mensajeExitoMail", "Registro del nuevo usuario OK");
                CuentaBancaria cuentaNueva = new CuentaBancaria();
                cuentaNueva.setUsuarioCuenta(nuevoUsuario);
                cuentaServicio.registrarCuenta(cuentaNueva);
                return "login";
            }
            else {
                if (usuarioDTO.getDniUsuario() == null) {
                    model.addAttribute("mensajeErrorDni", "Ya existe un usuario con ese DNI");
                    return "registro";
                } else {
                    model.addAttribute("mensajeErrorMail", "Ya existe un usuario con ese email");
                    return "registro";
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al registrar el usuario: " + e.getMessage());
            return "registro";
        }
    }
}
