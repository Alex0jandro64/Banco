package ProyectoFinal.Banco.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.TransaccionDTO;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.servicios.ITransaccionServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;

/**
 * Controlador para manejar las operaciones bancarias.
 */
@Controller
public class bancoControlador {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    @Autowired
    private ITransaccionServicio transaccionServicio;

    /**
     * Método para mostrar la página principal después del inicio de sesión.
     */
    @GetMapping("/privada/home")
    public String loginCorrecto(Model model, Authentication authentication,
                                @RequestParam(defaultValue = "0") int cuentaIndex,
                                @RequestParam(defaultValue = "0") int pagina,
                                @ModelAttribute("mensajeTransaccionExitosa") String mensajeTransaccionExitosa,
                                @ModelAttribute("mensajeTransaccionError") String mensajeTransaccionError,
                                @ModelAttribute("mensajeTransaccionErrorSaldo") String mensajeTransaccionErrorSaldo,
                                @ModelAttribute("mensajeTransaccionErrorExisteCuenta") String mensajeTransaccionErrorExisteCuenta,
                                @ModelAttribute("mensajeTransaccionErrorCuenta") String mensajeTransaccionErrorCuenta,
                                @ModelAttribute("mensajeCuentaLimite") String mensajeCuentaLimite,
                                @ModelAttribute("mensajeCuentaCreada") String mensajeCuentaCreada) {
        try {
            String mail = authentication.getName();
            Usuario usuario = usuarioServicio.buscarPorEmail(mail);
            
            List<CuentaBancaria> cuentasBancarias1 = usuario.getCuentasBancarias();
            
            List<CuentaBancariaDTO> cuentasBancariasDto = Util.cuentaBancariaToDto(cuentasBancarias1);
            
            // Asegúrate de que cuentaIndex esté dentro de los límites
            cuentaIndex = Math.min(Math.max(cuentaIndex, 0), cuentasBancarias1.size() - 1);
            
            CuentaBancaria cuentaActual = cuentasBancarias1.get(cuentaIndex);
            
            
            UsuarioDTO usuariodto = Util.usuarioToDto(usuario);
            List<Transaccion> listaCompletaTransacciones = transaccionServicio.obtenerTransaccionesDeUsuario(cuentaActual);
            listaCompletaTransacciones.sort((a, b) -> -1);
            CuentaBancariaDTO cuentaBancariaDto = Util.cuentaToDTO(cuentaActual);
            int indiceInicial = pagina * 3;
            int indiceFinal = Math.min(indiceInicial + 3, listaCompletaTransacciones.size());
            List<Transaccion> transaccionesPaginaActual1 = listaCompletaTransacciones.subList(indiceInicial, indiceFinal);
            List<TransaccionDTO> transaccionesPaginaActual = Util.listaTransaccionesToDto(transaccionesPaginaActual1);
            model.addAttribute("haySiguientePagina", indiceFinal < listaCompletaTransacciones.size());
            model.addAttribute("hayPaginaAnterior", pagina > 0);
            model.addAttribute("paginaActual", pagina);
            model.addAttribute("transacciones", transaccionesPaginaActual);
            model.addAttribute("cuentaBancaria", cuentaBancariaDto);
            model.addAttribute("cuentasBancarias", cuentasBancariasDto);
            model.addAttribute("cuentasBancariasDto", cuentasBancariasDto);
            model.addAttribute("usuarioTransaccionRemitente", cuentaBancariaDto.getCodigoIban());
            model.addAttribute("usuario", usuariodto);
            model.addAttribute("mensajeTransaccionExitosa", mensajeTransaccionExitosa);
            model.addAttribute("mensajeTransaccionError", mensajeTransaccionError);
            model.addAttribute("mensajeTransaccionErrorSaldo", mensajeTransaccionErrorSaldo);
            model.addAttribute("mensajeTransaccionErrorCuenta", mensajeTransaccionErrorCuenta);
            model.addAttribute("mensajeTransaccionErrorExisteCuenta", mensajeTransaccionErrorExisteCuenta);
            model.addAttribute("mensajeCuentaCreada", mensajeCuentaCreada);
            model.addAttribute("mensajeCuentaLimite", mensajeCuentaLimite);
        } catch (Exception e) {
            // Manejo de excepción
            System.out.println("Error [bancoControlador-loginCorrecto]");
        }
        return "home";
    }
}