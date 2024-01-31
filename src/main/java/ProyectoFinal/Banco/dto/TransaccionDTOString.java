package ProyectoFinal.Banco.dto;

/**
 * Clase que representa un objeto de transferencia de datos (DTO) para transacciones con datos de tipo String.
 */
public class TransaccionDTOString {

    // ATRIBUTOS
    private long idTransaccion;
    private String usuarioTransaccionRemitente;
    private String usuarioTransaccionDestinatario;
    private double cantidadTransaccion;

    // CONSTRUCTORES
    public TransaccionDTOString() {
        super();
    }

    public TransaccionDTOString(long idTransaccion, String usuarioTransaccionRemitente,
            String usuarioTransaccionDestinatario, double cantidadTransaccion) {
        super();
        this.idTransaccion = idTransaccion;
        this.usuarioTransaccionRemitente = usuarioTransaccionRemitente;
        this.usuarioTransaccionDestinatario = usuarioTransaccionDestinatario;
        this.cantidadTransaccion = cantidadTransaccion;
    }

    // GETTERS Y SETTERS 
    public long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getUsuarioTransaccionRemitente() {
        return usuarioTransaccionRemitente;
    }

    public void setUsuarioTransaccionRemitente(String usuarioTransaccionRemitente) {
        this.usuarioTransaccionRemitente = usuarioTransaccionRemitente;
    }

    public String getUsuarioTransaccionDestinatario() {
        return usuarioTransaccionDestinatario;
    }

    public void setUsuarioTransaccionDestinatario(String usuarioTransaccionDestinatario) {
        this.usuarioTransaccionDestinatario = usuarioTransaccionDestinatario;
    }

    public double getCantidadTransaccion() {
        return cantidadTransaccion;
    }

    public void setCantidadTransaccion(double cantidadTransaccion) {
        this.cantidadTransaccion = cantidadTransaccion;
    }

}
