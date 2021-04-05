package ec.edu.espe.corebancario.transactions.service;

import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.transactions.exception.InsertException;
import ec.edu.espe.corebancario.transactions.model.Transaction;
import ec.edu.espe.corebancario.transactions.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceUnitTest {

    @Mock
    private TransactionRepository repository;

    @Autowired
    private TransactionService service;

    /*@Test
    public void givenObjectCreateTransaction() {
        Transaction transaction = new Transaction("1", "1804915617", "270000001", new Date(), "1", "Descripcion", new BigDecimal("5"), new BigDecimal("4"));
        try {
            service.createTrasaction(transaction);
            verify(repository,times(1)).insert(transaction);
        } catch (InsertException ex) {
            Logger.getLogger(TransactionServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNumberAccountAndLimitReturnListOfLastTransactions() {
        String numberAccount = "270000000001";
        Integer limit = 3;
        List<Transaction> transactions
                = repository.findByAccountOrderByCreationDateDesc(numberAccount,
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
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.listLastTransactions(numberAccount, limit));
    }*/
}
