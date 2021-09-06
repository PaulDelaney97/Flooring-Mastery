package com.pauldelaney.flooringmastery.dao;

import com.pauldelaney.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryProductDaoImplTest {

    String testProductFile = "SampleFileDataTest/DataTest/ProductsTest.txt";
    FlooringMasteryProductDao testProductDao = new FlooringMasteryProductDaoImpl(testProductFile);

    public FlooringMasteryProductDaoImplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetAllProducts() throws FlooringMasteryPersistenceException {
        // Arrange
        Product carpet = new Product("Carpet");
        carpet.setCostPerSquareFoot(new BigDecimal("2.25"));
        carpet.setLaborCostPerSquareFoot(new BigDecimal("2.10"));

        Product laminate = new Product("Laminate");
        laminate.setCostPerSquareFoot(new BigDecimal("1.75"));
        laminate.setLaborCostPerSquareFoot(new BigDecimal("2.10"));

        Product tile = new Product("Tile");
        tile.setCostPerSquareFoot(new BigDecimal("3.50"));
        tile.setLaborCostPerSquareFoot(new BigDecimal("4.15"));

        Product wood = new Product("Wood");
        wood.setCostPerSquareFoot(new BigDecimal("5.15"));
        wood.setLaborCostPerSquareFoot(new BigDecimal("4.75"));

        // ACT
        List<Product> allProducts = testProductDao.getAllProducts();

        // ASSERT
        assertTrue(allProducts.contains(carpet) && allProducts.contains(laminate) && allProducts.contains(tile) && allProducts.contains(wood), "The "
                + "The list should contain carpet, laminate, tile and wood");
        assertEquals(allProducts.size(), 4, "The all products list should contain 4 products");
    }

    /**
     * Test of getProduct method, of class FlooringMasteryProductDaoImpl.
     */
    @Test
    public void testGetProduct() throws FlooringMasteryPersistenceException {
        // Arrange
        Product tile = new Product("Tile");
        tile.setCostPerSquareFoot(new BigDecimal("3.50"));
        tile.setLaborCostPerSquareFoot(new BigDecimal("4.15"));

        // Act
        Product retrievedProduct = testProductDao.getProduct("Tile");

        // Assert
        assertEquals(retrievedProduct, tile, "The products should be the same.");
    }

}
