package com.pauldelaney.flooringmastery.service;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryInvalidDateException extends Exception {

    public FlooringMasteryInvalidDateException(String message) {
        super(message);
    }

    public FlooringMasteryInvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }

}
