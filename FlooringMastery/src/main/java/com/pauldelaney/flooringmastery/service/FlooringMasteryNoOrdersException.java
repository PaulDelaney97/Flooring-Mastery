package com.pauldelaney.flooringmastery.service;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryNoOrdersException extends Exception {

    public FlooringMasteryNoOrdersException(String message) {
        super(message);
    }

    public FlooringMasteryNoOrdersException(String message, Throwable cause) {
        super(message, cause);
    }

}
