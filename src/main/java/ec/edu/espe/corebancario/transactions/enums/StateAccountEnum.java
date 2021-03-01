/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
