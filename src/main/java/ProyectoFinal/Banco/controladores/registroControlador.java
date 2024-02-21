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

/**
 * Controlador para gestionar el registro de usuarios.
 */
@Controller
public class registroControlador {
    
    @Autowired
    private ICuentaServicio cuentaServicio;
    
    @Autowired
    private IUsuarioServicio usuarioServicio;
    
    /**
     * Método para mostrar el formulario de registro de usuarios (GET).
     * @param model El modelo que se utiliza para enviar datos a la vista.
     * @return La vista de registro (registro.html).
     */
    @GetMapping("/auth/registrar")
    public String registrarGet(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "registro";
    }
    
    /**
     * Método para procesar el formulario de registro de usuarios (POST).
     * @param usuarioDTO El objeto UsuarioDTO que contiene los datos del usuario a registrar.
     * @param model El modelo que se utiliza para enviar mensajes y datos a la vista.
     * @return La vista de inicio de sesión (login.html) si el registro es exitoso;
     *         de lo contrario, la vista de registro (registro.html) con mensajes de error.
     */
    @PostMapping("/auth/registrar")
    public String registrarPost(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
        try {
            // Registrar el nuevo usuario
            Usuario nuevoUsuario = usuarioServicio.registrar(usuarioDTO);
            
            // Verificar si el registro fue exitoso
            if (nuevoUsuario.getDniUsuario() != null && nuevoUsuario != null ) {
                // Si el usuario se registró correctamente, crear una nueva cuenta bancaria para él
                model.addAttribute("mensajeExitoMail", "Registro del nuevo usuario OK");
                CuentaBancaria cuentaNueva = new CuentaBancaria();
                cuentaNueva.setUsuarioCuenta(nuevoUsuario);
                cuentaServicio.registrarCuenta(cuentaNueva);
                return "login"; // Redireccionar al formulario de inicio de sesión
            } else {
                // Si hubo errores durante el registro, mostrar mensajes de error adecuados
                if (nuevoUsuario.getDniUsuario() == null) {
                    model.addAttribute("mensajeErrorDni", "Ya existe un usuario con ese DNI");
                } else {
                    model.addAttribute("mensajeErrorMail", "Ya existe un usuario con ese email");
                }
                return "registro"; // Volver al formulario de registro
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al registrar el usuario: " + e.getMessage());
            return "registro"; // Volver al formulario de registro en caso de error
        }
    }
}