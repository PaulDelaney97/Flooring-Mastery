package com.pauldelaney.flooringmastery.dao;

import com.pauldelaney.flooringmastery.dto.Order;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pauldelaney
 */
public interface FlooringMasteryOrderDao {

    /**
     *
     * @return List of all Orders from all Order files
     * @throws FlooringMasteryPersistenceException
     */
    List<Order> getAllOrders() throws FlooringMasteryPersistenceException;

    /**
     *
     * @param orderFile
     * @return List of all orders numbers from one particular order file
     * @throws FlooringMasteryPersistenceException
     */
    List<Integer> getAllOrderNumbersForDate(String orderFile) throws FlooringMasteryPersistenceException;

    /**
     *
     * @return List of all order numbers from all files
     * @throws FlooringMasteryPersistenceException
     */
    List<Integer> getAllOrderNumbers() throws FlooringMasteryPersistenceException;

    /**
     *
     * @return Array of the names of all order files
     */
    String[] getAllOrderFiles();

    /**
     * This method takes an order file and returns a list of orders on that
     * file. Each file corresponds to a particular date so this returns all
     * orders for a date.
     *
     * @param orderFile
     * @return List of all orders on a given file
     * @throws FlooringMasteryPersistenceException
     */
    List<Order> getAllOrdersForDate(String orderFile) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param orderFile
     * @param orderNumber
     * @return Order object corresponding to given file and order number.
     * @throws FlooringMasteryPersistenceException
     */
    Order getOrder(String orderFile, int orderNumber) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param orderFile
     * @param orderNumber
     * @param order
     * @return Order object which has been added to given order file
     * @throws FlooringMasteryPersistenceException
     */
    Order addOrderToExistingFile(String orderFile, int orderNumber, Order order) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param newFile
     * @param orderNumber
     * @param order
     * @return Order object which has been added to order file
     * @throws FlooringMasteryPersistenceException
     */
    Order addOrderToNewFile(String newFile, int orderNumber, Order order) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param orderFile
     * @param updatedOrder
     * @return Order object which has bee updated
     * @throws FlooringMasteryPersistenceException
     */
    Order editOrder(String orderFile, Order updatedOrder) throws FlooringMasteryPersistenceException;

    /**
     *
     * @param orderFile
     * @param orderNum
     * @return Order object which has been removed from order file
     * @throws FlooringMasteryPersistenceException
     */
    Order removeOrder(String orderFile, int orderNum) throws FlooringMasteryPersistenceException;

    /**
     * This method exports all Order data to backup file.
     *
     * @throws FlooringMasteryPersistenceException
     */
    void exportAllData() throws FlooringMasteryPersistenceException;

    /**
     * This method gets all the order data and returns a list where the key is
     * the Date of the orders and the value is a list of Orders for that date
     *
     * @returnreturns a list where the key is the date of the orders and the
     * value is a list of Orders for that date.
     * @throws FlooringMasteryPersistenceException
     */
    Map<String, List<Order>> getExportData() throws FlooringMasteryPersistenceException;

}
