package ProyectoFinal.Banco.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.TransaccionDTOString;
import ProyectoFinal.Banco.servicios.ICuentaServicio;
import ProyectoFinal.Banco.servicios.ITransaccionServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;

/**
 * Controlador para gestionar las transacciones bancarias.
 */
@Controller
public class TransaccionControlador {

	@Autowired
    private ICuentaServicio cuentaServicio;
	
	@Autowired
    private IUsuarioServicio usuarioServicio;
	
    @Autowired
    private ITransaccionServicio transaccionServicio;

    @GetMapping("/privada/nuevaTransaccion")
    public String nuevaTrasaccion(Model model, Authentication authentication) {
    	String mail = authentication.getName();
        Usuario usuario = usuarioServicio.buscarPorEmail(mail);
        Long idUsuario = usuario.getIdUsuario();
        List<CuentaBancaria> cuentasBancarias = cuentaServicio.obtenerCuentasDeUsuario(idUsuario);
        List<CuentaBancariaDTO> cuentasBancariasDto = Util.cuentaBancariaToDto(cuentasBancarias);
        model.addAttribute("cuentasBancariasDto",cuentasBancariasDto);
        return "nuevaTransaccion";
    }

	@PostMapping("/privada/nuevaTransaccion")
    public String nuevaTransaccionPost(@ModelAttribute TransaccionDTOString transaccionDTOString,Authentication authentication ,RedirectAttributes redirectAttributes) {
        try {
        	
            int error = transaccionServicio.registrarTransaccion(transaccionDTOString);
            
            if (error == 1) {
                redirectAttributes.addFlashAttribute("mensajeTransaccionExitosa", "Registro del nuevo usuario OK");
            } 
            else if(error==-1) {
            	redirectAttributes.addFlashAttribute("mensajeTransaccionErrorSaldo", "No tiene saldo Suficiente.");
            }
            else if(error==2) {
            	redirectAttributes.addFlashAttribute("mensajeTransaccionErrorCuenta", "No puede hacer una tranferencia a su misma cuenta.");
            }
            else if(error==3) {
            	redirectAttributes.addFlashAttribute("mensajeTransaccionErrorExisteCuenta", "No existe la cuenta bancaria.");
            }
            else {
                redirectAttributes.addFlashAttribute("mensajeTransaccionError", "Error al registrar la transacción.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar la transacción: " + e.getMessage());
        }
        return "redirect:/privada/home";
    }

}
