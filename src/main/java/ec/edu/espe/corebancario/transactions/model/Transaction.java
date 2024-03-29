package ec.edu.espe.corebancario.transactions.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
