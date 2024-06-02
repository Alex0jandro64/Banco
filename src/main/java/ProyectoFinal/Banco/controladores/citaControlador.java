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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ProyectoFinal.Banco.dao.Oficina;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CitaDTO;
import ProyectoFinal.Banco.dto.CitaDTOLong;
import ProyectoFinal.Banco.dto.OficinaDTO;
import ProyectoFinal.Banco.servicios.ICitasServicio;
import ProyectoFinal.Banco.servicios.IOficinaServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador para manejar las citas bancarias.
 */
@Controller
public class citaControlador {
	
	@Autowired
    private IUsuarioServicio usuarioServicio;
	
	@Autowired
    private IOficinaServicio oficinaServicio;
	
	@Autowired
    private ICitasServicio citaServicio;

	/**
	 * Método para mostrar el formulario de solicitud de nueva cita.
	 */
	@GetMapping("/privada/cita")
    public String nuevaCita(Model model, Authentication authentication) {
    	try {
    		String mail = authentication.getName();
        	Usuario usuario = usuarioServicio.buscarPorEmail(mail);
        	List<Oficina> oficinas = oficinaServicio.obtenerOficinas();
        	List<OficinaDTO> oficinasDto = Util.listaOficinasToDto(oficinas);
        	CitaDTOLong citaDTOLong = new CitaDTOLong();
        
        	model.addAttribute("listaOficinas", oficinasDto);
        	model.addAttribute("usuario",usuario);
        	model.addAttribute("CitaDTOLong",citaDTOLong);
    	} catch (Exception e) {
        	model.addAttribute("errorMessage", "Error al cargar la página: " + e.getMessage());
    	}
    	return "nuevaCita";
    }
	
	/**
	 * Método para mostrar la lista de citas del usuario.
	 */
	@GetMapping("/privada/verCitas")
    public String listarCitas(Model model, Authentication authentication) {
		try {
    		String mail = authentication.getName();
        	Usuario usuario = usuarioServicio.buscarPorEmail(mail);
        
        	boolean isAdmin = authentication.getAuthorities().stream()
                				.anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_TRABAJADOR"));
        
        	if (isAdmin) {
        		List<CitaDTO> citasAdmin = citaServicio.buscarTodos();
            	model.addAttribute("citas", citasAdmin);
        	} else {
        		List<CitaDTO> citas = Util.listaCitasToDto(usuario.getCitasUsuario());
            	model.addAttribute("citas", citas);
        	}
        
        	model.addAttribute("usuario",usuario);
		} catch (Exception e) {
        	model.addAttribute("errorMessage", "Error al cargar la página: " + e.getMessage());
    	}
    	return "listadoCitas";
    }

	/**
	 * Método para procesar la solicitud de nueva cita.
	 */
	@PostMapping("/privada/nuevaCita")
    public String nuevaCitaPost(@RequestParam("idOficina") Long idOficina,
                                @ModelAttribute CitaDTOLong citaDTOLong,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
    	try {
        	String mail = authentication.getName();
            Usuario usuario = usuarioServicio.buscarPorEmail(mail);
            citaDTOLong.setUsuarioCita(usuario);
            citaDTOLong.setOficinaCita(idOficina);
            citaServicio.registrarCita(citaDTOLong);
            redirectAttributes.addFlashAttribute("mensajeCitaBien", "Cita registrada correctamente: ");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeCitaError", "Error al registrar la cita: " + e.getMessage());
        }
        return "redirect:/privada/home";
    }
	
	/**
	 * Método para eliminar una cita.
	 */
	@GetMapping("/privada/eliminarCita/{id}")
    public String eliminarCita(@PathVariable Long id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    	try {
            citaServicio.eliminar(id);
            return "redirect:/privada/verCitas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeCitaError", "Error al eliminar la cita: " + e.getMessage());
            return "redirect:/privada/verCitas";
        }
    }
}