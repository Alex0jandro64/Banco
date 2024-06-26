package ProyectoFinal.Banco.servicios;

import java.util.List;

import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.dto.UsuarioDTO;

/**
 * Interfaz del servicio para la gestión de usuarios, donde se declaran los
 * métodos correspondientes que serán implementados.
 */
public interface IUsuarioServicio {
	
	/**
	 * Se registra a un usuario antes comprobando si ya se encuentra en la BBDD registrado el usuario
	 * @param userDTO El usuario a registrar
	 * @return El usuario registrado
	 */
	public Usuario registrar(UsuarioDTO userDTO);
	
	/**
	 * Busca a un usuario por su identificador asignado en la bbdd
	 * @param id del usuario a buscar
	 * @return El usuario buscado
	 */
	public Usuario buscarPorId(long id);
	
	/**
	 * Busca a un usuario por su dirección de email registrada
	 * @param email del usario que se quiere encontrar
	 * @return El usuario buscado
	 */
	public Usuario buscarPorEmail(String email);
	
	/**
	 * Busca a un usuario por su dni
	 * @param dni del usuario que se quiere encontrar
	 * @return true si el usuario existe, false en caso contrario
	 */
	public boolean buscarPorDni(String dni);
	
	/**
	 * Busca todos los usuarios registrados
	 * @return la lista de todos los usuariosDTO
	 */
	public List<UsuarioDTO> buscarTodos();

	

	/**
	 * Busca un usuario por su token de recuperación.
	 * @param token que identifica al usuario recibió un correo con la url y dicho token
	 * @return el usuario buscado
	 */
	public UsuarioDTO obtenerUsuarioPorToken(String token);

	
	/**
	 * Inicia el proceso de recuperacion (generando token y vencimiento) con el email del usuario 
	 * @param emailUsuario El email del usuario a recuperar la contraseña
	 * @return true si el proceso se ha iniciado correctamente, false en caso contrario
	 */
	public boolean iniciarResetPassConEmail(String emailUsuario);
	
	/**
	 * Establece la nueva contraseña del usuario con el token
	 * @param usuario El usuario al que se le establecera la nueva contraseña
	 * @return true si el proceso se ha realizado correctamente, false en caso contrario
	 */
	public boolean modificarContraseñaConToken(UsuarioDTO usuario);

	/**
	 * Elimina un usuario por su identificador
	 * @param id del usuario
	 * @return el usuario eliminado o null si no existe
	 */
	public Usuario eliminar(long id);

	/**
	 * Obtiene un usuario por su nombre de usuario
	 * @param nombreUsuario nombre de usuario del usuario a obtener
	 * @return el usuario buscado
	 */
	/**
	 * Obtiene un usuario por su nombre de usuario.
	 * 
	 * @param nombreUsuario El nombre de usuario del usuario que se desea obtener.
	 * @return El objeto Usuario asociado al nombre de usuario proporcionado, null si no se encuentra ningún usuario.
	 */
	public Usuario obtenerUsuarioPorNombre(String nombreUsuario);

	/**
	 * Otorga un rol específico a un usuario.
	 * 
	 * @param id El identificador del usuario al que se desea otorgar el rol.
	 * @return El objeto Usuario al que se le ha otorgado el rol, null si no se encuentra ningún usuario con el identificador proporcionado.
	 */
	public Usuario darRol(long id);

	/**
	 * Confirma la cuenta de un usuario utilizando un token de confirmación.
	 * 
	 * @param token El token de confirmación del usuario.
	 * @return true si la cuenta se confirmó correctamente, false si la cuenta ya está confirmada o si ocurre un error durante el proceso.
	 */
	public boolean confirmarCuenta(String token);
}
