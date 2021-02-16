/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.api.dto;

import lombok.Data;

@Data
public class FindXTransactionRQ {
    
    private String identificationSender;
    private Integer limit;
    
}
