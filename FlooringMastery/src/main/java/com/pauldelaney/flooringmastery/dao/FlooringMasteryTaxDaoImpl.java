package com.pauldelaney.flooringmastery.dao;

import com.pauldelaney.flooringmastery.dto.Tax;
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
public class FlooringMasteryTaxDaoImpl implements FlooringMasteryTaxDao {

    //Declare
    Map<String, Tax> taxes = new HashMap<>();
    private final String DELIMITER = ",";
    private final String TAX_FILE;

    // Constructor
    public FlooringMasteryTaxDaoImpl() {
        this.TAX_FILE = "SampleFileData/Data/Taxes.txt";
    }

    // Constructor for testing
    public FlooringMasteryTaxDaoImpl(String taxFile) {
        this.TAX_FILE = taxFile;
    }

    @Override
    public List<Tax> getAllTaxes() throws FlooringMasteryPersistenceException {
        loadTaxes();
        return new ArrayList<>(taxes.values()); // List of all Tax objects
    }

    @Override
    public Tax getTax(String stateAbbr) throws FlooringMasteryPersistenceException {
        loadTaxes();
        Tax taxToGet = taxes.get(stateAbbr); // Tax object for given state
        return taxToGet;
    }

    private String marshallTax(Tax tax) {
        String taxAsText = tax.getStateAbbr() + DELIMITER;
        taxAsText += tax.getStateName() + DELIMITER;
        taxAsText += tax.getTaxRate().toString() + DELIMITER;
        return taxAsText;
    }

    private Tax unmarshallTax(String taxAsText) {

        String[] taxTokens = taxAsText.split(DELIMITER);

        String taxStateAbbr = taxTokens[0];
        Tax taxFromFile = new Tax(taxStateAbbr);

        taxFromFile.setStateName(taxTokens[1]);
        taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));

        return taxFromFile;

    }

    private void loadTaxes() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not load Tax date into memory", e);
        }

        // Read from file
        String currentLine;
        Tax currentTax;

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
            currentTax = unmarshallTax(currentLine);

            taxes.put(currentTax.getStateAbbr(), currentTax);
            i++;
        }
        scanner.close();
    }

}
