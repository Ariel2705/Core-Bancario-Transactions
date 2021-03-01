/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import ec.edu.espe.corebancario.transactions.enums.TypeTransactionEnum;
import ec.edu.espe.corebancario.transactions.exception.InsertException;
import ec.edu.espe.corebancario.transactions.enums.StateAccountEnum;
import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.transactions.model.Transaction;
import ec.edu.espe.corebancario.transactions.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
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
            if (TypeTransactionEnum.DEPOSITO.getDescription().equals(transaction.getType()) || TypeTransactionEnum.RETIRO.getDescription().equals(transaction.getType())) {
                HttpResponse<JsonNode> request = Unirest.get("http://localhost:8082/api/corebancario/account/findAccountByNumber/{number}")
                        .routeParam("number", transaction.getAccount()).asJson();

                if (200 == request.getStatus() && StateAccountEnum.ACTIVO.getEstado().equals(request.getBody().getObject().getString("status"))) {
                    BigDecimal balance = request.getBody().getObject().getBigDecimal("balance");
                    if (TypeTransactionEnum.DEPOSITO.getDescription().equals(transaction.getType())) {
                        balance = balance.add(transaction.getMont());
                    } else {
                        if (0 <= balance.compareTo(transaction.getMont())) {
                            balance = balance.subtract(transaction.getMont());                            
                        } else {
                            log.error("Transaccion de retiro con saldo insuficiente " + transaction.getAccount());
                            throw new InsertException("Transaction", "Cuenta no existente");
                        }
                    }
                    transaction.setBalanceAccount(balance);
                } else {
                    log.error("Intento de creacion de transaccion con cuenta no existente o inactiva " + transaction.getAccount());
                    throw new InsertException("Transaction", "Cuenta no existente");
                }
                JSONObject object = new JSONObject();
                object.put("number", transaction.getAccount());
                object.put("balance", transaction.getBalanceAccount());
                HttpResponse<JsonNode> put = Unirest.put("http://localhost:8082/api/corebancario/account/updateBalance").header("Content-Type", "application/json").body(object).asJson();
            }
            transaction.setCreationDate(new Date());
            log.info("Transaccion realizada con exito " + transaction.toString());
            this.transactionRepo.insert(transaction);
        } catch (Exception e) {
            throw new InsertException("Transaction", "Ocurrio un error al crear la transaccion: " + transaction.toString(), e);
        }
    }

    public List<Transaction> listXLastTransactions(String identificacion, Integer X) throws DocumentNotFoundException {
        try {
            log.info("Listando " + X + " transferencias de: " + identificacion);
            List<Transaction> transactions = this.transactionRepo.findByIdentificationOrderByCreationDateDesc(identificacion, PageRequest.of(0, X));
            if (!transactions.isEmpty()) {
                return transactions;
            } else {
                throw new DocumentNotFoundException("Error al listar transacciones");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al listar las " + X + " transacciones");
        }
    }

    public List<Transaction> listXLastTransactionsByType(String identificacion, String type, Integer X) throws DocumentNotFoundException {
        try {
            log.info("Listando " + X + " transferencias de: " + identificacion + " de tipo " + type);
            List<Transaction> transactions = this.transactionRepo.findByIdentificationAndTypeOrderByCreationDateDesc(identificacion, type, PageRequest.of(0, X));
            if (!transactions.isEmpty()) {
                return transactions;
            } else {
                throw new DocumentNotFoundException("Error al listar transacciones");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al listar las " + X + " transacciones");
        }
    }
}
