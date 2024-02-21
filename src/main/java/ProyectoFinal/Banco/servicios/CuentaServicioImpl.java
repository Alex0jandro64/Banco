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

/**
 * Servicio que implementa los métodos de la interfaz {@link ICuentaServicio} 
 * para la gestión de cuentas bancarias.
 */
@Service
@Transactional
public class CuentaServicioImpl implements ICuentaServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private CuentaRepositorio cuentaRepository;

    /**
     * Obtiene todas las cuentas bancarias asociadas a un usuario.
     *
     * @param usuarioId El identificador del usuario.
     * @return Una lista de cuentas bancarias asociadas al usuario, o null si el usuario no existe.
     */
    @Override
    public List<CuentaBancaria> obtenerCuentasDeUsuario(Long usuarioId) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);

            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                return usuario.getCuentasBancarias();
            } else {
                // Manejar el caso en el que el usuario no existe
                return null;
            }
        } catch (IllegalArgumentException iae) {
            System.out.println("[Error en CuentaServicioImpl - obtenerCuentasDeUsuario()]: " + iae.getMessage());
            throw new RuntimeException("Error al obtener las cuentas del usuario", iae);
        }
    }

    /**
     * Registra una nueva cuenta bancaria.
     *
     * @param cuenta La cuenta bancaria a registrar.
     * @return La cuenta bancaria registrada, o null si ya existe una cuenta con el mismo código IBAN.
     */
    @Override
    public CuentaBancaria registrarCuenta(CuentaBancaria cuenta) {
        try {
            // Generar código IBAN único
            String codigoIban = generarIBAN();
            cuenta.setCodigoIban(codigoIban);
            
            // Comprueba si ya existe una cuenta con el código IBAN que quiere registrar
            CuentaBancaria cuentaPorIban = cuentaRepository.findFirstBycodigoIban(cuenta.getCodigoIban());

            if (cuentaPorIban != null) {
                return null; // Si no es null es que ya está registrado
            }

            cuentaRepository.save(cuenta);

            return cuenta;
        } catch (IllegalArgumentException iae) {
            System.out.println("[Error en CuentaServicioImpl - registrarCuenta()]: " + iae.getMessage());
            throw new RuntimeException("Error al registrar la cuenta bancaria", iae);
        }
    }
    
    /**
     * Elimina una cuenta bancaria por su ID.
     *
     * @param id El ID de la cuenta bancaria a eliminar.
     * @return La cuenta bancaria eliminada, o null si no se encontró ninguna cuenta con el ID especificado.
     */
    @Override
    public CuentaBancaria eliminarCuenta(long id) {
        try {
            CuentaBancaria cuenta = cuentaRepository.findById(id).orElse(null);
            if (cuenta != null) {
                cuentaRepository.delete(cuenta);
            } 
            return cuenta;
        } catch (Exception e) {
            System.out.println("[Error en CuentaServicioImpl - eliminarCuenta()]: " + e.getMessage());
            throw new RuntimeException("Error al eliminar la cuenta bancaria", e);
        }
    }

    // Métodos privados auxiliares

    /**
     * Genera un código IBAN aleatorio único.
     *
     * @return El código IBAN generado.
     */
    private static String generarIBAN() {
        // Código del país
        String codigoPais = "ES";

        // Dígitos de control inicial
        String digitosControl = "00";

        // Longitud total del IBAN
        int longitudIBAN = 22;

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

    /**
     * Genera una cadena de dígitos aleatorios.
     *
     * @param longitud La longitud de la cadena de dígitos.
     * @return La cadena de dígitos aleatorios generada.
     */
    private static String generarNumerosAleatorios(int longitud) {
        Random random = new Random();
        StringBuilder numerosAleatorios = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            numerosAleatorios.append(random.nextInt(10));
        }

        return numerosAleatorios.toString();
    }

    /**
     * Calcula los dígitos de control de un IBAN.
     *
     * @param ibanParcial El IBAN parcial al que se le calcularán los dígitos de control.
     * @return Los dígitos de control calculados.
     */
    private static String calcularDigitosControl(String ibanParcial) {
        // Tomar solo los dígitos numéricos del IBAN parcial
        String numeros = ibanParcial.replaceAll("[^0-9]", "");

        // Convertir a número y aplicar el algoritmo de módulo 97
        long numero = Long.parseLong(numeros) * 100;
        int mod = (int) (98 - (numero % 97));

        return String.format("%02d", mod);
    }
}