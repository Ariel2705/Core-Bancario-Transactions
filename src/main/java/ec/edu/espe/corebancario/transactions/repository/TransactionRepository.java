package ec.edu.espe.corebancario.transactions.repository;

import ec.edu.espe.corebancario.transactions.model.Transaction;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByAccountOrderByCreationDateDesc(String account, Pageable limit);

    List<Transaction> findByAccountAndTypeOrderByCreationDateDesc(String account, String type, Pageable limit);

}
