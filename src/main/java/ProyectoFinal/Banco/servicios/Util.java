package ProyectoFinal.Banco.servicios;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.Cita;
import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Oficina;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CitaDTO;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.OficinaDTO;
import ProyectoFinal.Banco.dto.TransaccionDTO;
import ProyectoFinal.Banco.dto.UsuarioDTO;

/**
 * Clase utilitaria con métodos estáticos para realizar conversiones entre objetos DTO y DAO.
 */
@Service
public class Util {

    /**
     * Convierte un objeto CuentaBancaria a un objeto CuentaBancariaDTO.
     * @param cuentaBancaria El objeto CuentaBancaria a convertir.
     * @return El objeto CuentaBancariaDTO resultante.
     */
    static public CuentaBancariaDTO cuentaToDTO(CuentaBancaria cuentaBancaria) {
        try {
            CuentaBancariaDTO dto = new CuentaBancariaDTO();
            dto.setIdCuenta(cuentaBancaria.getIdCuenta());
            dto.setUsuario(cuentaBancaria.getIdCuenta()); // ¿Esto es correcto?
            dto.setSaldo(cuentaBancaria.getSaldoCuenta());
            dto.setCodigoIban(cuentaBancaria.getCodigoIban());
            return dto;
        } catch (Exception e) {
            System.out.println("[ERROR en cuentaToDTO()] - Error al convertir CuentaBancaria a CuentaBancariaDTO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte una lista de CuentaBancaria a una lista de CuentaBancariaDTO.
     * @param listaCuentas La lista de CuentaBancaria a convertir.
     * @return La lista de CuentaBancariaDTO resultante.
     */
    static public List<CuentaBancariaDTO> cuentaBancariaToDto(List<CuentaBancaria> listaCuentas) {
        try {
            List<CuentaBancariaDTO> listaDto = new ArrayList<>();
            for (CuentaBancaria cuentaBancaria : listaCuentas) {
                listaDto.add(Util.cuentaToDTO(cuentaBancaria));
            }
            return listaDto;
        } catch (Exception e) {
            System.out.println("[ERROR en cuentaBancariaToDto()] - Error al convertir lista de CuentaBancaria a lista de CuentaBancariaDTO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte un objeto Usuario a un objeto UsuarioDTO.
     * @param usuario El objeto Usuario a convertir.
     * @return El objeto UsuarioDTO resultante.
     */
    static public UsuarioDTO usuarioToDto(Usuario usuario) {
        try {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setNombreUsuario(usuario.getNombreUsuario());
            dto.setApellidosUsuario(usuario.getApellidosUsuario());
            dto.setRolUsuario(usuario.getRol());
            dto.setDniUsuario(usuario.getDniUsuario());
            dto.setTlfUsuario(usuario.getTlfUsuario());
            dto.setEmailUsuario(usuario.getEmailUsuario());
            dto.setClaveUsuario(usuario.getClaveUsuario());
            dto.setToken(usuario.getToken());
            dto.setExpiracionToken(usuario.getExpiracionToken());
            dto.setFchAltaUsuario(usuario.getFchAltaUsuario());
            dto.setId(usuario.getIdUsuario());
            return dto;
        } catch (Exception e) {
            System.out.println("[ERROR en usuarioToDto()] - Error al convertir Usuario a UsuarioDTO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte una lista de Usuario a una lista de UsuarioDTO.
     * @param listaUsuario La lista de Usuario a convertir.
     * @return La lista de UsuarioDTO resultante.
     */
    static public List<UsuarioDTO> listaUsuarioToDto(List<Usuario> listaUsuario) {
        try {
            List<UsuarioDTO> listaDto = new ArrayList<>();
            for (Usuario usuario : listaUsuario) {
                listaDto.add(Util.usuarioToDto(usuario));
            }
            return listaDto;
        } catch (Exception e) {
            System.out.println("[ERROR en listaUsuarioToDto()] - Error al convertir lista de Usuario a lista de UsuarioDTO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte un objeto UsuarioDTO a un objeto Usuario.
     * @param usuarioDTO El objeto UsuarioDTO a convertir.
     * @return El objeto Usuario resultante.
     */
    static public Usuario usuarioToDao(UsuarioDTO usuarioDTO) {
        Usuario usuarioDao = new Usuario();
        try {
            usuarioDao.setNombreUsuario(usuarioDTO.getNombreUsuario());
            usuarioDao.setApellidosUsuario(usuarioDTO.getApellidosUsuario());
            usuarioDao.setEmailUsuario(usuarioDTO.getEmailUsuario());
            usuarioDao.setClaveUsuario(usuarioDTO.getClaveUsuario());
            usuarioDao.setTlfUsuario(usuarioDTO.getTlfUsuario());
            usuarioDao.setDniUsuario(usuarioDTO.getDniUsuario());
            return usuarioDao;
        } catch (Exception e) {
            System.out.println("[ERROR en usuarioToDao()] - Error al convertir UsuarioDTO a Usuario: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte una lista de UsuarioDTO a una lista de Usuario.
     * @param listaUsuarioDTO La lista de UsuarioDTO a convertir.
     * @return La lista de Usuario resultante.
     */
    static public List<Usuario> listUsuarioToDao(List<UsuarioDTO> listaUsuarioDTO) {
        List<Usuario> listaUsuarioDao = new ArrayList<>();
        try {
            for (UsuarioDTO usuarioDTO : listaUsuarioDTO) {
                listaUsuarioDao.add(usuarioToDao(usuarioDTO));
            }
            return listaUsuarioDao;
        } catch (Exception e) {
            System.out.println("[ERROR en listUsuarioToDao()] - Error al convertir lista de UsuarioDTO a lista de Usuario: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte un objeto TransaccionDTO a un objeto Transaccion.
     * @param transaccionDto El objeto TransaccionDTO a convertir.
     * @return El objeto Transaccion resultante.
     */
    static public Transaccion transaccionToDao(TransaccionDTO transaccionDto) {
        Transaccion transaccionDao = new Transaccion();
        try {
            transaccionDao.setCantidadTransaccion(transaccionDto.getCantidadTransaccion());
            transaccionDao.setUsuarioTransaccionDestinatario(transaccionDto.getUsuarioTransaccionDestinatario());
            transaccionDao.setUsuarioTransaccionRemitente(transaccionDto.getUsuarioTransaccionRemitente());
            return transaccionDao;
        } catch (Exception e) {
            System.out.println("[ERROR en transaccionToDao()] - Error al convertir TransaccionDTO a Transaccion: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Convierte un objeto de tipo Oficina a un objeto de tipo OficinaDTO.
     * 
     * @param oficina El objeto de tipo Oficina que se desea convertir.
     * @return Un objeto de tipo OficinaDTO.
     */
    static public OficinaDTO oficinaToDto(Oficina oficina) {
        try {
            OficinaDTO dto = new OficinaDTO();
            
            dto.setIdOficina(oficina.getIdOficina());
            dto.setDireccionOficina(oficina.getDireccionOficina());
            dto.setCitasOficina(oficina.getCitasOficina());
            
            return dto;
        } catch (Exception e) {
            System.out.println("[ERROR en oficinaToDto()] - Error al convertir Oficina a OficinaDTO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte una lista de objetos de tipo Oficina a una lista de objetos de tipo OficinaDTO.
     * 
     * @param listaOficina La lista de oficinas que se desea convertir.
     * @return Una lista de objetos de tipo OficinaDTO.
     */
    static public List<OficinaDTO> listaOficinasToDto(List<Oficina> listaOficina) {
        try {
            List<OficinaDTO> listaDto = new ArrayList<>();
            for (Oficina oficina : listaOficina) {
                listaDto.add(Util.oficinaToDto(oficina));
            }
            return listaDto;
        } catch (Exception e) {
            System.out.println("[ERROR en listaOficinasToDto()] - Error al convertir lista de Oficinas a lista de OficinasDTO: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Convierte un objeto de tipo Transaccion a un objeto de tipo TransaccionDTO.
     * 
     * @param transaccion El objeto de tipo Transaccion que se desea convertir.
     * @return Un objeto de tipo TransaccionDTO.
     */
    static public TransaccionDTO transaccionToDto(Transaccion transaccion) {
        try {
            TransaccionDTO dto = new TransaccionDTO();
            
            dto.setCantidadTransaccion(transaccion.getCantidadTransaccion());
            dto.setUsuarioTransaccionDestinatario(transaccion.getUsuarioTransaccionDestinatario());
            dto.setUsuarioTransaccionRemitente(transaccion.getUsuarioTransaccionRemitente());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(transaccion.getFechaTransaccion().toInstant(), ZoneId.systemDefault());
            dto.setFechaTransaccion(localDateTime);
            
            return dto;
        } catch (Exception e) {
            System.out.println("[ERROR en transaccionToDto()] - Error al convertir Transaccion a TransaccionDTO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte una lista de objetos de tipo Transaccion a una lista de objetos de tipo TransaccionDTO.
     * 
     * @param listaTransacciones La lista de transacciones que se desea convertir.
     * @return Una lista de objetos de tipo TransaccionDTO.
     */
    static public List<TransaccionDTO> listaTransaccionesToDto(List<Transaccion> listaTransacciones) {
        try {
            List<TransaccionDTO> listaDto = new ArrayList<>();
            for (Transaccion transaccion : listaTransacciones) {
                listaDto.add(Util.transaccionToDto(transaccion));
            }
            return listaDto;
        } catch (Exception e) {
            System.out.println("[ERROR en listaTransaccionesToDto()] - Error al convertir lista de Transacciones a lista de TransaccionesDTO: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Convierte una lista de objetos de tipo Cita a una lista de objetos de tipo CitaDTO.
     * 
     * @param listaCita La lista de citas que se desea convertir.
     * @return Una lista de objetos de tipo CitaDTO.
     */
    static public List<CitaDTO> listaCitasToDto(List<Cita> listaCita) {
        try {
            List<CitaDTO> listaDto = new ArrayList<>();
            for (Cita cita : listaCita) {
                listaDto.add(Util.citaToDto(cita));
            }
            return listaDto;
        } catch (Exception e) { // Captura todas las excepciones no manejadas
            System.out.println("[ERROR en listaCitasToDto()] - Error al convertir lista de Cita a lista de CitaDTO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte un objeto de tipo Cita a un objeto de tipo CitaDTO.
     * 
     * @param cita El objeto de tipo Cita que se desea convertir.
     * @return Un objeto de tipo CitaDTO.
     */
    static public CitaDTO citaToDto(Cita cita) {
        try {
            CitaDTO dto = new CitaDTO();

            dto.setIdCita(cita.getIdCita());
            dto.setMotivoCita(cita.getMotivoCita());
            dto.setOficinaCita(cita.getOficinaCita());
            dto.setUsuarioCita(cita.getUsuarioCita());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(cita.getFechaCita().toInstant(), ZoneId.systemDefault());
            dto.setFechaCita(localDateTime);

            return dto;
        } catch (Exception e) { // Captura todas las excepciones no manejadas
            System.out.println("[ERROR en citaToDto()] - Error al convertir Cita a CitaDTO: " + e.getMessage());
            return null;
        }
    }
}

