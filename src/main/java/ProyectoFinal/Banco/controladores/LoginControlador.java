package ProyectoFinal.Banco.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.servicios.ICuentaServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Clase controlador para gestionar la autenticación y registro de usuarios.
 */
@Controller
public class LoginControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    @Autowired
    private ICuentaServicio cuentaServicio;

    @GetMapping("/auth/login")
    public String login(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "login";
    }

    @GetMapping("/auth/registrar")
    public String registrarGet(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "registro";
    }

    @PostMapping("/auth/registrar")
    public String registrarPost(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
        try {
            Usuario nuevoUsuario = usuarioServicio.registrar(usuarioDTO);
            if (nuevoUsuario != null && nuevoUsuario.getDniUsuario() != null) {
                model.addAttribute("mensajeTransaccionExitosa", "Registro del nuevo usuario OK");
                CuentaBancaria cuentaNueva = new CuentaBancaria();
                cuentaNueva.setUsuarioCuenta(nuevoUsuario);
                cuentaServicio.registrarCuenta(cuentaNueva);
                return "login";
            } else {
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

    @GetMapping("/privada/listado")
    public String listadoUsuarios(Model model, HttpServletRequest request, Authentication authentication) {
        try {
            List<UsuarioDTO> usuarios = usuarioServicio.buscarTodos();
            model.addAttribute("usuarios", usuarios);
            if (request.isUserInRole("ROLE_ADMIN")) {
                return "listado";
            }
            model.addAttribute("noAdmin", "No eres admin");
            model.addAttribute("nombreUsuario", authentication.getName());
            return "home";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al obtener el listado de usuarios: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/privada/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            List<UsuarioDTO> usuarios = usuarioServicio.buscarTodos();
            if (request.isUserInRole("ROLE_ADMIN") && usuario.getRol().equals("ROLE_ADMIN")) {
                model.addAttribute("noSePuedeEliminar", "No se puede eliminar a un admin");
                model.addAttribute("usuarios", usuarios);
                return "listado";
            }
            usuarioServicio.eliminar(id);
            return "redirect:/privada/listado";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al eliminar el usuario: " + e.getMessage());
            return "error";
        }
    }
}
