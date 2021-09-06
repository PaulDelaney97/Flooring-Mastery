package com.pauldelaney.flooringmastery.service;

import com.pauldelaney.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.pauldelaney.flooringmastery.dto.Order;
import com.pauldelaney.flooringmastery.dto.Product;
import com.pauldelaney.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author pauldelaney
 */
public interface FlooringMasteryServiceLayer {

    //-------------------------------------------------------------------------
    /**
     *
     * @param date
     * @return Name of order file for given date
     */
    String getOrderFileNameFromDate(LocalDate date);

    /**
     * Checks if order file exists, Throws exception if it does not exist
     *
     * @param orderFile
     * @throws FlooringMasteryNoOrdersException
     */
    void checkIfOrderFileExists(String orderFile) throws FlooringMasteryNoOrdersException;

    /**
     *
     * @param fileWithDate
     * @return List of all orders from all dates
     * @throws FlooringMasteryPersistenceException
     */
    List<Order> getAllOrders(String fileWithDate) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param OrderDate
     * @return List of orders for a particular date
     * @throws FlooringMasteryNoOrdersException
     * @throws FlooringMasteryPersistenceException
     */
    List<Order> getOrderList(LocalDate OrderDate) throws FlooringMasteryNoOrdersException, FlooringMasteryPersistenceException;

    //-------------------------------------------------------------------------
    /**
     * Checks if date is in future and throes exception if it is not.
     *
     * @param orderDate
     * @return The Date which is in future
     * @throws FlooringMasteryInvalidDateException
     */
    LocalDate checkDateIsInFuture(LocalDate orderDate) throws FlooringMasteryInvalidDateException;

    /**
     *
     * @param customerName
     * @throws FlooringMasteryInvalidCustomerNameException
     */
    void checkCustomerNameIsValid(String customerName) throws FlooringMasteryInvalidCustomerNameException;

    /**
     *
     * @param stateAbbr
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidStateException
     */
    void checkStateTaxFile(String stateAbbr) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidStateException;

    /**
     *
     * @return List of all Products
     * @throws FlooringMasteryPersistenceException
     */
    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

    /**
     *
     * @param productType
     * @return Product object for a particular date
     * @throws FlooringMasteryPersistenceException
     */
    Product getProduct(String productType) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param productType
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidProductTypeException
     */
    void checkProductTypeProductFile(String productType) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidProductTypeException;

    /**
     *
     * @param area
     * @param costPerSquareFt
     * @return Material Cost
     */
    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFt);

    /**
     *
     * @param area
     * @param laborCostPerSquareFt
     * @return labor cost
     */
    BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFt);

    /**
     *
     * @param materialCost
     * @param laborCost
     * @param taxRate
     * @return Tax
     */
    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    /**
     *
     * @param materialCost
     * @param laborCost
     * @param tax
     * @return Total cost
     */
    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);

    /**
     * Checks area is above 100sqft and throws exception if it is not
     *
     * @param area
     * @throws FlooringMasteryAreaTooSmallException
     */
    void checkAreaIsAboveMin(BigDecimal area) throws FlooringMasteryAreaTooSmallException;

    /**
     *
     * @param stateAbbr
     * @return Tax object corresponding to state abbreviation
     * @throws FlooringMasteryPersistenceException
     */
    Tax getTax(String stateAbbr) throws FlooringMasteryPersistenceException;

    /**
     *
     * @return a newly generated order number
     * @throws FlooringMasteryPersistenceException
     */
    int getNewOrderNumber() throws FlooringMasteryPersistenceException;

    /**
     *
     * @param confirmation
     * @param orderDate
     * @param orderNumber
     * @param customerName
     * @param stateAbbr
     * @param taxRate
     * @param productType
     * @param area
     * @param costPerSqFt
     * @param laborCostPerSqFt
     * @param materialCost
     * @param laborCost
     * @param tax
     * @param total
     * @return Order object
     * @throws FlooringMasteryPersistenceException
     */
    Order placeNewOrderIfRequired(String confirmation, LocalDate orderDate, int orderNumber, String customerName,
            String stateAbbr, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSqFt, BigDecimal laborCostPerSqFt,
            BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total)
            throws FlooringMasteryPersistenceException;

    //-------------------------------------------------------------------------
    /**
     * Checks that string entered is not empty or whitespace.
     *
     * @param edit
     * @return String input
     */
    String checkForEdit(String edit);

    /**
     *
     * @param edit
     * @return BigDecimal value of input
     */
    BigDecimal checkForBigDecimalEdit(String edit);

    /**
     *
     * @param newCustomerName
     * @param orderToEdit
     * @return Order Object with updated customer name
     */
    Order updateOrderCustomerName(String newCustomerName, Order orderToEdit);

    /**
     *
     * @param newState
     * @param orderToEdit
     * @return Order Object with updated State
     */
    Order updateOrderState(String newState, Order orderToEdit);

    /**
     *
     * @param newProductType
     * @param orderToEdit
     * @return Order Object with updated Product Type
     */
    Order updateProductType(String newProductType, Order orderToEdit);

    /**
     *
     * @param newArea
     * @param orderToEdit
     * @return Order object with updated Area
     */
    Order updateArea(BigDecimal newArea, Order orderToEdit);

    /**
     *
     * @param editedOrder
     * @return Order object with updated calculations
     * @throws FlooringMasteryPersistenceException
     */
    Order updateCalculations(Order editedOrder) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param toBeEdited
     * @param orderFile
     * @param editedOrder
     * @return edited order if order is to be edited
     * @throws FlooringMasteryPersistenceException
     */
    Order confirmEditOrder(String toBeEdited, String orderFile, Order editedOrder) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param orderFileName
     * @param orderNumberToCheck
     * @return order Number if it exists
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryInvalidOrderNumberException
     */
    int checkOrderNumberExists(String orderFileName, int orderNumberToCheck) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidOrderNumberException;

    /**
     *
     * @param fileName
     * @param orderNumber
     * @return Order object for order number
     * @throws FlooringMasteryPersistenceException
     */
    Order getOrder(String fileName, int orderNumber) throws FlooringMasteryPersistenceException;

    //-------------------------------------------------------------------------
    /**
     *
     * @param confirmation
     * @param orderFile
     * @param orderNumber
     * @return removed order if confirmation is yes
     * @throws FlooringMasteryPersistenceException
     * @throws FlooringMasteryOrderFileDoesNotExistException
     */
    Order removeOrderIfRequired(String confirmation, String orderFile, int orderNumber) throws FlooringMasteryPersistenceException, FlooringMasteryOrderFileDoesNotExistException;

    //-------------------------------------------------------------------------
    /**
     *
     * @throws FlooringMasteryPersistenceException
     */
    void exportAllData() throws FlooringMasteryPersistenceException;
}
