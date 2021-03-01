/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.enums;

public enum TypeTransactionEnum {
    
    DEPOSITO("Dep", "Deposito"),
    RETIRO("Ret", "Retiro");
    
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
