/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.api;

import ec.edu.espe.corebancario.transactions.exception.InsertException;
import ec.edu.espe.corebancario.transactions.service.TransactionService;
import ec.edu.espe.corebancario.transactions.model.Transaction;
import ec.edu.espe.corebancario.transactions.exception.DocumentNotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge=3600)
@RestController

@RequestMapping("/api/corebancario/transaction")
@Slf4j

public class TransactionController {
  
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }   
    
    @ApiOperation(value = "Lista x transacciones", notes = "Se lista las x últimas transaciones del cliente.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Correcto listado de transacciones."),
        @ApiResponse(code = 404, message = "Transacciones no encontradas.")
    })
    @GetMapping(path = "/listXLastTransactions")
    public ResponseEntity listXLastTransactions(@RequestParam String identification,@RequestParam(defaultValue = "1", required=false) Integer limit) {
        try {
            return ResponseEntity.ok(this.service.listXLastTransactions(identification, limit));
        } catch (DocumentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @ApiOperation(value = "Lista x transacciones por tipo.", notes = "Se lista las x últimas transaciones del cliente por tipo.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Correcto listado de transacciones."),
        @ApiResponse(code = 404, message = "Transacciones no encontradas.")
    })
    @GetMapping(path = "/listXLastTransactionsByType")
    public ResponseEntity listXLastTransactionsByType(
            @RequestParam(defaultValue = "Retiro") String type, 
            @RequestParam String identification,
            @RequestParam(defaultValue = "1", required=false) Integer limit) 
    {
        try {
            return ResponseEntity.ok(this.service.listXLastTransactionsByType(identification, type, limit));
        } catch (DocumentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @ApiOperation(value = "Crea una transaccion", notes = "Crear una transacción. Las transacciones son: retiro, depósito y pago de servicios")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Transacción creada."),
        @ApiResponse(code = 400, message = "Error al crear la transacción.")
    })
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Transaction transaction) {
        try {
            this.service.createTrasaction(transaction);
            return ResponseEntity.ok().build();
        } catch (InsertException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @ApiOperation(value = "Crea una transaccion en relacion al pago de tarjetas de credito", notes = "Crear una transacción. Las transacciones son el pago de estado de cuentas de las tarjetas de credito.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Transacción creada."),
        @ApiResponse(code = 400, message = "Error al crear la transacción.")
    })
    @PostMapping("/cardPayment")
    public ResponseEntity cardPayment(@RequestBody Transaction transaction) {
        try {
            this.service.cardPayment(transaction);
            return ResponseEntity.ok().build();
        } catch (InsertException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
