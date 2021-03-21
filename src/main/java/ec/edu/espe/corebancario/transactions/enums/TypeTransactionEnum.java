package ec.edu.espe.corebancario.transactions.enums;

public enum TypeTransactionEnum {

    DEPOSITO("Dep", "Deposito"),
    RETIRO("Ret", "Retiro"),
    PAGO("Pag", "Pago");

    private final String type;
    private final String description;

    private TypeTransactionEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
