package ec.edu.espe.corebancario.transactions.enums;

public enum StateAccountEnum {
    ACTIVO("ACT", "ACTIVO"),
    INACTIVO("INA", "INACTIVO"),
    BLOQUEADO("BLO", "BLOQUEADO"),
    SUSPEDIDO("SUS", "SUSPENDIDO");

    private final String estado;
    private final String descripcion;

    private StateAccountEnum(String estado, String descripcion) {
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
