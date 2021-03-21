package ec.edu.espe.corebancario.transactions.api.dto;

import lombok.Data;

@Data
public class FindTransactionRq {

    private String identificationSender;
    private Integer limit;

}
