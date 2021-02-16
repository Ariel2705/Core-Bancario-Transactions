/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.api;

import ec.edu.espe.corebancario.transactions.exception.InsertException;
import ec.edu.espe.corebancario.transactions.service.TransactionService;
import ec.edu.espe.corebancario.transactions.model.Transaction;
import ec.edu.espe.corebancario.transactions.api.dto.FindXTransactionRQ;
import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/api/corebancario/transaction")
@Slf4j

public class TransactionController {
  
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }    
    
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Transaction transaction) {
        try {
            this.service.createTrasaction(transaction);
            return ResponseEntity.ok().build();
        } catch (InsertException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/listXLastTransactions")
    public ResponseEntity listXLastTransactions(@RequestBody FindXTransactionRQ transactionRQ) {
        try {
            return ResponseEntity.ok(this.service.listXLastTransactions(transactionRQ.getIdentificationSender(), transactionRQ.getLimit()));
        } catch (DocumentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
