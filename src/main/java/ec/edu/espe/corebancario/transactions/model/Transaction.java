/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String identification;    
    private String account;
    private Date creationDate;
    private String type;
    private String description;
    private BigDecimal mont;
    private BigDecimal balanceAccount;
    
}
