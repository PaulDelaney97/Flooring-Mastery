package com.pauldelaney.flooringmastery.dao;

import java.io.FileNotFoundException;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryPersistenceException extends Exception {

    public FlooringMasteryPersistenceException(String message) {
        super(message);
    }

    public FlooringMasteryPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
