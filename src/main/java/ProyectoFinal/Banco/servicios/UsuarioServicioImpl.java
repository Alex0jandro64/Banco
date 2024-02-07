package ProyectoFinal.Banco.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Oficina;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.UsuarioDTO;
import ProyectoFinal.Banco.repositorios.CuentaRepositorio;
import ProyectoFinal.Banco.repositorios.OficinaRepositorio;
import ProyectoFinal.Banco.repositorios.UsuarioRepositorio;
import jakarta.persistence.PersistenceException;

/**
 * Servicio que implementa los métodos de la interfaz {@link IUsuarioServicio}
 * y en esta clase es donde se entra al detalle de la lógica de dichos métodos
 * para la gestión de usuarios.
 */
@Service
public class UsuarioServicioImpl implements IUsuarioServicio {

    @Autowired
    private UsuarioRepositorio repositorioUsuario;
    
    @Autowired
    private OficinaRepositorio repositorioOficina;

    @Autowired
    private CuentaRepositorio repositorioCuenta;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IEmailServicio emailServicio;

    @Override
    public Usuario registrar(UsuarioDTO userDto) {
        try {
            Usuario usuarioDaoByEmail = repositorioUsuario.findFirstByEmailUsuario(userDto.getEmailUsuario());

            if (usuarioDaoByEmail != null) {
                // El usuario ya está registrado y confirmado
                return null;
            }
            
            userDto.setClaveUsuario(passwordEncoder.encode(userDto.getClaveUsuario()));
            Usuario usuarioDao = Util.usuarioToDao(userDto);

            boolean yaExisteElDNI = repositorioUsuario.existsByDniUsuario(usuarioDao.getDniUsuario());

            if (yaExisteElDNI) {
                usuarioDao.setDniUsuario(null); // Controlar el error en el controlador
                return usuarioDao;
            }

            usuarioDao.setRol("ROLE_USER");
            usuarioDao.setFchAltaUsuario(Calendar.getInstance());
            
            if (userDto.isMailConfirmado()) {
				usuarioDao.setMailConfirmado(true);
				repositorioUsuario.save(usuarioDao);
			} else {
				usuarioDao.setMailConfirmado(false);
				// Generar token de confirmación
				String token = passwordEncoder.encode(RandomStringUtils.random(30));
				usuarioDao.setToken(token);
				
				// Guardar el usuario en la base de datos
				usuarioDao.setFotoPerfil(obtenerFotoPorDefecto());
				repositorioUsuario.save(usuarioDao);
				
				// Enviar el correo de confirmación
				String nombreUsuario = usuarioDao.getNombreUsuario()+" "+ usuarioDao.getApellidosUsuario();
				emailServicio.enviarEmailConfirmacion(userDto.getEmailUsuario(), nombreUsuario, token);

			}
            
            return usuarioDao;
        } catch (IllegalArgumentException iae) {
            System.out.println("[Error en UsuarioServicioImpl - registrar()]: " + iae.getMessage());
        } catch (Exception e) {
            System.out.println("[Error en UsuarioServicioImpl - registrar()]: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método que ejecuta la creación de un usuario administrador con su rol de administrador.
     */
    private void inicializarUsuarioAdmin() {
        if (!repositorioUsuario.existsByNombreUsuario("admin")) {
            Usuario admin = new Usuario();
            admin.setNombreUsuario("admin");
            admin.setClaveUsuario(passwordEncoder.encode("admin"));
            admin.setDniUsuario("-");
            admin.setEmailUsuario("admin@admin.com");
            admin.setRol("ROLE_ADMIN");

            CuentaBancaria cuentaAdmin = new CuentaBancaria();
            cuentaAdmin.setUsuarioCuenta(admin);
            cuentaAdmin.setSaldoCuenta(120.50);
            cuentaAdmin.setCodigoIban("1234");
            
            Oficina oficina = new Oficina();
            oficina.setDireccionOficina("Calle Pepe Nº2");
            
            repositorioOficina.save(oficina);
            repositorioUsuario.save(admin);
            repositorioCuenta.save(cuentaAdmin);
        }
    }

    /**
     * Método que automatiza la creación de un usuario administrador que se ejecuta la primera vez que se despliega la aplicación.
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
                String token = passwordEncoder.encode(RandomStringUtils.random(30));
                Calendar fechaExpiracion = Calendar.getInstance();
                fechaExpiracion.add(Calendar.MINUTE, 10);
                usuarioExistente.setToken(token);
                usuarioExistente.setExpiracionToken(fechaExpiracion);

                repositorioUsuario.save(usuarioExistente);

                String nombreUsuario = usuarioExistente.getNombreUsuario() + " " + usuarioExistente.getApellidosUsuario();
                emailServicio.enviarEmailRecuperacion(emailUsuario, nombreUsuario, token);

                return true;
            } else {
                System.out.println("[Error en UsuarioServicioImpl - iniciarResetPassConEmail()]: El usuario con email " + emailUsuario + " no existe");
                return false;
            }
        } catch (IllegalArgumentException iae) {
            System.out.println("[Error en UsuarioServicioImpl - iniciarResetPassConEmail()]: " + iae.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[Error en UsuarioServicioImpl - iniciarResetPassConEmail()]: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modificarContraseñaConToken(UsuarioDTO usuario) {
        Usuario usuarioExistente = repositorioUsuario.findByToken(usuario.getToken());

        if (usuarioExistente != null) {
            String nuevaContraseña = passwordEncoder.encode(usuario.getPassword());
            usuarioExistente.setClaveUsuario(nuevaContraseña);
            usuarioExistente.setToken(null);
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
    
    @Override
    public Usuario darRol(long id) {
        Usuario usuario = repositorioUsuario.findById(id).orElse(null);
        if (usuario != null) {
        	usuario.setRol("ROLE_TRABAJADOR");
            repositorioUsuario.save(usuario);
        } 
        return usuario;
    }
    
    @Override
	public boolean confirmarCuenta(String token) {
		try {
			Usuario usuarioExistente = repositorioUsuario.findByToken(token);

			if (usuarioExistente != null && !usuarioExistente.getMailConfirmado()) {
				// Entra en esta condición si el usuario existe y su cuenta no se ha confirmado
				usuarioExistente.setMailConfirmado(true);
				usuarioExistente.setToken(null);
				repositorioUsuario.save(usuarioExistente);

				return true;
			} else {
				System.out.println("La cuenta no existe o ya está confirmada");
				return false;
			}
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error UsuarioServicioImpl - confirmarCuenta()] Error al confirmar la cuenta " + iae.getMessage());
			return false;
		} catch (PersistenceException e) {
			System.out.println("[Error UsuarioServicioImpl - confirmarCuenta()] Error de persistencia al confirmar la cuenta" + e.getMessage());
			return false;
		}
	}
    
    public byte[] obtenerFotoPorDefecto() throws IOException {
        // Ruta de la imagen por defecto
        String defaultImagePath = "static/img/user_icon_149851.jpeg";
        
        // Lee la imagen por defecto como un array de bytes
        InputStream inputStream = new ClassPathResource(defaultImagePath).getInputStream();
        return IOUtils.toByteArray(inputStream);
    }

    // ESTOS MÉTODOS NO SE USAN DE MOMENTO

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
