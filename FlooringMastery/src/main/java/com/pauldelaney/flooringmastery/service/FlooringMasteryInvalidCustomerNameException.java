package com.pauldelaney.flooringmastery.service;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryInvalidCustomerNameException extends Exception {

    public FlooringMasteryInvalidCustomerNameException(String message) {
        super(message);
    }

    public FlooringMasteryInvalidCustomerNameException(String message, Throwable cause) {
        super(message, cause);
    }

}
