package ProyectoFinal.Banco.controladores;

import java.time.format.DateTimeFormatter;
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
    public String listarCitas(Model model, Authentication authentication, @ModelAttribute("mensajeCitaEditada") String mensajeCitaEditada,@ModelAttribute("ErrorEditarCita") String errorEditarCita) {
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
        	model.addAttribute("mensajeCitaEditada",mensajeCitaEditada);
        	model.addAttribute("ErrorEditarCita",errorEditarCita);
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
	
	@GetMapping("/privada/editar-cita/{id}")
	public String mostrarFormularioEdicion(@PathVariable Long id, Model model, HttpServletRequest request) {
	    try {
	        if (request.isUserInRole("ROLE_USER")) {
	            model.addAttribute("noAdmin", "No tiene permiso para realizar esta accion");
	            return "dashboard";
	        } else {
	            List<CitaDTO> citasDto = citaServicio.buscarTodos();
	            CitaDTO citaDto = null;
	            for (CitaDTO cita : citasDto) {
	                if (cita.getIdCita() == id.longValue()) { // Convertimos el Long a long
	                    citaDto = cita;
	                    break;
	                }
	            }

	            if (citaDto == null) {
	                return "administracionUsuarios";
	            }
	            
	            List<Oficina> oficinas = oficinaServicio.obtenerOficinas();
	            List<OficinaDTO> oficinasDto = Util.listaOficinasToDto(oficinas);
	        
	            model.addAttribute("listaOficinas", oficinasDto);
	            CitaDTOLong citaDtoLong = new CitaDTOLong();
	            citaDtoLong.setIdCita(citaDto.getIdCita());
	            citaDtoLong.setFechaCita(citaDto.getFechaCita());
	            citaDtoLong.setMotivoCita(citaDto.getMotivoCita());
	            citaDtoLong.setOficinaCita(citaDto.getOficinaCita().getIdOficina());
	            citaDtoLong.setUsuarioCita(citaDto.getUsuarioCita());
	            model.addAttribute("fechaSeleccionada", citaDto.getFechaCita().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

	            model.addAttribute("citaDtoLong", citaDtoLong);
	            
	            // Establecer la oficina seleccionada por defecto
	            model.addAttribute("oficinaSeleccionada", citaDto.getOficinaCita().getIdOficina());
	            
	            return "editarCita";
	        }
	    } catch (Exception e) {
	        model.addAttribute("Error", "Ocurrió un error al obtener el usuario para editar");
	        return "dashboard";
	    }
	}

	/**
	 * Gestiona la solicitud HTTP POST para la url /privada/procesar-editar y
	 * procesa el formulario de edición del usuario.
	 *
	 * @param usuarioDTO UsuarioDTO con los datos editados.
	 * @param model      Modelo que se utiliza para enviar mensajes y el listado
	 *                   actualizado a la vista.
	 * @return La vista de administración de usuarios con el resultado de la
	 *         edición.
	 */
	@PostMapping("/privada/editarCita")
	public String procesarFormularioEdicion(@RequestParam("idOficina") Long idOficina,
            @ModelAttribute CitaDTOLong citaDTOLong,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
		try {
			String mail = authentication.getName();
            Usuario usuario = usuarioServicio.buscarPorEmail(mail);
            citaDTOLong.setUsuarioCita(usuario);
            citaDTOLong.setOficinaCita(idOficina);
			citaServicio.editarCita(citaDTOLong);
			redirectAttributes.addFlashAttribute("mensajeCitaEditada", "La cita se ha editado correctamente");
			return "redirect:/privada/verCitas";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("ErrorEditarCita", "Ocurrió un error al editar la cita");
			return "redirect:/privada/verCitas";
		}
	}
	
}