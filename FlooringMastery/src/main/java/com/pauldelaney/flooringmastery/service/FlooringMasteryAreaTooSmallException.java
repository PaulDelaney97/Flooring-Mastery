package com.pauldelaney.flooringmastery.service;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryAreaTooSmallException extends Exception {
    // This class inherits all the properties of the Exception class

    public FlooringMasteryAreaTooSmallException(String message) {
        super(message);
    }

    public FlooringMasteryAreaTooSmallException(String message, Throwable cause) {
        super(message, cause);
    }

}
