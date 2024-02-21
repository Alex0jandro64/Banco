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
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.TransaccionDTOString;
import ProyectoFinal.Banco.servicios.ITransaccionServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;

/**
 * Controlador para gestionar las transacciones bancarias.
 */
@Controller
public class TransaccionControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;
    
    @Autowired
    private ITransaccionServicio transaccionServicio;

    /**
     * Método para mostrar la vista de nueva transacción.
     * @param model El modelo utilizado para agregar los datos necesarios a la vista.
     * @param authentication La autenticación del usuario.
     * @return La vista "nuevaTransaccion".
     */
    @GetMapping("/privada/nuevaTransaccion")
    public String nuevaTrasaccion(Model model, Authentication authentication) {
        String mail = authentication.getName();
        Usuario usuario = usuarioServicio.buscarPorEmail(mail);
        List<CuentaBancaria> cuentasBancarias = usuario.getCuentasBancarias();
        List<CuentaBancariaDTO> cuentasBancariasDto = Util.cuentaBancariaToDto(cuentasBancarias);
        model.addAttribute("cuentasBancariasDto",cuentasBancariasDto);
        return "nuevaTransaccion";
    }

    /**
     * Método para procesar la solicitud de nueva transacción.
     * @param transaccionDTOString El objeto TransaccionDTOString que contiene los datos de la transacción.
     * @param authentication La autenticación del usuario.
     * @param redirectAttributes Los atributos utilizados para enviar mensajes flash a la redirección.
     * @return La redirección a la página de inicio.
     */
    @PostMapping("/privada/nuevaTransaccion")
    public String nuevaTransaccionPost(@ModelAttribute TransaccionDTOString transaccionDTOString,Authentication authentication ,RedirectAttributes redirectAttributes) {
        try {
            int error = transaccionServicio.registrarTransaccion(transaccionDTOString);
            
            if (error == 1) {
                redirectAttributes.addFlashAttribute("mensajeTransaccionExitosa", "Registro de la transacción exitoso");
            } 
            else if(error==-1) {
                redirectAttributes.addFlashAttribute("mensajeTransaccionErrorSaldo", "Saldo insuficiente para realizar la transacción");
            }
            else if(error==2) {
                redirectAttributes.addFlashAttribute("mensajeTransaccionErrorCuenta", "No se puede hacer una transferencia a la misma cuenta");
            }
            else if(error==3) {
                redirectAttributes.addFlashAttribute("mensajeTransaccionErrorExisteCuenta", "La cuenta bancaria especificada no existe");
            }
            else {
                redirectAttributes.addFlashAttribute("mensajeTransaccionError", "Error al registrar la transacción");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar la transacción: " + e.getMessage());
        }
        return "redirect:/privada/home";
    }
}