package ProyectoFinal.Banco.controladores;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Prestamo;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.PrestamoDTO;
import ProyectoFinal.Banco.dto.TransaccionDTO;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.servicios.ITransaccionServicio;
import ProyectoFinal.Banco.servicios.IUsuarioServicio;
import ProyectoFinal.Banco.servicios.Util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
                                @ModelAttribute("mensajeCitaError") String mensajeCitaError,
                                @ModelAttribute("mensajeCuentaLimite") String mensajeCuentaLimite,
                                @ModelAttribute("mensajeCuentaCreada") String mensajeCuentaCreada) {
        try {
            String mail = authentication.getName();
            Usuario usuario = usuarioServicio.buscarPorEmail(mail);
            
            if (usuario != null) {
                List<CuentaBancaria> cuentasBancarias1 = usuario.getCuentasBancarias();
                
                List<CuentaBancariaDTO> cuentasBancariasDto = Util.cuentaBancariaToDto(cuentasBancarias1);
                
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
                List<Prestamo> prestamos = cuentaActual.getPrestamosCuenta();
                List<PrestamoDTO> prestamosDTO = Util.listaPrestamosToDto(prestamos);
                model.addAttribute("haySiguientePagina", indiceFinal < listaCompletaTransacciones.size());
                model.addAttribute("hayPaginaAnterior", pagina > 0);
                model.addAttribute("paginaActual", pagina);
                model.addAttribute("cuentaIndex", cuentaIndex);
                model.addAttribute("transacciones", transaccionesPaginaActual);
                model.addAttribute("cuentaBancaria", cuentaBancariaDto);
                model.addAttribute("cuentasBancarias", cuentasBancariasDto);
                model.addAttribute("cuentasBancariasDto", cuentasBancariasDto);
                model.addAttribute("usuarioTransaccionRemitente", cuentaBancariaDto.getCodigoIban());
                model.addAttribute("usuario", usuariodto);
                model.addAttribute("prestamos", prestamosDTO);
                model.addAttribute("mensajeTransaccionExitosa", mensajeTransaccionExitosa);
                model.addAttribute("mensajeTransaccionError", mensajeTransaccionError);
                model.addAttribute("mensajeTransaccionErrorSaldo", mensajeTransaccionErrorSaldo);
                model.addAttribute("mensajeTransaccionErrorCuenta", mensajeTransaccionErrorCuenta);
                model.addAttribute("mensajeTransaccionErrorExisteCuenta", mensajeTransaccionErrorExisteCuenta);
                model.addAttribute("mensajeCuentaCreada", mensajeCuentaCreada);
                model.addAttribute("mensajeCuentaLimite", mensajeCuentaLimite);
                model.addAttribute("mensajeCitaError", mensajeCitaError);
            } else {
                // Manejo de caso cuando el usuario es nulo
                // Puedes redirigir a una página de error o manejarlo de otra manera apropiada.
                return "error";
            }
        } catch (Exception e) {
            // Manejo de excepción
            System.out.println("Error [bancoControlador-loginCorrecto]: " + e.getMessage());
            // Puedes redirigir a una página de error o manejarlo de otra manera apropiada.
            return "error";
        }
        return "home";
    }
    
    @GetMapping("/privada/descargarExtracto")
    public ResponseEntity<Resource> descargarExtractoBancario(Authentication authentication,
            @RequestParam(defaultValue = "0") int cuentaIndex) {
        try {
            // Obtener el usuario autenticado y su cuenta seleccionada
            String mail = authentication.getName();
            Usuario usuario = usuarioServicio.buscarPorEmail(mail);
            List<CuentaBancaria> cuentasBancarias = usuario.getCuentasBancarias();
            CuentaBancaria cuentaActual = cuentasBancarias.get(cuentaIndex);
            
            // Generar el extracto bancario en formato PDF
            byte[] contenidoPDF = generarExtractoBancarioPDF(cuentaActual);

            // Crear un recurso a partir del contenido del extracto
            ByteArrayResource resource = new ByteArrayResource(contenidoPDF);

            // Devolver el recurso como respuesta para descargar
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=extracto_bancario.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            // Manejo de excepción
            System.out.println("Error al descargar el extracto bancario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private byte[] generarExtractoBancarioPDF(CuentaBancaria cuenta) throws Exception {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        List<Transaccion> listaCompletaTransacciones = transaccionServicio.obtenerTransaccionesDeUsuario(cuenta);
        // Crear un nuevo documento PDF
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        // Abrir el documento
        document.open();

        // Agregar contenido al documento
        document.add(new Paragraph("Extracto Bancario de la cuenta: " + cuenta.getCodigoIban()));
        document.add(new Paragraph("Saldo actual: " + cuenta.getSaldoCuenta() + " €"));

        // Agregar las transacciones al documento
        if (!listaCompletaTransacciones.isEmpty()) {
            document.add(new Paragraph("Historial de Transacciones:"));
            document.add(new Paragraph("--------------------------------------------------------------------------------------------"));
            for (Transaccion transaccion : listaCompletaTransacciones) {
                document.add(new Paragraph("Cantidad: " + transaccion.getCantidadTransaccion() + " €"));
                document.add(new Paragraph("Cuenta Destinataria: " + transaccion.getUsuarioTransaccionDestinatario().getCodigoIban()));
                document.add(new Paragraph("Cuenta Remitente: " + transaccion.getUsuarioTransaccionRemitente().getCodigoIban()));
                String fechaFormateada = dateFormat.format(transaccion.getFechaTransaccion().getTime());
                document.add(new Paragraph("Fecha: " + fechaFormateada));
                document.add(new Paragraph("--------------------------------------------------------------------------------------------"));
                document.add(new Paragraph(" "));
            }
        } else {
            document.add(new Paragraph("No hay transacciones para mostrar."));
        }

        // Cerrar el documento
        document.close();

        return baos.toByteArray();
    }}
