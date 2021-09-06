package com.pauldelaney.flooringmastery.service;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryOrderFileDoesNotExistException extends Exception {

    public FlooringMasteryOrderFileDoesNotExistException(String message) {
        super(message);
    }

    public FlooringMasteryOrderFileDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
