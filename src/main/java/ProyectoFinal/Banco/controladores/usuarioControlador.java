package ProyectoFinal.Banco.controladores;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    
    /**
     * Método para mostrar el listado de usuarios.
     * @param model El modelo utilizado para agregar los datos necesarios a la vista.
     * @param request La solicitud HTTP.
     * @param authentication La autenticación del usuario.
     * @param redirectAttributes Los atributos utilizados para enviar mensajes flash a la redirección.
     * @return La vista "listado".
     */
    @GetMapping("/privada/listado")
    public String listadoUsuarios(Model model, HttpServletRequest request, Authentication authentication, RedirectAttributes redirectAttributes) {
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

    /**
     * Método para eliminar un usuario.
     * @param id El ID del usuario a eliminar.
     * @param model El modelo utilizado para agregar los datos necesarios a la vista.
     * @param request La solicitud HTTP.
     * @return La redirección al listado de usuarios.
     */
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
    
    /**
     * Método para dar un rol a un usuario.
     * @param id El ID del usuario al que se le dará el rol.
     * @param model El modelo utilizado para agregar los datos necesarios a la vista.
     * @param request La solicitud HTTP.
     * @return La redirección al listado de usuarios.
     */
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
    
    /**
     * Método para mostrar los datos del usuario actual.
     * @param model El modelo utilizado para agregar los datos necesarios a la vista.
     * @param authentication La autenticación del usuario.
     * @return La vista "datosUsuario".
     */
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
            model.addAttribute("errorMessage", "Error al obtener los datos del usuario: " + e.getMessage());
            return "error";
        }
        return "datosUsuario";
    }
    
    /**
     * Método para guardar la foto de perfil del usuario.
     * @param file El archivo de la foto de perfil.
     * @param principal El principal del usuario.
     * @return La redirección a la página de datos del usuario.
     * @throws IOException Si hay un error de E/S.
     */
    @PostMapping("/privada/guardarFotoPerfil")
    public String guardarFotoPerfil(@RequestParam("fotoPerfil") MultipartFile file, Principal principal) throws IOException {
        try {
            Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
            
            if (file != null && !file.isEmpty()) {
                usuario.setFotoPerfil(file.getBytes());
                usuarioRepository.save(usuario);
            }
        } catch (Exception e) {
            // Manejo de excepciones no controladas
            throw new IOException("Error al guardar la foto de perfil: " + e.getMessage());
        }
        
        return "redirect:/privada/datosUsuario";
    }
    
    /**
     * Método para obtener la foto de perfil de un usuario.
     * @param id El ID del usuario.
     * @return La respuesta con la foto de perfil.
     */
    @GetMapping("/privada/perfil/foto/{id}")
    public ResponseEntity<byte[]> obtenerFotoPerfil(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            
            if (usuario != null && usuario.getFotoPerfil() != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(usuario.getFotoPerfil());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Manejo de excepciones no controladas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}