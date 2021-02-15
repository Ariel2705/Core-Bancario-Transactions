/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.service;

import ec.edu.espe.corebancario.transactions.exception.InsertException;
import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.transactions.model.Transaction;
import ec.edu.espe.corebancario.transactions.repository.TransactionRepository;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service

public class TransactionService {
    
    private final TransactionRepository transactionRepo;

    public TransactionService(TransactionRepository accountRepo) {
        this.transactionRepo = accountRepo;
    }
    
    public void createTrasaction(Transaction transaction) throws InsertException {
        try {
            log.info("Creando transaccion "+transaction.toString());
            transaction.setCreationDate(new Date());
            this.transactionRepo.insert(transaction);
        } catch (Exception e) {
            throw new InsertException("Transaction", "Ocurrio un error al crear la transaccion: " + transaction.toString(), e);
        }
    }
    
    public List<Transaction> listXLastTransactions(Integer X, String identificacion) throws DocumentNotFoundException{
        try {
            log.info("Listando "+X+" transeferencias de: "+identificacion);
            List<Transaction> transactions = this.transactionRepo.findTop2ByIdentificationSenderOrderByCreationDateDesc(identificacion);
            log.info("\n"+transactions.size()+" "+transactions.toString());
            return transactions;
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al listar las "+X+" transacciones");
        }        
    }
}
