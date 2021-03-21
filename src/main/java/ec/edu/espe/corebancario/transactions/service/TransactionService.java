package ec.edu.espe.corebancario.transactions.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import ec.edu.espe.corebancario.transactions.enums.StateAccountEnum;
import ec.edu.espe.corebancario.transactions.enums.TypeTransactionEnum;
import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import ec.edu.espe.corebancario.transactions.exception.InsertException;
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
            if (TypeTransactionEnum.DEPOSITO.getDescription().equals(transaction.getType()) 
                    || TypeTransactionEnum.RETIRO.getDescription().equals(transaction.getType())) {
                activateAccount(transaction.getAccount(), transaction.getType());
                HttpResponse<JsonNode> request = Unirest.get("http://localhost:8082/api/corebancario/account/findAccountByNumber/{number}")
                        .routeParam("number", transaction.getAccount()).asJson();

                if (200 == request.getStatus() 
                        && StateAccountEnum.ACTIVO.getEstado().equals(
                                request.getBody().getObject().getString("status"))) {
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
                    log.error("Intento de creacion de transaccion con cuenta no existente o inactiva "
                            + transaction.getAccount());
                    throw new InsertException("Transaction", "Cuenta no existente o inactiva");
                }
                JSONObject object = new JSONObject();
                object.put("number", transaction.getAccount());
                object.put("balance", transaction.getBalanceAccount());
                HttpResponse<JsonNode> put = Unirest.put("http://localhost:8082/api/corebancario/account/updateBalance").header("Content-Type", "application/json").body(object).asJson();

            } else {
                if (TypeTransactionEnum.PAGO.getDescription().equals(transaction.getType())) {
                    String accountPay = "270000000001";
                    HttpResponse<JsonNode> request = Unirest.get("http://localhost:8082/api/corebancario/account/findAccountByNumber/{number}")
                            .routeParam("number", accountPay).asJson();
                    BigDecimal balance = request.getBody().getObject().getBigDecimal("balance");
                    balance = balance.add(transaction.getMont());
                    transaction.setBalanceAccount(balance);
                    JSONObject object = new JSONObject();
                    object.put("number", accountPay);
                    object.put("balance", balance);
                    HttpResponse<JsonNode> put = Unirest.put("http://localhost:8082/api/corebancario/account/updateBalance").header("Content-Type", "application/json").body(object).asJson();
                } else {
                    throw new InsertException("Transaction", "Intento de transaccion de tipo no existente " 
                            + transaction.toString());
                }
            }
            transaction.setCreationDate(new Date());
            log.info("Transaccion realizada con exito " + transaction.toString());
            this.transactionRepo.insert(transaction);
        } catch (Exception e) {
            throw new InsertException("Transaction", "Ocurrio un error al crear la transaccion: " 
                    + transaction.toString(), e);
        }
    }

    public void cardPayment(Transaction transaction) throws InsertException {
        try {
            HttpResponse<JsonNode> request = Unirest.get("http://localhost:8082/api/corebancario/creditCard/findCreditCard/{number}")
                    .routeParam("number", transaction.getAccount()).asJson();
            if (200 == request.getStatus() 
                    && StateAccountEnum.ACTIVO.getEstado().equals(request.getBody().getObject().getString("status"))) {
                Integer account = request.getBody().getObject().getInt("codAccount");
                HttpResponse<JsonNode> requestAccount = Unirest.get("http://localhost:8082/api/corebancario/account/findAccountById/{id}")
                        .routeParam("id", account.toString()).asJson();
                BigDecimal balance = requestAccount.getBody().getObject().getBigDecimal("balance");
                balance = balance.add(transaction.getMont());
                transaction.setBalanceAccount(balance);
                JSONObject object = new JSONObject();
                object.put("number", requestAccount.getBody().getObject().getString("number"));
                object.put("balance", transaction.getBalanceAccount());
                HttpResponse<JsonNode> put = Unirest.put("http://localhost:8082/api/corebancario/account/updateBalance").header("Content-Type", "application/json").body(object).asJson();
            } else {
                log.error("Intento de pago de tarjeta de credito no existente o inactiva " + transaction.getAccount());
                throw new InsertException("Transaction", "Tarjeta de credito no existente o inactiva");
            }
            transaction.setCreationDate(new Date());
            log.info("Transaccion realizada con exito " + transaction.toString());
            this.transactionRepo.insert(transaction);
        } catch (Exception e) {
            throw new InsertException("Transaction", "Ocurrio un error al pagar la tarjeta de credito: " 
                    + transaction.toString(), e);
        }
    }

    public List<Transaction> listLastTransactions(String account, Integer amount) throws DocumentNotFoundException {
        try {
            log.info("Listando " + amount + " transferencias de: " + account);
            List<Transaction> transactions = 
                    this.transactionRepo.findByAccountOrderByCreationDateDesc(account,
                            PageRequest.of(0, amount));
            if (!transactions.isEmpty()) {
                return transactions;
            } else {
                throw new DocumentNotFoundException("Error al listar transacciones");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al listar las " + amount + " transacciones");
        }
    }

    public List<Transaction> listLastTransactionsByType(String account, 
            String type, 
            Integer amount) throws DocumentNotFoundException {
        try {
            log.info("Listando " + amount + " transferencias de: " + account + " de tipo " + type);
            List<Transaction> transactions = 
                    this.transactionRepo.findByAccountAndTypeOrderByCreationDateDesc(account,
                            type,
                            PageRequest.of(0, amount));
            if (!transactions.isEmpty()) {
                return transactions;
            } else {
                throw new DocumentNotFoundException("Error al listar transacciones");
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al listar las " + amount + " transacciones");
        }
    }

    private void activateAccount(String account, String type) throws DocumentNotFoundException {
        try {
            HttpResponse<JsonNode> request = Unirest.get("http://localhost:8082/api/corebancario/account/findAccountByNumber/{number}")
                    .routeParam("number", account).asJson();

            if (200 == request.getStatus() 
                    && TypeTransactionEnum.DEPOSITO.getDescription().equals(type) 
                    && StateAccountEnum.INACTIVO.getEstado().equals(request.getBody().getObject().getString("status")) 
                    && (new BigDecimal("0").equals(request.getBody().getObject().getBigDecimal("balance")))) {
                JSONObject object = new JSONObject();
                object.put("number", request.getBody().getObject().getString("number"));
                object.put("state", StateAccountEnum.ACTIVO.getEstado());
                HttpResponse<JsonNode> put = Unirest.put("http://localhost:8082/api/corebancario/account/updateStatus").header("Content-Type", "application/json").body(object).asJson();
            }
        } catch (Exception e) {
            throw new DocumentNotFoundException("Error al activar cuenta " + e);
        }
    }
}
