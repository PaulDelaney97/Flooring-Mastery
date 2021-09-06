package com.pauldelaney.flooringmastery.dao;

import com.pauldelaney.flooringmastery.dto.Tax;
import java.util.List;

/**
 *
 * @author pauldelaney
 */
public interface FlooringMasteryTaxDao {

    /**
     *
     * @return List<Tax> List of all Tax objects in the tax file
     * @throws FlooringMasteryPersistenceException
     */
    List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException;

    /**
     * This method returns the Tax object corresponding to a given State.
     *
     * @param stateAbbr State abbreviation
     * @return Tax object for state abbreviation
     * @throws FlooringMasteryPersistenceException
     */
    Tax getTax(String stateAbbr) throws FlooringMasteryPersistenceException;
}
