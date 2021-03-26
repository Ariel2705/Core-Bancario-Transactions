
package ec.edu.espe.corebancario.transactions.api;

import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.transactions.model.Transaction;
import ec.edu.espe.corebancario.transactions.service.TransactionService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerUnitTest {
    
    @Mock
    private TransactionService service;
    
    @InjectMocks
    Transaction transaction;
    
    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
    }

    @Test
    public void givenNumberAccountAndLimitReturnListOfLastTransactions() {
        String numberAccount = "270000000001";
        Integer limit = 3;
        TransactionController controller = new TransactionController(service);
        List<Transaction> result = new ArrayList<>();
        try {
            when (service.listLastTransactions(numberAccount, limit))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.listXlastTransactions(numberAccount, limit));
    }
    
    @Test
    public void givenNullAndLimitThrowDocumentNotFoundException() {
        String numberAccount = null;
        Integer limit = 1;
        TransactionController controller = new TransactionController(service);
        try {
            lenient().when(service.listLastTransactions(numberAccount,limit))
                    .thenThrow(DocumentNotFoundException.class);
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(TransactionControllerUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void givenNumberAccountAndLimitReturnListOfLastTransactionsByType() {
        String numberAccount = "270000000001";
        String type = "1";
        Integer limit = 3;
        TransactionController controller = new TransactionController(service);
        List<Transaction> result = new ArrayList<>();
        try {
            when (service.listLastTransactionsByType(numberAccount, type, limit))
                    .thenReturn(result);
        } catch (DocumentNotFoundException ex) {
            
        }
        ResponseEntity response = ResponseEntity.ok(result);
        Assertions.assertEquals(response, controller.listXlastTransactionsByType(type, numberAccount, limit));
    }
    
    @Test
    public void givenObjectTransactionCreateNewTransaction() {
        transaction.setId("1");
        transaction.setIdentification("1804915617");
        transaction.setAccount("270000000001");
        transaction.setCreationDate(new Date());
        transaction.setType("1");
        transaction.setDescription("Deposito de la 10 a la cuenta de 270000000001");
        transaction.setMont(new BigDecimal("10.0"));
        transaction.setBalanceAccount(new BigDecimal("100.0"));
        TransactionController controller = new TransactionController(service);
        ResponseEntity response = ResponseEntity.ok().build();
        Assertions.assertEquals(response, controller.create(transaction));
    }
    
    @Test
    public void givenObjectTransactionReturnCardPayment() {
        transaction.setId("1");
        transaction.setIdentification("1804915617");
        transaction.setAccount("270000000001");
        transaction.setCreationDate(new Date());
        transaction.setType("4");
        transaction.setDescription("Pago de trajeta de credito de 10, en la cuenta de 270000000001");
        transaction.setMont(new BigDecimal("10.0"));
        transaction.setBalanceAccount(new BigDecimal("0.0"));
        TransactionController controller = new TransactionController(service);
        ResponseEntity response = ResponseEntity.ok().build();
        Assertions.assertEquals(response, controller.cardPayment(transaction));
    }
}
