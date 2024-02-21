package ProyectoFinal.Banco.servicios;

import java.util.List;

import ProyectoFinal.Banco.dao.CuentaBancaria;

/**
 * Interfaz del servicio para la gestión de cuentas bancarias, donde se declaran los
 * métodos correspondientes que serán implementados.
 */
public interface ICuentaServicio {

    /**
     * Obtiene todas las cuentas bancarias asociadas a un usuario.
     *
     * @param usuarioId El identificador del usuario.
     * @return Una lista de cuentas bancarias asociadas al usuario.
     */
    public List<CuentaBancaria> obtenerCuentasDeUsuario(Long usuarioId);

    /**
     * Registra una nueva cuenta bancaria.
     *
     * @param cuenta La cuenta bancaria a registrar.
     * @return La cuenta bancaria registrada.
     */
    public CuentaBancaria registrarCuenta(CuentaBancaria cuenta);
    
    /**
     * Elimina una cuenta bancaria.
     * 
     * @param id El identificador de la cuenta bancaria a eliminar.
     * @return La cuenta bancaria eliminada, o null si no se encuentra ninguna cuenta con el identificador proporcionado.
     */
    public CuentaBancaria eliminarCuenta(long id);
}