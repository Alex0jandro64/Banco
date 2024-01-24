package ProyectoFinal.Banco.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.servicios.ICuentaServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;

@Controller
public class bancoControlador {

	@Autowired
	private ICuentaServicio cuentaServicio;
	
	@Autowired
    private IUsuarioServicio usuarioServicio;

	
	/**
	 * Gestiona la solicitud HTTP GET para llevar a la página de home una vez logeado con exito.
	 * @return La vista de home.html
	 */
	@GetMapping("/privada/home")
	public String loginCorrecto(Model model, Authentication authentication) {
		try {
			
		// Obtener el nombre de usuario de la autenticación
        String mail = authentication.getName();
        System.out.println(mail);
        // Obtener el objeto Usuario desde tu servicio
        Usuario usuario = usuarioServicio.buscarPorEmail(mail);

        // Obtener el idUsuario
        Long idUsuario = usuario.getIdUsuario();
        
        model.addAttribute("idUsuario", idUsuario);
		
		
		List<CuentaBancaria> cuentasBancarias = cuentaServicio.obtenerCuentasDeUsuario(idUsuario);
		List<CuentaBancariaDTO> cuentasBancariasDto = Util.cuentaBancariaToDto(cuentasBancarias);
		
		CuentaBancariaDTO cuentaDTO = cuentasBancariasDto.get(0);
		UsuarioDTO usuariodto = Util.usuarioToDto(usuario);
		
				
		model.addAttribute("cuentaBancaria", cuentaDTO);
		model.addAttribute("usuario", usuariodto);
		System.out.println(authentication.getAuthorities());
		}catch (Exception e) {
			System.out.println("Error [bancoControlador-loginCorrecto]");// TODO: handle exception
		}
		return "home";
	}
	
	
}
