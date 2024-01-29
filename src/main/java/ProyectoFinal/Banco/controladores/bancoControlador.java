package ProyectoFinal.Banco.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.servicios.ICuentaServicio;
import ProyectoFinal.Banco.servicios.ITransaccionServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;

@Controller
public class bancoControlador {

	@Autowired
	private ICuentaServicio cuentaServicio;
	
	@Autowired
    private IUsuarioServicio usuarioServicio;
	
	@Autowired
    private ITransaccionServicio transaccionServicio;

	
	/**
	 * Gestiona la solicitud HTTP GET para llevar a la página de home una vez logeado con exito.
	 * @return La vista de home.html
	 */
	@GetMapping("/privada/home")
	public String loginCorrecto(Model model, Authentication authentication, @RequestParam(defaultValue = "0") int pagina) {
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
		CuentaBancaria cuentaDAO = cuentasBancarias.get(0);
		UsuarioDTO usuariodto = Util.usuarioToDto(usuario);
		
		List<Transaccion> listaCompletaTransacciones = transaccionServicio.obtenerTransaccionesDeUsuario(cuentaDAO);
		
		// Calcula el índice inicial y final de las transacciones para la página actual
        int indiceInicial = pagina * 3;
        int indiceFinal = Math.min(indiceInicial + 3, listaCompletaTransacciones.size());

        // Obtiene solo las transacciones para la página actual
        List<Transaccion> transaccionesPaginaActual = listaCompletaTransacciones.subList(indiceInicial, indiceFinal);

        // Envía información sobre si hay una página siguiente disponible
        model.addAttribute("haySiguientePagina", indiceFinal < listaCompletaTransacciones.size());

        // Envía información sobre si hay una página anterior disponible
        model.addAttribute("hayPaginaAnterior", pagina > 0);

        // Envía el número de página actual para la paginación
        model.addAttribute("paginaActual", pagina);

        // Envía la lista de transacciones para mostrar en la página
        model.addAttribute("transacciones", transaccionesPaginaActual);
		
				
		model.addAttribute("cuentaBancaria", cuentaDTO);
		model.addAttribute("usuario", usuariodto);
		
		System.out.println(authentication.getAuthorities());
		}catch (Exception e) {
			System.out.println("Error [bancoControlador-loginCorrecto]");// TODO: handle exception
		}
		return "home";
	}
	
	
}
