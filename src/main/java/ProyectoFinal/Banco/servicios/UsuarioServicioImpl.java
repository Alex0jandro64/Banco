package ProyectoFinal.Banco.servicios;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.repositorios.CuentaRepositorio;
import ProyectoFinal.Banco.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;

/**
 * Servicio que implementa los metodos de la interface {@link IUsuarioServicio}
 * y en esta clase es donde se entra al detalle de la logica de dichos métodos
 * para la gestión de usuarios.
 */
@Service
@Transactional
public class UsuarioServicioImpl implements IUsuarioServicio {

	@Autowired
	private UsuarioRepositorio repositorioUsuario;
	
	@Autowired
	private CuentaRepositorio repositorioCuenta;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IEmailServicio emailServicio;

	@Override
	public Usuario registrar(UsuarioDTO userDto) {

		try {
			// Comprueba si ya existe un usuario con el email que quiere registrar
			Usuario usuarioDaoByEmail = repositorioUsuario.findFirstByEmailUsuario(userDto.getEmailUsuario());

			if (usuarioDaoByEmail != null) {
				return null; // Si no es null es que ya está registrado
			}
			userDto.setClaveUsuario(passwordEncoder.encode(userDto.getClaveUsuario()));
			Usuario usuarioDao = Util.usuarioToDao(userDto);			// Ahora se comprueba si hay un usuario por el DNI que quiere registrar
			boolean yaExisteElDNI = repositorioUsuario.existsByDniUsuario(usuarioDao.getDniUsuario());

			if (yaExisteElDNI) {
				// Si es que ya hay un usuario con ese dni se setea a null para controlar el
				// error en controlador
				userDto.setDniUsuario(null);
				return usuarioDao;
			}

			// Si llega a esta línea es que no existe el usuario con el email y el dni a
			// registrar
			
			
			usuarioDao.setRol("ROLE_USER");
			usuarioDao.setFchAltaUsuario(Calendar.getInstance());
			repositorioUsuario.save(usuarioDao);

			return usuarioDao;
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error UsuarioServicioImpl - registrar() ]" + iae.getMessage());
		} catch (Exception e) {
			System.out.println("[Error UsuarioServicioImpl - registrar() ]" + e.getMessage());
		}
		return null;
	}

	/**
	 * Metodo que ejecuta la creacion de un usuario administrador con su rol de administrador
	 */
	private void inicializarUsuarioAdmin() {
		// Comprueba si ya existe un usuario admin
		if (!repositorioUsuario.existsByNombreUsuario("admin")) {
			// Si no existe, crea un nuevo usuario con rol de administrador
			Usuario admin = new Usuario();
			admin.setNombreUsuario("admin");
			admin.setClaveUsuario(passwordEncoder.encode("admin"));
			admin.setDniUsuario("-");
			admin.setEmailUsuario("admin@admin.com");
			admin.setRol("ROLE_ADMIN");

			CuentaBancaria cuentaAdmin = new CuentaBancaria();
			cuentaAdmin.setUsuarioCuenta(admin);
			cuentaAdmin.setSaldoCuenta(120.50);
			repositorioUsuario.save(admin);
			repositorioCuenta.save(cuentaAdmin);
		}
	}

	/**
	 * Metodo que automatiza la creacion de un usuario administrador que se ejecuta la primera vez que se despliega la aplicacion
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		inicializarUsuarioAdmin();
	}

	@Override
	public boolean iniciarResetPassConEmail(String emailUsuario) {
		try {
			Usuario usuarioExistente = repositorioUsuario.findFirstByEmailUsuario(emailUsuario);

			if (usuarioExistente != null) {
				// Generar el token y establece la fecha de expiración
				String token = passwordEncoder.encode(RandomStringUtils.random(30));
				Calendar fechaExpiracion = Calendar.getInstance();
				fechaExpiracion.add(Calendar.MINUTE, 10);
				// Actualizar el usuario con el nuevo token y la fecha de expiración
				usuarioExistente.setToken(token);
				usuarioExistente.setExpiracionToken(fechaExpiracion);

				// Actualizar el usuario en la base de datos
				repositorioUsuario.save(usuarioExistente);

				// Enviar el correo de recuperación
				String nombreUsuario = usuarioExistente.getNombreUsuario() + " "
						+ usuarioExistente.getApellidosUsuario();
				emailServicio.enviarEmailRecuperacion(emailUsuario, nombreUsuario, token);

				return true;

			} else {
				System.out.println("[Error UsuarioServicioImpl - iniciarRecuperacionConEmail()] El usuario con email "
						+ emailUsuario + " no existe");
				return false;
			}
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error UsuarioServicioImpl - registrar() ]" + iae.getMessage());
			return false;
		} catch (Exception e) {
			System.out.println("[Error UsuarioServicioImpl - iniciarRecuperacionConEmail()]" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean modificarContraseñaConToken(UsuarioDTO usuario) {

		Usuario usuarioExistente = repositorioUsuario.findByToken(usuario.getToken());

		if (usuarioExistente != null) {
			String nuevaContraseña = passwordEncoder.encode(usuario.getPassword());
			usuarioExistente.setClaveUsuario(nuevaContraseña);
			usuarioExistente.setToken(null); // Se setea a null para invalidar el token ya consumido al cambiar de
												// password
			repositorioUsuario.save(usuarioExistente);

			return true;
		}

		return false;
	}

	@Override
	public UsuarioDTO obtenerUsuarioPorToken(String token) {
		Usuario usuarioExistente = repositorioUsuario.findByToken(token);

		if (usuarioExistente != null) {
			UsuarioDTO usuario = Util.usuarioToDto(usuarioExistente);
			return usuario;
		} else {
			System.out.println("No existe el usuario con el token " + token);
			return null;
		}

	}

	@Override
	public Usuario buscarPorEmail(String email) {
		return repositorioUsuario.findFirstByEmailUsuario(email);
	}
	
	@Override
	public Usuario eliminar(long id) {
		Usuario usuario = repositorioUsuario.findById(id).orElse(null);
		if (usuario != null) {
			repositorioUsuario.delete(usuario);
		} 
		return usuario;
		
	}
	
	@Override
	public Usuario obtenerUsuarioPorNombre(String nombreUsuario) {
        return repositorioUsuario.findByNombreUsuario(nombreUsuario);
    }

	// ESTOS METODO NO SE USAN DE MOMENTO
	@Override
	public boolean buscarPorDni(String dni) {
		return repositorioUsuario.existsByDniUsuario(dni);
	}

	@Override
	public Usuario buscarPorId(long id) {
		return repositorioUsuario.findById(id).orElse(null);
	}

	@Override
	public List<UsuarioDTO> buscarTodos() {
		return Util.listaUsuarioToDto(repositorioUsuario.findAll());
	}
}
