package com.pauldelaney.flooringmastery.service;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryInvalidProductTypeException extends Exception {

    public FlooringMasteryInvalidProductTypeException(String message) {
        super(message);
    }

    public FlooringMasteryInvalidProductTypeException(String message, Throwable cause) {
        super(message, cause);
    }

}
