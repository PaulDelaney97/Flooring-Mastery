package com.pauldelaney.flooringmastery.dao;

import com.pauldelaney.flooringmastery.dto.Product;
import java.util.List;

/**
 *
 * @author pauldelaney
 */
public interface FlooringMasteryProductDao {

    /**
     *
     * @return List of all products from all product file
     * @throws FlooringMasteryPersistenceException
     */
    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

    /**
     *
     * @param productType
     * @return Product object from
     * @throws FlooringMasteryPersistenceException
     */
    Product getProduct(String productType) throws FlooringMasteryPersistenceException;
}
