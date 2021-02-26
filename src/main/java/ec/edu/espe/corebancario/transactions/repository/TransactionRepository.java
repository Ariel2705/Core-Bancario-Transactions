/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.repository;

import ec.edu.espe.corebancario.transactions.model.Transaction;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    List<Transaction> findByIdentificationOrderByCreationDateDesc(String identification, Pageable limit);
    List<Transaction> findByIdentificationAndTypeOrderByCreationDateDesc(String identification, String type, Pageable limit);
       
}
