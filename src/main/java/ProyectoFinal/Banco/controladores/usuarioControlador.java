package ProyectoFinal.Banco.controladores;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.repositorios.UsuarioRepositorio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class usuarioControlador {

	@Autowired
    private IUsuarioServicio usuarioServicio;
	
	@Autowired
    private UsuarioRepositorio usuarioRepository;
	
	@GetMapping("/privada/listado")
    public String listadoUsuarios(Model model, HttpServletRequest request, Authentication authentication,RedirectAttributes redirectAttributes) {
        try {
            
            if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_TRABAJADOR")) {
            	List<UsuarioDTO> usuarios = usuarioServicio.buscarTodos();
                model.addAttribute("usuarios", usuarios);
                return "listado";
            }
            redirectAttributes.addFlashAttribute("noAdmin", "No eres admin");
            return "redirect:/privada/home";
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
            if (request.isUserInRole("ROLE_ADMIN") && usuario.getRol().equals("ROLE_ADMIN") || usuario.getRol().equals("ROLE_TRABAJADOR")) {
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
    
    @GetMapping("/privada/darRol/{id}")
    public String darRolUsuario(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            List<UsuarioDTO> usuarios = usuarioServicio.buscarTodos();
            if (request.isUserInRole("ROLE_ADMIN") && usuario.getRol().equals("ROLE_ADMIN")) {
                model.addAttribute("noSePuedeAdmin", "No se puede cambiar rol a un admin");
                model.addAttribute("usuarios", usuarios);
                return "listado";
            }
            usuarioServicio.darRol(id);
            return "redirect:/privada/listado";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al dar rol al usuario: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/privada/datosUsuario")
    public String loginCorrecto(Model model, Authentication authentication ) {
        try {
            String mail = authentication.getName();
            Usuario usuario = usuarioServicio.buscarPorEmail(mail);
            Long idUsuario = usuario.getIdUsuario();
            model.addAttribute("idUsuario", idUsuario);
            
            List<CuentaBancaria> cuentasBancarias1 = usuario.getCuentasBancarias();
            List<CuentaBancariaDTO> cuentasBancariasDto = Util.cuentaBancariaToDto(cuentasBancarias1);
            
            UsuarioDTO usuariodto = Util.usuarioToDto(usuario);
            
            
            
            model.addAttribute("cuentasBancarias", cuentasBancariasDto);
            model.addAttribute("cuentasBancariasDto"  , cuentasBancariasDto);
            model.addAttribute("usuario", usuariodto);
        } catch (Exception e) {
            System.out.println("Error [bancoControlador-loginCorrecto]");
        }
        return "datosUsuario";
    }
    
    @PostMapping("/privada/guardarFotoPerfil")
    public String guardarFotoPerfil(@RequestParam("fotoPerfil") MultipartFile file, Principal principal) throws IOException {
        Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
        
        if (file != null && !file.isEmpty()) {
            usuario.setFotoPerfil(file.getBytes());
            usuarioRepository.save(usuario);
        }
        
        return "redirect:/privada/datosUsuario";
    }
    
    @GetMapping("/privada/perfil/foto/{id}")
    public ResponseEntity<byte[]> obtenerFotoPerfil(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        
        if (usuario != null && usuario.getFotoPerfil() != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(usuario.getFotoPerfil());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
