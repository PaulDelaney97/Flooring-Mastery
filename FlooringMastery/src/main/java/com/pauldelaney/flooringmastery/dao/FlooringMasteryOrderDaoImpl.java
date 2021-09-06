package com.pauldelaney.flooringmastery.dao;

import com.pauldelaney.flooringmastery.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryOrderDaoImpl implements FlooringMasteryOrderDao {

    // Declare
    private Map<Integer, Order> orders = new HashMap<>();
    private final String DELIMITER = ",";
    private final String DATA_EXPORT_FILE;
    private final String orderFolder;

    // Constructor
    public FlooringMasteryOrderDaoImpl() {
        this.DATA_EXPORT_FILE = "SampleFileData/Backup/DataExport.txt";
        this.orderFolder = "SampleFileData/Orders/";
    }

    // Constructor for testing
    public FlooringMasteryOrderDaoImpl(String exportFile, String orderFolder) {
        this.DATA_EXPORT_FILE = exportFile;
        this.orderFolder = orderFolder;
    }

    @Override
    public List<Order> getAllOrders() throws FlooringMasteryPersistenceException {

        String[] allOrderFiles = getAllOrderFiles();

        List<Order> allOrders = new ArrayList<>();  //New list

        for (String orderFile : allOrderFiles) {
            List<Order> ordersForDate = getAllOrdersForDate(orderFile);
            ordersForDate.forEach(order -> {
                allOrders.add(order);               //Add orders to list
            });

        }
        return allOrders;   //Return List
    }

    @Override
    public List<Integer> getAllOrderNumbersForDate(String orderFile) throws FlooringMasteryPersistenceException {
        loadOrders(orderFile);
        // Get all order numbs from the file
        Set<Integer> orderNums = orders.keySet();
        // Create ArrayList of order numbers
        List<Integer> orderNumberList = new ArrayList<>(orderNums);
        return orderNumberList;
    }

    @Override
    public List<Integer> getAllOrderNumbers() throws FlooringMasteryPersistenceException {

        String[] allOrderFiles = getAllOrderFiles();

        List<Integer> allOrderNumbers = new ArrayList<>(); //New List

        for (String file : allOrderFiles) {
            List<Integer> orderNumsPerFile = getAllOrderNumbersForDate(file);
            orderNumsPerFile.forEach(orderNum -> {
                allOrderNumbers.add(orderNum);      //Add numbers to List
            });
        }
        orders.clear();
        return allOrderNumbers;     //Return List
    }

    @Override
    public String[] getAllOrderFiles() {

        // Select all files by filtering fileNames which hae "."
        // if there was a folder within this folder this would not be selected
        FilenameFilter filter = (file, fileName) -> {
            return fileName.contains(".");
        };

        String[] orderFiles = new File(orderFolder).list(filter);
        return orderFiles;
    }

    @Override
    public Order getOrder(String orderFile, int orderNumber) throws FlooringMasteryPersistenceException {
        loadOrders(orderFile);
        Order orderToGet = orders.get(orderNumber);
        orders.clear();
        return orderToGet;
    }

    @Override
    public List<Order> getAllOrdersForDate(String orderFile) throws FlooringMasteryPersistenceException {
        loadOrders(orderFile);
        List<Order> allOrdersForDate = new ArrayList(orders.values());
        this.orders.clear();
        return allOrdersForDate;
    }

    @Override
    public Order addOrderToExistingFile(String orderFile, int orderNumber, Order order) throws FlooringMasteryPersistenceException {
        loadOrders(orderFile);
        Order orderToAdd = orders.put(orderNumber, order);
        writeOrders(orderFile);
        return orderToAdd;
    }

    @Override
    public Order addOrderToNewFile(String newOrderFile, int orderNumber, Order order) throws FlooringMasteryPersistenceException {
        Order orderToAdd = orders.put(orderNumber, order);
        writeOrders(newOrderFile);
        return orderToAdd;
    }

    @Override
    public Order editOrder(String orderFile, Order updatedOrder)
            throws FlooringMasteryPersistenceException {

        loadOrders(orderFile);
        Order orderToEdit = orders.get(updatedOrder.getOrderNumber());
        orderToEdit.setCustomerName(updatedOrder.getCustomerName());
        orderToEdit.setState(updatedOrder.getState());
        orderToEdit.setTaxRate(updatedOrder.getTaxRate());
        orderToEdit.setProductType(updatedOrder.getProductType());
        orderToEdit.setArea(updatedOrder.getArea());
        orderToEdit.setCostPerSquareFoot(updatedOrder.getCostPerSquareFoot());
        orderToEdit.setLaborCostPerSquareFoot(updatedOrder.getLaborCostPerSquareFoot());
        orderToEdit.setMaterialCost(updatedOrder.getMaterialCost());
        orderToEdit.setLaborCost(updatedOrder.getLaborCost());
        orderToEdit.setTax(updatedOrder.getTax());
        orderToEdit.setTotal(updatedOrder.getTotal());
        writeOrders(orderFile);
        return orderToEdit;
    }

    @Override
    public Order removeOrder(String orderFile, int orderNum) throws FlooringMasteryPersistenceException {
        loadOrders(orderFile);
        Order orderToRemove = orders.remove(orderNum);
        writeOrders(orderFile);
        return orderToRemove;
    }

    @Override
    public void exportAllData() throws FlooringMasteryPersistenceException {
        this.writeDataExport();
    }

    private String marshallOrder(Order order) {
        String orderAsText = String.valueOf(order.getOrderNumber() + DELIMITER);
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getState() + DELIMITER;
        // Get tax rate number and convert BigDecimal to a string
        orderAsText += order.getTaxRate().toString() + DELIMITER;
        orderAsText += order.getProductType() + DELIMITER;
        // Get all BigDec and convert to string
        orderAsText += order.getArea().toString() + DELIMITER;
        orderAsText += order.getCostPerSquareFoot().toString() + DELIMITER;
        orderAsText += order.getLaborCostPerSquareFoot().toString() + DELIMITER;
        orderAsText += order.getMaterialCost().toString() + DELIMITER;
        orderAsText += order.getLaborCost().toString() + DELIMITER;
        orderAsText += order.getTax().toString() + DELIMITER;
        orderAsText += order.getTotal().toString();

        return orderAsText;
    }

    private Order unmarshallOrder(String orderAsText) {

        String[] orderTokens = orderAsText.split(DELIMITER);

        int orderNumber = Integer.valueOf(orderTokens[0]);
        Order orderFromFile = new Order(orderNumber);
        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setState(orderTokens[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));
        orderFromFile.setProductType(orderTokens[4]);
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));

        return orderFromFile;

    }

    private void loadOrders(String orderFile) throws FlooringMasteryPersistenceException {
        //Open File:
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(this.orderFolder + orderFile)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not load order data into memory", e);
        }

        //Read from file
        String currentLine; //holds the most recent line read from the file
        Order currentOrder;  //holds the most recent unmarshalled order

        //int iteration = 0;
        while (scanner.hasNextLine()) {
            //get the next line in the file
            currentLine = scanner.nextLine();
            //if the line begins with OrderNumber then this is the heading, ignore this iteration.
            if (currentLine.startsWith("OrderNumber")) {
                continue;
            }
            //unmarshall the line into an order
            currentOrder = unmarshallOrder(currentLine);

            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        //Clean up/close file
        scanner.close();
    }

    private void writeOrders(String orderFile) throws FlooringMasteryPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(this.orderFolder + orderFile));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not save order data", e);
        }
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
        String orderAsText;
        List<Order> orderList = this.getAllOrdersForDate(orderFile);
        for (Order currentOrder : orderList) {
            orderAsText = marshallOrder(currentOrder);
            out.println(orderAsText);
            out.flush();
        }
        //Clean up
        out.close();
    }

    private void writeDataExport() throws FlooringMasteryPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(DATA_EXPORT_FILE));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not save order data", e);
        }
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,Date");

        //Map <String date, ArrayList of Orders>
        Map<String, List<Order>> exportData = getExportData();

        //Collections.sort(exportData.keySet());
        //Loop through every date (key) and create a new string in the form of:
        //marshalled order + "," + date
        //print this out to the file.
        String exportDataAsText;

        for (String date : exportData.keySet()) {
            List<Order> currentOrderList = exportData.get(date);
            for (Order order : currentOrderList) {
                //marshall each order and concatenate the date onto it
                exportDataAsText = marshallOrder(order) + "," + date;
                out.println(exportDataAsText);
                out.flush();
            }
        }
        out.close();
    }

    @Override
    public Map<String, List<Order>> getExportData() throws FlooringMasteryPersistenceException {
        Map<String, List<Order>> exportDataMap = new HashMap<>();
        //Get a list of all the orders from all files
        String[] allOrderFiles = this.getAllOrderFiles();

        for (String orderFile : allOrderFiles) {
            //Get the order date from the file name
            String dateString = this.getDateFromOrderFileName(orderFile);
            //Get the order list from the file
            List<Order> allOrdersForFile = this.getAllOrdersForDate(orderFile);
            //Add to the map the date and orders
            exportDataMap.put(dateString, allOrdersForFile);
        }
        return exportDataMap;
    }

    public String getDateFromOrderFileName(String orderFile) throws FlooringMasteryPersistenceException {
        //Takes in an ordeFile name in the form: "Order_MMDDYY.txt" and converts it to 'mm-dd'yyyy'

        //Split the order file name at _ into two strings: 'Order' & 'MMDDYY.txt'
        String[] fileNameTokens = orderFile.split("_");
        //String with index [1] will have the date in the format 'MMDDYYYY.txt'
        //need to split again at the '.'
        String[] dateTokens = fileNameTokens[1].split("\\.");
        //String with index [0] will have the date in the format 'MMDDYYYY'
        //Need to convert this into mm-dd-yyyy
        String date = dateTokens[0];
        //Get the indiviual dd, mm and yyyy
        String mm = date.substring(0, 2);   //second number is up to but not including hence go up to the index + 1
        String dd = date.substring(2, 4);
        String yyyy = date.substring(4, 8);

        //combine to get format mm-dd-yyyy
        return mm + "-" + dd + "-" + yyyy;
    }
}
