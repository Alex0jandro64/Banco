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
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;

@Controller
public class datosUsuarioControlador {
	@Autowired
    private IUsuarioServicio usuarioServicio;

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
}
