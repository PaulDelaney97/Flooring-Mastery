package com.pauldelaney.flooringmastery.service;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryInvalidStateException extends Exception {

    public FlooringMasteryInvalidStateException(String message) {
        super(message);
    }

    public FlooringMasteryInvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

}
