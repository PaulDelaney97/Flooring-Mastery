package com.pauldelaney.flooringmastery.dao;

import com.pauldelaney.flooringmastery.dto.Order;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryOrderDaoImplTest {

    String testExportFile = "SampleFileDataTest/BackupTest/DataExportTest.txt";
    String testOrderFolder = "SampleFileDataTest/OrdersTest/";
    FlooringMasteryOrderDao testOrderDao = new FlooringMasteryOrderDaoImpl(testExportFile, testOrderFolder);

    public FlooringMasteryOrderDaoImplTest() {
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
    public void testGetOrder() throws FlooringMasteryPersistenceException {
        // Arrange
        // OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total
        // Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06

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

        // Make up order to add
        Order order2 = new Order(2);
        order2.setCustomerName("Paul Delaney");
        order2.setState("CA");
        order2.setTaxRate(new BigDecimal("25"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("249"));
        order2.setCostPerSquareFoot(new BigDecimal("3.50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.35"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));

        testOrderDao.addOrderToExistingFile(file, 2, order2);

        // Act
        Order retrievedOrder1 = testOrderDao.getOrder(file, 1);
        Order retrievedOrder2 = testOrderDao.getOrder(file, 2);

        // Assert
        assertEquals(order1, retrievedOrder1, "The orders should be the same.");
        assertEquals(order2, retrievedOrder2, "The orders should be the same.");
    }

    @Test
    public void testGetAllOrders() throws FlooringMasteryPersistenceException {

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
        String file1 = "Orders_06032013.txt";
        testOrderDao.addOrderToNewFile(file1, 1, order1);

        // Make up order to add
        Order order2 = new Order(2);
        order2.setCustomerName("Paul Delaney");
        order2.setState("CA");
        order2.setTaxRate(new BigDecimal("25"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("249"));
        order2.setCostPerSquareFoot(new BigDecimal("3.50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.35"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));

        String file2 = "Orders_06042013.txt";
        testOrderDao.addOrderToNewFile(file2, 2, order2);

        Order order3 = new Order(3);
        order3.setCustomerName("Paul Delaney");
        order3.setState("CA");
        order3.setTaxRate(new BigDecimal("25"));
        order3.setProductType("Tile");
        order3.setArea(new BigDecimal("249"));
        order3.setCostPerSquareFoot(new BigDecimal("3.50"));
        order3.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order3.setMaterialCost(new BigDecimal("871.50"));
        order3.setLaborCost(new BigDecimal("1033.35"));
        order3.setTax(new BigDecimal("476.21"));
        order3.setTotal(new BigDecimal("2381.06"));
        // Act
        List<Order> allOrders = testOrderDao.getAllOrders();

        // Assert
        assertTrue(allOrders.contains(order1), "Order1 should be in the list");
        assertTrue(allOrders.contains(order2), "Order2 should be in the list");
        assertFalse(allOrders.contains(order3), "Order3 should not be in the list");
    }

    @Test
    public void testGetAllOrdersForDate() throws FlooringMasteryPersistenceException {

        // Arrange
        // OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total
        // Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06
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

        // Make up order to add
        Order order2 = new Order(2);
        order2.setCustomerName("Paul Delaney");
        order2.setState("CA");
        order2.setTaxRate(new BigDecimal("25"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("249"));
        order2.setCostPerSquareFoot(new BigDecimal("3.50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.35"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));

        testOrderDao.addOrderToExistingFile(file, 2, order2);

        // Act
        List<Order> orders = testOrderDao.getAllOrdersForDate(file);

        // Assert
        assertTrue(orders.contains(order1), "Order1 should be in the list");
        assertTrue(orders.contains(order2), " Order2 should be in the list");

    }

    @Test
    public void testGetAllOrderNumbersForDate() throws FlooringMasteryPersistenceException {
        // Arrange
        // OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total
        // Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06
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

        // Make up order to add
        Order order2 = new Order(2);
        order2.setCustomerName("Paul Delaney");
        order2.setState("CA");
        order2.setTaxRate(new BigDecimal("25"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("249"));
        order2.setCostPerSquareFoot(new BigDecimal("3.50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.35"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));

        testOrderDao.addOrderToExistingFile(file, 2, order2);

        // Act
        List<Integer> orderNumbers = testOrderDao.getAllOrderNumbersForDate(file);

        // Assert
        assertTrue(orderNumbers.contains(1), "Order1 should be in the list");
        assertTrue(orderNumbers.contains(2), " Order2 should be in the list");
    }

    @Test
    public void testGetAllOrderNumbers() throws FlooringMasteryPersistenceException {

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
        String file1 = "Orders_06032013.txt";
        testOrderDao.addOrderToNewFile(file1, 1, order1);

        // Make up order to add
        Order order2 = new Order(2);
        order2.setCustomerName("Paul Delaney");
        order2.setState("CA");
        order2.setTaxRate(new BigDecimal("25"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("249"));
        order2.setCostPerSquareFoot(new BigDecimal("3.50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.35"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));

        String file2 = "Orders_06042013.txt";
        testOrderDao.addOrderToNewFile(file2, 2, order2);

        Order order3 = new Order(3);
        order3.setCustomerName("Paul Delaney");
        order3.setState("CA");
        order3.setTaxRate(new BigDecimal("25"));
        order3.setProductType("Tile");
        order3.setArea(new BigDecimal("249"));
        order3.setCostPerSquareFoot(new BigDecimal("3.50"));
        order3.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order3.setMaterialCost(new BigDecimal("871.50"));
        order3.setLaborCost(new BigDecimal("1033.35"));
        order3.setTax(new BigDecimal("476.21"));
        order3.setTotal(new BigDecimal("2381.06"));
        // Act
        List<Integer> allOrders = testOrderDao.getAllOrderNumbers();

        // Assert
        assertTrue(allOrders.contains(1), "Order1 should be in the list");
        assertTrue(allOrders.contains(2), "Order2 should be in the list");
        assertFalse(allOrders.contains(3), "Order3 should not be in the list");
    }

    @Test
    public void testEditOrder() throws FlooringMasteryPersistenceException {

        // Create and add order to file
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
        String file1 = "Orders_06032013.txt";
        testOrderDao.addOrderToNewFile(file1, 1, order1);

        //edit the order
        order1.setCustomerName("Paul Delaney");
        order1.setState("KY");
        order1.setProductType("Laminate");

        Order editedOrder = testOrderDao.editOrder(file1, order1);

        // Assert
        assertEquals(editedOrder.getCustomerName(), "Paul Delaney", "Name should be Paul Delaney");
        assertNotEquals(editedOrder.getCustomerName(), "Ada Lovelace", "Name should not be Ada Lovelace");
    }

    @Test
    public void testRemoveOrder() throws FlooringMasteryPersistenceException {
        // Create and add order to file
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
        String file1 = "Orders_06032013.txt";
        testOrderDao.addOrderToNewFile(file1, 1, order1);

        // Make up order to add
        Order order2 = new Order(2);
        order2.setCustomerName("Paul Delaney");
        order2.setState("CA");
        order2.setTaxRate(new BigDecimal("25"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("249"));
        order2.setCostPerSquareFoot(new BigDecimal("3.50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.35"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));

        // Add to file
        testOrderDao.addOrderToExistingFile(file1, 2, order2);
        List<Order> orderList = testOrderDao.getAllOrders();

        // remove file
        testOrderDao.removeOrder(file1, 1);
        List<Order> orderListRemoved = testOrderDao.getAllOrders();

        assertNotEquals(orderList, orderListRemoved, "The lists should not be equal");
        assertEquals(orderList.size(), 2, "orderList should have two orders");
        assertEquals(orderListRemoved.size(), 1, "orderListRemoved should have 1 order");

        assertTrue(orderListRemoved.contains(order2), " List should contain order 2");
        assertFalse(orderListRemoved.contains(order1), " List shouldn't contain order 1");
    }

    @Test
    public void testGetExportData() throws FlooringMasteryPersistenceException {
        // Create and add order to file
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
        String file1 = "Orders_06032013.txt";
        testOrderDao.addOrderToNewFile(file1, 1, order1);

        // Make up order to add
        Order order2 = new Order(2);
        order2.setCustomerName("Paul Delaney");
        order2.setState("CA");
        order2.setTaxRate(new BigDecimal("25"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("249"));
        order2.setCostPerSquareFoot(new BigDecimal("3.50"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order2.setMaterialCost(new BigDecimal("871.50"));
        order2.setLaborCost(new BigDecimal("1033.35"));
        order2.setTax(new BigDecimal("476.21"));
        order2.setTotal(new BigDecimal("2381.06"));

        String file2 = "Orders_06042013.txt";
        testOrderDao.addOrderToNewFile(file2, 2, order2);

        // write export data
        testOrderDao.exportAllData();

        // export data to test backup folder
        Map<String, List<Order>> exportData = testOrderDao.getExportData();

        assertTrue(exportData.containsKey("06-03-2013"));
        assertTrue(exportData.containsKey("06-04-2013"));

        // Delete export file
        File exportFile = new File(testExportFile);
        exportFile.delete();
    }
}
