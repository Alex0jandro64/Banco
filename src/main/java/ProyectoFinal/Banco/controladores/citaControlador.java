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

@Controller
public class citaControlador {
	
	@Autowired
    private IUsuarioServicio usuarioServicio;
	
	@Autowired
    private IOficinaServicio oficinaServicio;
	
	@Autowired
    private ICitasServicio citaServicio;

	
	@GetMapping("/privada/cita")
    public String nuevaCita(Model model, Authentication authentication) {
    	String mail = authentication.getName();
        Usuario usuario = usuarioServicio.buscarPorEmail(mail);
        List<Oficina> oficinas = oficinaServicio.obtenerOficinas();
        List<OficinaDTO> oficinasDto = Util.listaOficinasToDto(oficinas);
        CitaDTOLong citaDTOLong = new CitaDTOLong();
        
        model.addAttribute("listaOficinas", oficinasDto);
        model.addAttribute("usuario",usuario);
        model.addAttribute("CitaDTOLong",citaDTOLong);
        return "nuevaCita";
    }
	
	@GetMapping("/privada/verCitas")
    public String listarCitas(Model model, Authentication authentication) {
		
    	String mail = authentication.getName();
        Usuario usuario = usuarioServicio.buscarPorEmail(mail);
        
        
        
     // Verificar si el usuario tiene el rol de administrador
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_TRABAJADOR"));
        
     // Si el usuario es administrador, pasar la lista completa de citas
        if (isAdmin) {
        	List<CitaDTO> citasAdmin = citaServicio.buscarTodos();
            model.addAttribute("citas", citasAdmin);
        } else {
            // Si no es administrador, pasar solo las citas del usuario actual
        	List<CitaDTO> citas = Util.listaCitasToDto(usuario.getCitasUsuario());
            model.addAttribute("citas", citas);
        }
        
        model.addAttribute("usuario",usuario);
        return "listadoCitas";
    }

	
	@PostMapping("/privada/nuevaCita")
    public String nuevaCitaPost(@RequestParam("idOficina") Long idOficina,@ModelAttribute CitaDTOLong citaDTOLong,Authentication authentication ,RedirectAttributes redirectAttributes) {
        try {
        	String mail = authentication.getName();
            Usuario usuario = usuarioServicio.buscarPorEmail(mail);
            citaDTOLong.setUsuarioCita(usuario);
            citaDTOLong.setOficinaCita(idOficina);
            citaServicio.registrarCita(citaDTOLong);

        	
            //int error = citaServicio.registrarCita(citaDTO);
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar la transacción: " + e.getMessage());
        }
        return "redirect:/privada/home";
    }
	
	@GetMapping("/privada/eliminarCita/{id}")
    public String eliminarCita(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            citaServicio.eliminar(id);
            return "redirect:/privada/verCitas";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al eliminar el usuario: " + e.getMessage());
            return "error";
        }
    }
}
