/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.corebancario.transactions.exception;

public class InsertException extends Exception{
    private final String collectionName;

    public InsertException(String collectionName, String message) {
        super(message);
        this.collectionName = collectionName;
    }

    public InsertException(String collectionName, String message, Throwable cause) {
        super(message, cause);
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }     
}
