package ProyectoFinal.Banco.dto;

/**
 * Clase que representa un objeto de transferencia de datos (DTO) para cuentas bancarias.
 */
public class CuentaBancariaDTO {

    // ATRIBUTOS
    private long idCuenta;
    private long usuario;
    private double saldo;
    private String codigoIban;

    // CONSTRUCTORES
    public CuentaBancariaDTO(long idCuenta, long usuario, double saldo, String codigoIban) {
        super();
        this.idCuenta = idCuenta;
        this.usuario = usuario;
        this.saldo = saldo;
        this.codigoIban = codigoIban;
    }

    public CuentaBancariaDTO() {
        super();
    }

    // GETTERS Y SETTERS
    public long getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public long getUsuario() {
        return usuario;
    }

    public void setUsuario(long usuario) {
        this.usuario = usuario;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getCodigoIban() {
        return codigoIban;
    }

    public void setCodigoIban(String codigoIban) {
        this.codigoIban = codigoIban;
    }

}
