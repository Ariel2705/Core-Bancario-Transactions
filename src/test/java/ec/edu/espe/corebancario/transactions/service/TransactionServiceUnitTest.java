
package ec.edu.espe.corebancario.transactions.service;

import ec.edu.espe.corebancario.transactions.api.TransactionController;
import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.transactions.model.Transaction;
import ec.edu.espe.corebancario.transactions.repository.TransactionRepository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceUnitTest {
    
    @Mock
    private TransactionRepository repository;
    
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
        List<Transaction> transactions = 
                    repository.findByAccountOrderByCreationDateDesc(numberAccount,
                            PageRequest.of(0, limit));
        TransactionService service = new TransactionService(repository);
        try {
            Assertions.assertEquals(transactions, service.listLastTransactions(numberAccount, limit));
        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(TransactionServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void givenNullAndLimitThrowDocumentNotFoundException() {
        String numberAccount = null;
        Integer limit = -1;
        TransactionService service = new TransactionService(repository);
        Assertions.assertThrows(DocumentNotFoundException.class, ()-> service.listLastTransactions(numberAccount, limit));
    }
}
