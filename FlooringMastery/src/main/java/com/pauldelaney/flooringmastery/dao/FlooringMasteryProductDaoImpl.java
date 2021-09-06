package com.pauldelaney.flooringmastery.dao;

import com.pauldelaney.flooringmastery.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryProductDaoImpl implements FlooringMasteryProductDao {

    // Declare
    Map<String, Product> products = new HashMap<>();
    private final String DELIMITER = ",";
    private final String PRODUCT_FILE;

    // Constructor
    public FlooringMasteryProductDaoImpl() {
        this.PRODUCT_FILE = "SampleFileData/Data/Products.txt";
    }

    // Constructor for testing
    public FlooringMasteryProductDaoImpl(String productFile) {
        this.PRODUCT_FILE = productFile;
    }

    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        loadProducts();
        return new ArrayList<>(products.values());  // List of all products
    }

    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        loadProducts();
        Product productToGet = products.get(productType);   //Product object for type
        return productToGet;
    }

    private String marshallProduct(Product product) {
        String productAsText = product.getProductType();
        productAsText += product.getCostPerSquareFoot().toString();
        productAsText += product.getLaborCostPerSquareFoot().toString();
        return productAsText;
    }

    private Product unmarshallProduct(String productAsText) {

        String[] productTokens = productAsText.split(DELIMITER);

        String productType = productTokens[0];
        Product productFromFile = new Product(productType);

        productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));

        return productFromFile;

    }

    private void loadProducts() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not load product data into memory", e);
        }

        // Read from file
        String currentLine;
        Product currentProduct;

        int i = 0;

        while (scanner.hasNextLine()) {
            if (i == 0) {
                // First line in our file is headings, don't marshall this line
                String headings = scanner.nextLine();
                i++;
                continue;
            }
            // Get the next line int the file
            currentLine = scanner.nextLine();
            // Unmarshall this line into an order
            currentProduct = unmarshallProduct(currentLine);

            products.put(currentProduct.getProductType(), currentProduct);
            i++;
        }
        scanner.close();
    }

}
