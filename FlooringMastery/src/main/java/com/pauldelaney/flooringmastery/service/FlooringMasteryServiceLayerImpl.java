package com.pauldelaney.flooringmastery.service;

import com.pauldelaney.flooringmastery.dao.FlooringMasteryOrderDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryProductDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryTaxDao;
import com.pauldelaney.flooringmastery.dto.Order;
import com.pauldelaney.flooringmastery.dto.Product;
import com.pauldelaney.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {

    private FlooringMasteryOrderDao orderDao;
    private FlooringMasteryProductDao productDao;
    private FlooringMasteryTaxDao taxDao;

    public FlooringMasteryServiceLayerImpl(FlooringMasteryOrderDao orderDao, FlooringMasteryProductDao productDao, FlooringMasteryTaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    //=========================  Listing Orders =========================
    @Override
    public String getOrderFileNameFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String formattedDate = date.format(formatter);
        return "Orders_" + formattedDate + ".txt";
    }

    @Override
    public void checkIfOrderFileExists(String orderFileName) throws FlooringMasteryNoOrdersException {

        String[] orderFiles = orderDao.getAllOrderFiles();
        String orderFile = null;

        for (String file : orderFiles) {
            // If file exists, set the name of order file
            if (file.equals(orderFileName)) {
                orderFile = orderFileName;
                if (orderFile != null) {
                    break;
                }
            }
        }

        // If the file does not exist, throw Exception
        if (orderFile == null) {
            throw new FlooringMasteryNoOrdersException(
                    "Error: No orders exist for that date.");
        }

    }

    @Override
    public List<Order> getAllOrders(String orderFile) throws FlooringMasteryPersistenceException {
        return orderDao.getAllOrdersForDate(orderFile);
    }

    @Override
    public List<Order> getOrderList(LocalDate dateForOrderFile) throws FlooringMasteryNoOrdersException, FlooringMasteryPersistenceException {
        String orderFile = getOrderFileNameFromDate(dateForOrderFile);
        checkIfOrderFileExists(orderFile);
        return getAllOrders(orderFile);
    }

    @Override
    public LocalDate checkDateIsInFuture(LocalDate orderDate) throws FlooringMasteryInvalidDateException {
        LocalDate today = LocalDate.now();

        if (orderDate.compareTo(today) < 0) {
            throw new FlooringMasteryInvalidDateException(
                    "Date must be in the future.");
        }

        return orderDate;
    }

    @Override
    public void checkCustomerNameIsValid(String customerName) throws FlooringMasteryInvalidCustomerNameException {
        // Check if customer name is null or empty
        if (customerName.isBlank() || customerName.isEmpty()) {
            throw new FlooringMasteryInvalidCustomerNameException(
                    "Customer Name must not be left blank.");
        }
    }

    @Override
    public void checkStateTaxFile(String stateAbbr) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidStateException {
        List<Tax> taxList = taxDao.getAllTaxes();
        String state = null;
        for (Tax tax : taxList) {
            if (tax.getStateAbbr().equals(stateAbbr)) {
                state = tax.getStateAbbr();
                if (state != null) {
                    break;
                }
            }

        }

        if (state == null) {
            throw new FlooringMasteryInvalidStateException(
                    "We cannot sell to " + stateAbbr + " for tax reasons.");
        }
    }

    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        return productDao.getAllProducts();
    }

    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        return productDao.getProduct(productType);
    }

    @Override
    public void checkProductTypeProductFile(String productType) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidProductTypeException {
        List<Product> productList = productDao.getAllProducts();
        String checkProduct = null;

        for (Product product : productList) {
            if (product.getProductType().equals(productType)) {
                checkProduct = product.getProductType();
            }
        }

        if (checkProduct == null) {
            throw new FlooringMasteryInvalidProductTypeException(
                    "We do not stock " + productType + " at the moment.");
        }
    }

    @Override
    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFt) {
        return area.multiply(costPerSquareFt).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFt) {
        return area.multiply(laborCostPerSquareFt).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
        return (materialCost.add(laborCost)).multiply(taxRate.divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return materialCost.add(laborCost).add(tax).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void checkAreaIsAboveMin(BigDecimal area) throws FlooringMasteryAreaTooSmallException {
        BigDecimal minArea = new BigDecimal("100");
        if (area.compareTo(minArea) < 0) {
            throw new FlooringMasteryAreaTooSmallException(
                    "The minimum order is 100 square feet.");
        }
    }

    @Override
    public Tax getTax(String stateAbbr) throws FlooringMasteryPersistenceException {
        return taxDao.getTax(stateAbbr);
    }

    @Override
    public int getNewOrderNumber() throws FlooringMasteryPersistenceException {
        List<Integer> allOrderNumbers = orderDao.getAllOrderNumbers();
        int latestOrderNumber = Collections.max(allOrderNumbers);
        return latestOrderNumber + 1;

    }

    @Override
    public Order placeNewOrderIfRequired(String confirmation, LocalDate orderDate, int orderNumber,
            String customerName, String stateAbbr, BigDecimal taxRate, String productType, BigDecimal area,
            BigDecimal costPerSqFt, BigDecimal laborCostPerSqFt, BigDecimal materialCost, BigDecimal laborCost,
            BigDecimal tax, BigDecimal total) throws FlooringMasteryPersistenceException {

        Order newOrder;

        if (confirmation.equalsIgnoreCase("Y")) {
            // Create new order
            newOrder = new Order(orderNumber);
            newOrder.setCustomerName(customerName);
            newOrder.setState(stateAbbr);
            newOrder.setTaxRate(taxRate);
            newOrder.setProductType(productType);
            newOrder.setArea(area);
            newOrder.setCostPerSquareFoot(costPerSqFt);
            newOrder.setLaborCostPerSquareFoot(laborCostPerSqFt);
            newOrder.setMaterialCost(materialCost);
            newOrder.setLaborCost(laborCost);
            newOrder.setTax(tax);
            newOrder.setTotal(total);

            // Create new file name using the new date
            String newOrderFileName = getOrderFileNameFromDate(orderDate);

            // Check if this file already exists or not
            String[] allOrderFiles = orderDao.getAllOrderFiles();
            String fileExists = null;

            for (String file : allOrderFiles) {
                if (file.equals(newOrderFileName)) {
                    // If file exists, add order to that file
                    fileExists = newOrderFileName;
                    Order addOrder = orderDao.addOrderToExistingFile(fileExists, orderNumber, newOrder);
                    return addOrder;
                }
            }
            // If file does not already exist, add to new file.
            if (fileExists == null) {
                Order addOrder = orderDao.addOrderToNewFile(newOrderFileName, orderNumber, newOrder);
                return addOrder;
            }

        }
        return null;
    }

    @Override
    public String checkForEdit(String edit) {
        // If user entered an empty string, return null. Otherewise return the edit
        if (edit == null || edit.trim().length() == 0 || edit.isBlank() || edit.isEmpty()) {
            return null;
        } else {
            return edit;
        }

    }

    @Override
    public BigDecimal checkForBigDecimalEdit(String edit) {
        // If user enterers nothing, return null. Otherwise return the edit
        if (edit == null || edit.trim().length() == 0 || edit.isBlank() || edit.isEmpty()) {
            return null;
        } else {
            return new BigDecimal(edit);
        }
    }

    @Override
    public Order updateOrderCustomerName(String newCustomerName, Order orderToEdit) {
        if (newCustomerName == null) {
            return orderToEdit;
        } else {
            orderToEdit.setCustomerName(newCustomerName);
            return orderToEdit;
        }
    }

    @Override
    public Order updateOrderState(String newState, Order orderToEdit) {
        if (newState == null) {
            return orderToEdit;
        } else {
            orderToEdit.setState(newState);
            return orderToEdit;
        }
    }

    @Override
    public Order updateProductType(String newProductType, Order orderToEdit) {
        if (newProductType == null) {
            return orderToEdit;
        } else {
            orderToEdit.setProductType(newProductType);
            return orderToEdit;
        }
    }

    @Override
    public Order updateArea(BigDecimal newArea, Order orderToEdit) {
        if (newArea == null) {
            return orderToEdit;
        } else {
            orderToEdit.setArea(newArea);
            return orderToEdit;
        }
    }

    @Override
    public Order updateCalculations(Order editedOrder) throws FlooringMasteryPersistenceException {

        // Get the new State, Product Type and Area and use them to calculate the other fields.
        // If the values have not been changed, the existing values will be used
        BigDecimal newTaxRate = null;
        BigDecimal newCostPerSquareFoot = null;
        BigDecimal newLaborCostPerSquareFoot = null;

        // get updated tax rate
        String newStateAbbr = editedOrder.getState();
        if (newStateAbbr != null) {
            Tax newTaxObject = taxDao.getTax(newStateAbbr);
            newTaxRate = newTaxObject.getTaxRate();
        }

        // get the updated product type
        String newProductType = editedOrder.getProductType();

        if (newProductType != null) {
            Product newProduct = productDao.getProduct(newProductType);
            newCostPerSquareFoot = newProduct.getCostPerSquareFoot();
            newLaborCostPerSquareFoot = newProduct.getLaborCostPerSquareFoot();
            BigDecimal newArea = editedOrder.getArea();

            // Calculate Values
            BigDecimal newMaterialCost = this.calculateMaterialCost(newArea, newCostPerSquareFoot);
            BigDecimal newLaborCost = this.calculateLaborCost(newArea, newLaborCostPerSquareFoot);
            BigDecimal newTax = this.calculateTax(newMaterialCost, newLaborCost, newTaxRate);
            BigDecimal newTotal = this.calculateTotal(newMaterialCost, newLaborCost, newTax);

            // Update calculated values
            editedOrder.setMaterialCost(newMaterialCost);
            editedOrder.setLaborCost(newLaborCost);
            editedOrder.setTax(newTax);
            editedOrder.setTotal(newTotal);
        }

        return editedOrder;
    }

    @Override
    public Order confirmEditOrder(String yesOrNo, String orderFile, Order newOrder) throws FlooringMasteryPersistenceException {

        // If user decides to edit the order, order is changed in memory
        if (yesOrNo.equalsIgnoreCase("Y")) {
            Order editedOrder = orderDao.editOrder(orderFile, newOrder);
            return editedOrder;
        }
        return null;
    }

    @Override
    public int checkOrderNumberExists(String orderFileName, int orderNumberToCheck) throws FlooringMasteryPersistenceException, FlooringMasteryInvalidOrderNumberException {

        List<Integer> orderNumbers = orderDao.getAllOrderNumbersForDate(orderFileName);

        int orderNumberExists = 0;

        for (Integer num : orderNumbers) {
            if (orderNumberToCheck == num) {
                orderNumberExists = num;
                return orderNumberExists;
            }
        }

        if (orderNumberExists == 0) {
            throw new FlooringMasteryInvalidOrderNumberException(
                    "No orders exists for that number.");
        }
        return 0;

    }

    @Override
    public Order getOrder(String fileName, int orderNumber) throws FlooringMasteryPersistenceException {
        return orderDao.getOrder(fileName, orderNumber);
    }

    //=========================  Remove Order =========================
    @Override
    public Order removeOrderIfRequired(String confirmation, String orderFile, int orderNumber) throws FlooringMasteryPersistenceException, FlooringMasteryOrderFileDoesNotExistException {

        if (confirmation.equalsIgnoreCase("Y")) {
            return orderDao.removeOrder(orderFile, orderNumber);
        }
        return null;
    }

    //=========================  Export All Data =========================
    @Override
    public void exportAllData() throws FlooringMasteryPersistenceException {
        orderDao.exportAllData();
    }

}
