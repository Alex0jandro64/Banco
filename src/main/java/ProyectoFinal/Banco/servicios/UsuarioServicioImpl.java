package ProyectoFinal.Banco.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
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

    /**
     * Registra un nuevo usuario a partir de los datos proporcionados en un objeto UsuarioDTO.
     * 
     * @param userDto El objeto UsuarioDTO que contiene los datos del usuario a registrar.
     * @return El objeto Usuario recién registrado si se completa el proceso con éxito, null si el usuario ya está registrado y confirmado o si se produce un error.
     */
    @Override
    public Usuario registrar(UsuarioDTO userDto) {
        try {
            // Verificar si ya existe un usuario con el mismo correo electrónico
            Usuario usuarioDaoByEmail = repositorioUsuario.findFirstByEmailUsuario(userDto.getEmailUsuario());

            if (usuarioDaoByEmail != null) {
                // El usuario ya está registrado y confirmado
                return null;
            }
            
            // Codificar la contraseña antes de almacenarla en la base de datos
            userDto.setClaveUsuario(passwordEncoder.encode(userDto.getClaveUsuario()));
            Usuario usuarioDao = Util.usuarioToDao(userDto);

            // Verificar si ya existe un usuario con el mismo DNI
            boolean yaExisteElDNI = repositorioUsuario.existsByDniUsuario(usuarioDao.getDniUsuario());

            if (yaExisteElDNI) {
                usuarioDao.setDniUsuario(null); // Controlar el error en el controlador
                return usuarioDao;
            }

            // Establecer el rol del usuario y la fecha de alta
            usuarioDao.setRol("ROLE_USER");
            usuarioDao.setFchAltaUsuario(Calendar.getInstance());
            
            // Si el correo electrónico está confirmado, guardar el usuario
            if (userDto.isMailConfirmado()) {
                usuarioDao.setMailConfirmado(true);
                repositorioUsuario.save(usuarioDao);
            } else {
                // Si el correo electrónico no está confirmado, generar un token de confirmación, guardar el usuario y enviar un correo electrónico de confirmación
                usuarioDao.setMailConfirmado(false);
                String token = passwordEncoder.encode(RandomStringUtils.random(30));
                usuarioDao.setToken(token);
                usuarioDao.setFotoPerfil(obtenerFotoPorDefecto());
                repositorioUsuario.save(usuarioDao);
                String nombreUsuario = usuarioDao.getNombreUsuario() + " " + usuarioDao.getApellidosUsuario();
                emailServicio.enviarEmailConfirmacion(userDto.getEmailUsuario(), nombreUsuario, token);
            }
            
            return usuarioDao;
        } catch (IllegalArgumentException iae) {
            // Captura y manejo de excepciones específicas
            System.out.println("[Error en UsuarioServicioImpl - registrar()]: " + iae.getMessage());
        } catch (Exception e) {
            // Captura y manejo de excepciones generales
            System.out.println("[Error en UsuarioServicioImpl - registrar()]: " + e.getMessage());
        }
        return null; // Retorno null en caso de error
    }
    /**
     * Método que ejecuta la creación de un usuario administrador con su rol de administrador.
     */
    private void inicializarUsuarioAdmin() {
        try {
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
        } catch (Exception e) {
            System.out.println("[Error en inicializarUsuarioAdmin()]: " + e.getMessage());
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

    /**
     * Modifica la contraseña de un usuario utilizando un token de confirmación.
     * 
     * @param usuario El objeto UsuarioDTO que contiene el token y la nueva contraseña.
     * @return true si la contraseña se modificó correctamente, false si no se encontró un usuario asociado al token.
     */
    @Override
    public boolean modificarContraseñaConToken(UsuarioDTO usuario) {
        try {
            Usuario usuarioExistente = repositorioUsuario.findByToken(usuario.getToken());

            if (usuarioExistente != null) {
                String nuevaContraseña = passwordEncoder.encode(usuario.getPassword());
                usuarioExistente.setClaveUsuario(nuevaContraseña);
                usuarioExistente.setToken(null);
                repositorioUsuario.save(usuarioExistente);

                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("[Error en modificarContraseñaConToken()]: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un usuario a partir de un token de confirmación.
     * 
     * @param token El token de confirmación del usuario.
     * @return Un objeto UsuarioDTO si se encuentra un usuario asociado al token, null de lo contrario.
     */
    @Override
    public UsuarioDTO obtenerUsuarioPorToken(String token) {
        try {
            Usuario usuarioExistente = repositorioUsuario.findByToken(token);

            if (usuarioExistente != null) {
                return Util.usuarioToDto(usuarioExistente);
            } else {
                System.out.println("No existe el usuario con el token " + token);
                return null;
            }
        } catch (Exception e) {
            System.out.println("[Error en obtenerUsuarioPorToken()]: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * 
     * @param email La dirección de correo electrónico del usuario.
     * @return El objeto Usuario asociado al correo electrónico proporcionado, null si no se encuentra ningún usuario.
     */
    @Override
    public Usuario buscarPorEmail(String email) {
        try {
            return repositorioUsuario.findFirstByEmailUsuario(email);
        } catch (Exception e) {
            System.out.println("[Error en buscarPorEmail()]: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina un usuario por su identificador.
     * 
     * @param id El identificador del usuario a eliminar.
     * @return El objeto Usuario eliminado, null si no se encontró ningún usuario con el identificador proporcionado.
     */
    @Override
    public Usuario eliminar(long id) {
        try {
            Usuario usuario = repositorioUsuario.findById(id).orElse(null);
            if (usuario != null) {
                repositorioUsuario.delete(usuario);
            } 
            return usuario;
        } catch (Exception e) {
            System.out.println("[Error en eliminar()]: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene un usuario por su nombre de usuario.
     * 
     * @param nombreUsuario El nombre de usuario del usuario que se desea obtener.
     * @return El objeto Usuario asociado al nombre de usuario proporcionado, null si no se encuentra ningún usuario.
     */
    @Override
    public Usuario obtenerUsuarioPorNombre(String nombreUsuario) {
        try {
            return repositorioUsuario.findByNombreUsuario(nombreUsuario);
        } catch (Exception e) {
            System.out.println("[Error en obtenerUsuarioPorNombre()]: " + e.getMessage());
            return null;
        }
    }

    /**
     * Otorga un rol específico a un usuario.
     * 
     * @param id El identificador del usuario al que se desea otorgar el rol.
     * @return El objeto Usuario al que se le ha otorgado el rol, null si no se encuentra ningún usuario con el identificador proporcionado.
     */
    @Override
    public Usuario darRol(long id) {
        try {
            Usuario usuario = repositorioUsuario.findById(id).orElse(null);
            if (usuario != null) {
                usuario.setRol("ROLE_TRABAJADOR");
                repositorioUsuario.save(usuario);
            } 
            return usuario;
        } catch (Exception e) {
            System.out.println("[Error en darRol()]: " + e.getMessage());
            return null;
        }
    }

    /**
     * Confirma la cuenta de un usuario utilizando un token de confirmación.
     * 
     * @param token El token de confirmación del usuario.
     * @return true si la cuenta se confirmó correctamente, false si la cuenta ya está confirmada o si ocurre un error durante el proceso.
     */
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
        } catch (Exception e) {
            System.out.println("[Error en confirmarCuenta()]: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene los bytes de la foto de perfil por defecto.
     * 
     * @return Un array de bytes que representa la foto de perfil por defecto, null si ocurre un error al obtener la imagen.
     * @throws IOException Si ocurre un error de entrada/salida al leer la imagen por defecto.
     */
    public byte[] obtenerFotoPorDefecto() throws IOException {
        try {
            // Ruta de la imagen por defecto
            String defaultImagePath = "static/img/user_icon_149851.jpeg";
            
            // Lee la imagen por defecto como un array de bytes
            InputStream inputStream = new ClassPathResource(defaultImagePath).getInputStream();
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            System.out.println("[Error en obtenerFotoPorDefecto()]: " + e.getMessage());
            throw e; // Propaga la excepción para que sea manejada por el código que llama a este método.
        }
    }

    /**
     * Busca un usuario por su número de documento de identidad (DNI).
     * 
     * @param dni El número de DNI del usuario que se desea buscar.
     * @return true si se encuentra algún usuario con el DNI proporcionado, false de lo contrario.
     */
    @Override
    public boolean buscarPorDni(String dni) {
        try {
            return repositorioUsuario.existsByDniUsuario(dni);
        } catch (Exception e) {
            System.out.println("[Error en buscarPorDni()]: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un usuario por su identificador.
     * 
     * @param id El identificador del usuario que se desea buscar.
     * @return El objeto Usuario asociado al identificador proporcionado, null si no se encuentra ningún usuario con ese identificador.
     */
    @Override
    public Usuario buscarPorId(long id) {
        try {
            return repositorioUsuario.findById(id).orElse(null);
        } catch (Exception e) {
            System.out.println("[Error en buscarPorId()]: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca todos los usuarios y los devuelve en forma de lista de objetos UsuarioDTO.
     * 
     * @return Una lista de objetos UsuarioDTO que representan a todos los usuarios encontrados.
     */
    @Override
    public List<UsuarioDTO> buscarTodos() {
        try {
            return Util.listaUsuarioToDto(repositorioUsuario.findAll());
        } catch (Exception e) {
            System.out.println("[Error en buscarTodos()]: " + e.getMessage());
            return Collections.emptyList(); // Devuelve una lista vacía en caso de error
        }
    }
}
