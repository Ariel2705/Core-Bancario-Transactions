package ec.edu.espe.corebancario.transactions.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ec.edu.espe.corebancario.transactions.constants.DomainConstant;
import ec.edu.espe.corebancario.transactions.enums.TypeTransactionEnum;
import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.transactions.exception.InsertException;
import ec.edu.espe.corebancario.transactions.model.Transaction;
import ec.edu.espe.corebancario.transactions.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TransactionServiceUnitTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;
    private Transaction transaction;
    private List<Transaction> transactions;

    @BeforeEach
    public void setUp() {
        this.transaction = new Transaction();
    }

    @Test
    public void givenObjectCreateTransaction() {
        Transaction transactionSample1 = new Transaction(null, "1804915617", "270000000001", new Date(), "Deposito", "Descripcion", new BigDecimal("5"), null);
        Transaction transactionSample2 = new Transaction(null, "1804915617", "270000000001", new Date(), "Retiro", "Descripcion", new BigDecimal("5"), null);
        Transaction transactionSample3 = new Transaction(null, "1804915617", "270000000001", new Date(), "Pago", "Descripcion", new BigDecimal("5"), null);

        try {
            service.createTrasaction(transactionSample1);
            service.createTrasaction(transactionSample2);
            service.createTrasaction(transactionSample3);
            verify(repository, times(1)).insert(transactionSample1);
            verify(repository, times(1)).insert(transactionSample2);
            verify(repository, times(1)).insert(transactionSample3);
        } catch (InsertException ex) {
            Logger.getLogger(TransactionServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenObjectReturnInsertException() {
        Transaction transactionException = new Transaction(null, "1804915617", "270000000001", new Date(), "Error", "Descripcion", new BigDecimal("5"), null);

        try {
            service.createTrasaction(transactionException);
            verify(repository, times(1)).insert(transactionException);
        } catch (InsertException ex) {
            Logger.getLogger(TransactionServiceUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenObjectTransactionReturnCardPayment() {
        Transaction transactionSample1 = new Transaction(null, "1804915617", "446758270000000004", new Date(), "Pago", "Descripcion", new BigDecimal("5"), null);
        Transaction transactionSample2 = new Transaction(null, "1804915617", "4467582700000000044", new Date(), "Pago", "Descripcion", new BigDecimal("5"), null);

        try {
            service.cardPayment(transactionSample1);
            service.cardPayment(transactionSample2);
            verify(repository, times(1)).insert(transactionSample1);
            verify(repository, times(1)).insert(transactionSample2);
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
            Logger.getLogger(TransactionServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenNullAndLimitThrowDocumentNotFoundException() {
        String numberAccount = null;
        Integer limit = -1;
        TransactionService service = new TransactionService(repository);
        Assertions
                .assertThrows(DocumentNotFoundException.class,
                        () -> service.listLastTransactions(numberAccount, limit));
    }

    @Test
    public void givenNumberAccountAndLimitReturnListOfLastTransactionsByType() {
        String numberAccount = "270000000001";
        String type = "1";
        Integer limit = 3;
        List<Transaction> transactions
                = repository.findByAccountAndTypeOrderByCreationDateDesc(numberAccount,
                        type,
                        PageRequest.of(0, limit));
        TransactionService service = new TransactionService(repository);
        try {
            Assertions.assertEquals(transactions, service.listLastTransactionsByType(numberAccount, type, limit));

        } catch (DocumentNotFoundException ex) {
            Logger.getLogger(TransactionServiceUnitTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void givenTypeAndNullAndLimitThrowDocumentNotFoundException() {
        String numberAccount = null;
        String type = "0";
        Integer limit = -1;
        TransactionService service = new TransactionService(repository);
        Assertions
                .assertThrows(DocumentNotFoundException.class,
                        () -> service.listLastTransactionsByType(numberAccount, type, limit));
    }
}
