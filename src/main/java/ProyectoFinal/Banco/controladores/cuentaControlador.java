package ProyectoFinal.Banco.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.servicios.ICuentaServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;

/**
 * Controlador para manejar las cuentas bancarias.
 */
@Controller
public class cuentaControlador {

	@Autowired
    private ICuentaServicio cuentaServicio;
	
    @Autowired
    private IUsuarioServicio usuarioServicio;
    
	/**
	 * Método para crear una nueva cuenta bancaria.
	 */
	@PostMapping("/privada/cuenta")
    public String crearCuenta(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
        	String mail = authentication.getName();
            Usuario usuario = usuarioServicio.buscarPorEmail(mail);
            Long idUsuario = usuario.getIdUsuario();
            model.addAttribute("idUsuario", idUsuario);
            
            if(usuario.getCuentasBancarias().size()>=3) {
            	redirectAttributes.addFlashAttribute("mensajeCuentaLimite", "Limite de cuentas por usuario");
            } else {
            	CuentaBancaria cuentaNueva = new CuentaBancaria();
                cuentaNueva.setUsuarioCuenta(usuario);
                cuentaServicio.registrarCuenta(cuentaNueva);
                redirectAttributes.addFlashAttribute("mensajeCuentaCreada", "La cuenta se ha creado correctamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar la cuenta: " + e.getMessage());
        }
        return "redirect:/privada/home";
    }
}