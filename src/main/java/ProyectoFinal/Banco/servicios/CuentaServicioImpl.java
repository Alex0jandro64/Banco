package ProyectoFinal.Banco.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CuentaServicioImpl implements ICuentaServicio{

	@Autowired
    private UsuarioRepositorio usuarioRepository;
	
	public List<CuentaBancaria> obtenerCuentasDeUsuario(Long usuarioId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return usuario.getCuentasBancarias();
        } else {
            // Manejar el caso en el que el usuario no existe
            return null;
        }
    }
}
