package com.pauldelaney.flooringmastery.service;

import com.pauldelaney.flooringmastery.dao.FlooringMasteryOrderDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryOrderDaoImpl;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryProductDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryProductDaoImpl;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryTaxDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryTaxDaoImpl;
import com.pauldelaney.flooringmastery.dto.Order;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryServiceLayerImplTest {

    String testExportFile = "SampleFileDataTest/BackupTest/DataExportTest.txt";
    String testOrderFolder = "SampleFileDataTest/OrdersTest/";
    String testProductFile = "SampleFileDataTest/DataTest/ProductsTest.txt";
    String testTaxesFile = "SampleFileDataTest/DataTest/TaxesTest.txt";

    FlooringMasteryProductDao testProductDao = new FlooringMasteryProductDaoImpl(testProductFile);
    FlooringMasteryTaxDao testTaxDao = new FlooringMasteryTaxDaoImpl(testTaxesFile);
    FlooringMasteryOrderDao testOrderDao = new FlooringMasteryOrderDaoImpl(testExportFile, testOrderFolder);

    FlooringMasteryServiceLayer testService = new FlooringMasteryServiceLayerImpl(testOrderDao, testProductDao, testTaxDao);

    public FlooringMasteryServiceLayerImplTest() {
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
        //This method deletes all the files in the OrdersTest Folder after each test
        File directory = new File(testOrderFolder);
        //deletes all the files in the testOrderFolder
        File[] files = directory.listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    @Test
    public void testGetOrderFileNameFromDate() {
        LocalDate date = LocalDate.parse("2021-08-22");
        String orderFile = testService.getOrderFileNameFromDate(date);
        assertEquals(orderFile, "Orders_08222021.txt", "File name was wrong");
    }

    @Test
    public void testCheckIfOrderFileExists() throws FlooringMasteryPersistenceException {
        Order order1 = new Order(1);
        order1.setCustomerName("Ada Lovelace");
        order1.setState("CA");
        order1.setTaxRate(new BigDecimal("25"));
        order1.setProductType("Tile");
        order1.setArea(new BigDecimal("249"));
        order1.setCostPerSquareFoot(new BigDecimal("3.50"));
        order1.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order1.setMaterialCost(new BigDecimal("871.50"));
        order1.setLaborCost(new BigDecimal("1033.35"));
        order1.setTax(new BigDecimal("476.21"));
        order1.setTotal(new BigDecimal("2381.06"));

        //add order to new file
        String file = "Orders_06032013.txt";
        testOrderDao.addOrderToNewFile(file, 1, order1);

        try {
            testService.checkIfOrderFileExists(file);
        } catch (FlooringMasteryNoOrdersException e) {
            fail("Order file exists");
        }

    }

    @Test
    public void testCheckDateIsInFuture() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);

        try {
            testService.checkDateIsInFuture(tomorrow);
        } catch (FlooringMasteryInvalidDateException e) {
            fail("This date is in future, no exception should be thrown.");
        }

        try {
            testService.checkDateIsInFuture(yesterday);
            fail("This date is not in the futire, exception should be thrown.");
        } catch (FlooringMasteryInvalidDateException e) {
        }

    }

    @Test
    public void testCheckCustomerNameIsValid() {

        String customerName = "";

        try {
            testService.checkCustomerNameIsValid(customerName);
            fail("Customer's name is invalid. Exception should have been thrown.");
        } catch (FlooringMasteryInvalidCustomerNameException e) {
        }

        String customerName2 = "          ";

        try {
            testService.checkCustomerNameIsValid(customerName2);
            fail("Customer's name is invalid. Exception should have been thrown.");
        } catch (FlooringMasteryInvalidCustomerNameException e) {
        }

        String customerName3 = "Paul Delaney";

        try {
            testService.checkCustomerNameIsValid(customerName3);
        } catch (FlooringMasteryInvalidCustomerNameException e) {
            fail("Customer's name valid. Exception should not have been thrown.");
        }
    }

    @Test
    public void testCheckValidStateAgainstTaxFile() throws FlooringMasteryPersistenceException {
        String stateAbbr1 = "TX";
        String stateAbbr2 = "FAIL";
        try {
            testService.checkStateTaxFile(stateAbbr1);
        } catch (FlooringMasteryInvalidStateException e) {
            fail("The state with abbreviation TX exists. Exception should not have"
                    + "been thrown.");
        }

        try {
            testService.checkStateTaxFile(stateAbbr2);
            fail("The state with abbreviation 'FAIL' does not exist. Exception should be thrown"
                    + "been thrown.");
        } catch (FlooringMasteryInvalidStateException e) {
        }
    }

    @Test
    public void testCalculateMaterialCost() {
        BigDecimal area = new BigDecimal("5");
        BigDecimal costPerSquareFoot = new BigDecimal("10");

        BigDecimal materialCost = testService.calculateMaterialCost(area, costPerSquareFoot);

        assertEquals(materialCost, new BigDecimal("50.00"), "Material cost should be 50.00");
    }

    @Test
    public void testCalculateLaborCost() {
        BigDecimal area = new BigDecimal("5.00");
        BigDecimal laborCostPerSquareFoot = new BigDecimal("10.00");

        BigDecimal laborCost = testService.calculateMaterialCost(area, laborCostPerSquareFoot);

        assertEquals(laborCost, new BigDecimal("50.00"), "labor cost should be 50.00");
    }

    @Test
    public void testCalculateTax() {
        BigDecimal materialCost = new BigDecimal("4");
        BigDecimal laborCost = new BigDecimal("50");
        BigDecimal taxRate = new BigDecimal("7.50");

        BigDecimal tax = testService.calculateTax(materialCost, laborCost, taxRate);

        assertEquals(tax, new BigDecimal("4.05"), "Tax should be 4.05");
    }

    @Test
    public void testCalculateTotal() {
        BigDecimal materialCost = new BigDecimal("10.00");
        BigDecimal laborCost = new BigDecimal("67.50");
        BigDecimal tax = new BigDecimal("11.75");

        BigDecimal total = testService.calculateTotal(materialCost, laborCost, tax);

        assertEquals(total, new BigDecimal("89.25"), "The total should be 89.25");

    }

}
