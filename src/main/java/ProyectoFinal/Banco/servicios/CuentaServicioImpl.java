package ProyectoFinal.Banco.servicios;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ProyectoFinal.Banco.dao.CuentaBancaria;
import ProyectoFinal.Banco.dao.Usuario;
import ProyectoFinal.Banco.repositorios.CuentaRepositorio;
import ProyectoFinal.Banco.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CuentaServicioImpl implements ICuentaServicio{

	@Autowired
    private UsuarioRepositorio usuarioRepository;
	
	@Autowired
    private CuentaRepositorio cuentaRepository;
	
	@Override
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
	
	@Override
	public CuentaBancaria registrarCuenta(CuentaBancaria cuenta) {

		try {
			String codigoIban = generarIBAN();
			cuenta.setCodigoIban(codigoIban);
			// Comprueba si ya existe una cuenta con el codigo Iban que quiere registrar
			CuentaBancaria cuentaPorIban = cuentaRepository.findFirstBycodigoIban(cuenta.getCodigoIban());
			
			
			if (cuentaPorIban != null) {
				return null; // Si no es null es que ya está registrado
			}

			cuentaRepository.save(cuenta);

			return cuenta;
		} catch (IllegalArgumentException iae) {
			System.out.println("[Error CuentaServicioImpl - registrar() ]" + iae.getMessage());
		} catch (Exception e) {
			System.out.println("[Error CuentaServicioImpl - registrar() ]" + e.getMessage());
		}
		return null;
	}
	
	public static String generarIBAN() {
        // Longitud total del IBAN
        int longitudIBAN = 22;

        // Código del país y dígitos de control inicial
        String codigoPais = "ES";
        String digitosControl = "00";

        // Generar el resto del IBAN como números aleatorios
        String numerosAleatorios = generarNumerosAleatorios(longitudIBAN - codigoPais.length() - digitosControl.length());

        // Combinar todo para formar el IBAN completo
        String ibanCompleto = codigoPais + digitosControl + numerosAleatorios;

        // Calcular dígitos de control
        String digitosControlCalculados = calcularDigitosControl(ibanCompleto);

        // Sustituir los dígitos de control iniciales con los calculados
        ibanCompleto = codigoPais + digitosControlCalculados + numerosAleatorios;

        return ibanCompleto;
    }

    private static String generarNumerosAleatorios(int longitud) {
        Random random = new Random();
        StringBuilder numerosAleatorios = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            numerosAleatorios.append(random.nextInt(10));
        }

        return numerosAleatorios.toString();
    }

    private static String calcularDigitosControl(String ibanParcial) {
        // Tomar solo los dígitos numéricos del IBAN parcial
        String numeros = ibanParcial.replaceAll("[^0-9]", "");

        // Convertir a número y aplicar el algoritmo de módulo 97
        long numero = Long.parseLong(numeros) * 100;
        int mod = (int) (98 - (numero % 97));

        return String.format("%02d", mod);
    }
}
