package ProyectoFinal.Banco.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Transaccion;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.CuentaBancariaDTO;
import ProyectoFinal.Banco.dto.TransaccionDTO;
import ProyectoFinal.Banco.dto.UsuarioDTO;

@Service
public class Util {

	static public CuentaBancariaDTO cuentaToDTO(CuentaBancaria u) {

		try {
			CuentaBancariaDTO dto = new CuentaBancariaDTO();
			dto.setIdCuenta(u.getIdCuenta());
			dto.setUsuario(u.getIdCuenta());
			dto.setSaldo(u.getSaldoCuenta());
			dto.setCodigoIban(u.getCodigoIban());

			return dto;
		} catch (Exception e) {
			System.out.println(
					"\n[ERROR CuentaToDtoImpl - CuentaToDto()] - Error al convertir usuario DAO a usuarioDTO (return null): "
							+ e);
			return null;
		}
	}

	static public List<CuentaBancariaDTO> cuentaBancariaToDto(List<CuentaBancaria> listaCuentas) {
		try {

			List<CuentaBancariaDTO> listaDto = new ArrayList<>();
			for (CuentaBancaria u : listaCuentas) {
				listaDto.add(Util.cuentaToDTO(u));
			}
			return listaDto;

		} catch (Exception e) {
			System.out.println(
					"\n[ERROR CuentaBancariaToDtoImpl - listaCuentaToDto()] - Error al convertir lista de usuario DAO a lista de usuarioDTO (return null): "
							+ e);
		}
		return null;
	}

	static public UsuarioDTO usuarioToDto(Usuario u) {

		try {
			UsuarioDTO dto = new UsuarioDTO();
			dto.setNombreUsuario(u.getNombreUsuario());
			dto.setApellidosUsuario(u.getApellidosUsuario());
			dto.setDniUsuario(u.getDniUsuario());
			dto.setTlfUsuario(u.getTlfUsuario());
			dto.setEmailUsuario(u.getEmailUsuario());
			dto.setClaveUsuario(u.getClaveUsuario());
			dto.setToken(u.getToken());
			dto.setExpiracionToken(u.getExpiracionToken());
			dto.setId(u.getIdUsuario());

			return dto;
		} catch (Exception e) {
			System.out.println(
					"\n[ERROR UsuarioToDtoImpl - usuarioToDto()] - Error al convertir usuario DAO a usuarioDTO (return null): "
							+ e);
			return null;
		}
	}

	static public List<UsuarioDTO> listaUsuarioToDto(List<Usuario> listaUsuario) {
		try {

			List<UsuarioDTO> listaDto = new ArrayList<>();
			for (Usuario u : listaUsuario) {
				listaDto.add(Util.usuarioToDto(u));
			}
			return listaDto;

		} catch (Exception e) {
			System.out.println(
					"\n[ERROR UsuarioToDtoImpl - listauUsuarioToDto()] - Error al convertir lista de usuario DAO a lista de usuarioDTO (return null): "
							+ e);
		}
		return null;
	}

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
			System.out.println(
					"\n[ERROR UsuarioToDaoImpl - toUsuarioDao()] - Al convertir usuarioDTO a usuarioDAO (return null): "
							+ e);
			return null;
		}

	}

	static public List<Usuario> listUsuarioToDao(List<UsuarioDTO> listaUsuarioDTO) {

		List<Usuario> listaUsuarioDao = new ArrayList<>();

		try {
			for (UsuarioDTO usuarioDTO : listaUsuarioDTO) {
				listaUsuarioDao.add(usuarioToDao(usuarioDTO));
			}

			return listaUsuarioDao;

		} catch (Exception e) {
			System.out.println(
					"\n[ERROR UsuarioToDaoImpl - toListUsuarioDao()] - Al convertir lista de usuarioDTO a lista de usuarioDAO (return null): "
							+ e);
		}
		return null;
	}

	static public Transaccion transaccionToDao(TransaccionDTO transaccionDto) {

		Transaccion transaccionDao = new Transaccion();

		try {
			transaccionDao.setCantidadTransaccion(transaccionDto.getCantidadTransaccion());
			transaccionDao.setUsuarioTransaccionDestinatario(transaccionDto.getUsuarioTransaccionDestinatario());
			transaccionDao.setUsuarioTransaccionRemitente(transaccionDto.getUsuarioTransaccionRemitente());

			return transaccionDao;

		} catch (Exception e) {
			System.out.println(
					"\n[ERROR Util - transaccionToDao()] - Al convertir transaccionDto a transaccionDao (return null): "
							+ e);
			return null;
		}

	}
}
