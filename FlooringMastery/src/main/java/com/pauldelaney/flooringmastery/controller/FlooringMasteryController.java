package com.pauldelaney.flooringmastery.controller;

import com.pauldelaney.flooringmastery.dao.FlooringMasteryOrderDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryOrderDaoImpl;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryProductDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryProductDaoImpl;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryTaxDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryTaxDaoImpl;
import com.pauldelaney.flooringmastery.dto.Order;
import com.pauldelaney.flooringmastery.dto.Product;
import com.pauldelaney.flooringmastery.service.FlooringMasteryAreaTooSmallException;
import com.pauldelaney.flooringmastery.service.FlooringMasteryInvalidCustomerNameException;
import com.pauldelaney.flooringmastery.service.FlooringMasteryInvalidDateException;
import com.pauldelaney.flooringmastery.service.FlooringMasteryInvalidOrderNumberException;
import com.pauldelaney.flooringmastery.service.FlooringMasteryInvalidProductTypeException;
import com.pauldelaney.flooringmastery.service.FlooringMasteryInvalidStateException;
import com.pauldelaney.flooringmastery.service.FlooringMasteryNoOrdersException;
import com.pauldelaney.flooringmastery.service.FlooringMasteryOrderFileDoesNotExistException;
import com.pauldelaney.flooringmastery.service.FlooringMasteryServiceLayer;
import com.pauldelaney.flooringmastery.ui.FlooringMasteryView;
import com.pauldelaney.flooringmastery.ui.UserIO;
import com.pauldelaney.flooringmastery.ui.UserIOConsoleImpl;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryController {

    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer service) {
        this.view = view;
        this.service = service;
    }

    FlooringMasteryOrderDao orderDao = new FlooringMasteryOrderDaoImpl();
    FlooringMasteryProductDao productDao = new FlooringMasteryProductDaoImpl();
    FlooringMasteryTaxDao taxDao = new FlooringMasteryTaxDaoImpl();

    public void run() {

        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        listAllOrdersForDate();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportAllData();
                        break;
                    case 6:
                        exit();
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();

                }

            }

            exitMessage();

        } catch (FlooringMasteryPersistenceException
                | FlooringMasteryOrderFileDoesNotExistException
                | FlooringMasteryInvalidOrderNumberException
                | FlooringMasteryInvalidStateException
                | FlooringMasteryInvalidProductTypeException
                | FlooringMasteryAreaTooSmallException
                | FlooringMasteryInvalidCustomerNameException
                | FlooringMasteryInvalidDateException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void listAllOrdersForDate() throws FlooringMasteryPersistenceException {

        view.displayOrderListBanner();
        boolean hasErrors = false;
        List<Order> orderList = null;

        do {
            try {
                // Check orders for this date exist and get list of orders
                LocalDate inputDate = view.getOrderDate();
                orderList = service.getOrderList(inputDate);
                view.displayOrderListForDateBanner(inputDate);
                hasErrors = false;
            } catch (DateTimeException | FlooringMasteryNoOrdersException | FlooringMasteryPersistenceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        view.displayOrderList(orderList);
    }

    private LocalDate getInputDate() {
        return view.getOrderDate();
    }

    private void addOrder() throws FlooringMasteryInvalidDateException,
            FlooringMasteryPersistenceException,
            FlooringMasteryAreaTooSmallException,
            FlooringMasteryInvalidProductTypeException,
            FlooringMasteryInvalidStateException,
            FlooringMasteryInvalidCustomerNameException {

        boolean hasErrors = false;

        do {
            try {
                LocalDate orderDate = view.getNewOrderDate();
                //check date is in future
                service.checkDateIsInFuture(orderDate);

                String orderCustomerName = view.getCustomerName();
                //check name is not empty/null
                service.checkCustomerNameIsValid(orderCustomerName);

                String orderStateAbbr = view.getState();
                // check state against tax file
                service.checkStateTaxFile(orderStateAbbr);

                // Show a list of available products and get user selection
                List<Product> availableProducts = productDao.getAllProducts();
                String productSelection = view.displayAvailableProductsAndGetSelection(availableProducts);
                // check product is valid against product file
                service.checkProductTypeProductFile(productSelection);
                Product orderProductType = service.getProduct(productSelection);

                BigDecimal costPerSqFt = orderProductType.getCostPerSquareFoot();
                BigDecimal laborCostPerSqFt = orderProductType.getLaborCostPerSquareFoot();

                BigDecimal orderArea = view.getArea();
                // Check area is over 100sqft
                service.checkAreaIsAboveMin(orderArea);

                // CALCULATIONS
                BigDecimal materialCost = service.calculateMaterialCost(orderArea, costPerSqFt);
                BigDecimal laborCost = service.calculateLaborCost(orderArea, laborCostPerSqFt);

                BigDecimal taxRate = service.getTax(orderStateAbbr).getTaxRate();

                BigDecimal tax = service.calculateTax(materialCost, laborCost, taxRate);

                BigDecimal total = service.calculateTotal(materialCost, laborCost, tax);

                // Create new order number
                int orderNumber = service.getNewOrderNumber();

                String validateOrder = view.displayOrderSummaryAndGetConfirmation(orderDate,
                        orderNumber, orderCustomerName, orderStateAbbr, productSelection, total, materialCost, laborCost, tax, total);

                // write order to memory and check if order is correct
                Order orderAdded = service.placeNewOrderIfRequired(validateOrder, orderDate, orderNumber, orderCustomerName,
                        orderStateAbbr, taxRate, productSelection, orderArea, costPerSqFt,
                        laborCostPerSqFt, materialCost, laborCost, tax, total);

                if (validateOrder.equalsIgnoreCase("Y")) {
                    view.displayNewOrderSuccessBanner();
                }

                hasErrors = false;

            } catch (FlooringMasteryInvalidDateException
                    | FlooringMasteryInvalidCustomerNameException
                    | FlooringMasteryPersistenceException
                    | FlooringMasteryInvalidStateException
                    | FlooringMasteryInvalidProductTypeException
                    | FlooringMasteryAreaTooSmallException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
    }

    public void editOrder() {

        view.displayEditOrderBanner();
        boolean hasErrors = false;

        do {

            // User Inputs
            // Get date and Order number from user
            LocalDate inputDate = view.getEditOrderDate();
            int inputOrderNumber = view.getEditOrderNumber();

            try {
                // Order File
                // Check that orders exists for this date
                String orderFile = service.getOrderFileNameFromDate(inputDate);
                service.checkIfOrderFileExists(orderFile);
                // If orders exist, check that an order with the given order number exists
                int orderNum = service.checkOrderNumberExists(orderFile, inputOrderNumber);
                Order orderToEdit = service.getOrder(orderFile, orderNum);

                // Customer Name
                // Get edited customer name
                String newCustomerName = view.getEditOrderCustomerName(orderToEdit);
                newCustomerName = service.checkForEdit(newCustomerName);

                Order newOrder = service.updateOrderCustomerName(newCustomerName, orderToEdit);

                // State
                String newStateAbbr = view.getEditOrderStateAbbr(orderToEdit);
                newStateAbbr = service.checkForEdit(newStateAbbr);

                if (newStateAbbr != null) {
                    service.checkStateTaxFile(newStateAbbr);
                }

                newOrder = service.updateOrderState(newStateAbbr, orderToEdit);

                // Product Type
                List<Product> productList = service.getAllProducts();
                String newProductType = view.getEditProductType(orderToEdit, productList);

                if (newProductType != null) {
                    service.checkProductTypeProductFile(newProductType);
                }

                newOrder = service.updateProductType(newProductType, orderToEdit);

                // Area
                BigDecimal newArea = view.getEditArea(orderToEdit);
                newArea = service.checkForBigDecimalEdit(newArea.toString());

                if (newArea != null) {
                    service.checkAreaIsAboveMin(newArea);
                }

                newOrder = service.updateArea(newArea, orderToEdit);

                // Calculations
                newOrder = service.updateCalculations(newOrder);

                // Display edit summary
                String yesOrNo = view.displayEditedOrderSummaryAndGetConfirmation(inputDate, newOrder);

                service.confirmEditOrder(yesOrNo, orderFile, newOrder);

                view.displayEditSucessBanner(newOrder);

                hasErrors = false;

            } catch (FlooringMasteryNoOrdersException
                    | FlooringMasteryInvalidOrderNumberException
                    | FlooringMasteryPersistenceException
                    | FlooringMasteryInvalidStateException
                    | FlooringMasteryInvalidProductTypeException
                    | FlooringMasteryAreaTooSmallException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());

            }
        } while (hasErrors);

    }

    public void removeOrder() throws FlooringMasteryInvalidOrderNumberException, FlooringMasteryOrderFileDoesNotExistException {
        view.displayRemoveOrderBanner();
        boolean hasErrors = false;
        do {
            //Prompt user for the date & order number
            LocalDate orderDateInput = view.getRemoveOrderDate();
            int orderNumberInput = view.getRemoveOrderNumber();
            try {
                //Check that the order exists
                //First, check for the file using the orderDateInput
                String orderFile = service.getOrderFileNameFromDate(orderDateInput);
                service.checkIfOrderFileExists(orderFile);

                //If the file exists, check if the order with the specified order number exists in the file
                int orderNumToRemove = service.checkOrderNumberExists(orderFile, orderNumberInput);
                //If it doesn't exist, an exception is thrown. If it is, then remove the order.
                Order orderToRemove = service.getOrder(orderFile, orderNumToRemove);

                //Display the order information
                String confirmation = view.displayRemovedOrderAndGetConfirmation(orderDateInput, orderToRemove);

                //If they are sure, remove the order, if not return to menu. removedOrder will be null if no order to be removed.
                Order removedOrder = service.removeOrderIfRequired(confirmation, orderFile, orderNumberInput);

                view.displayRemoveSuccessBanner(removedOrder);
                hasErrors = false;
            } catch (FlooringMasteryNoOrdersException
                    | FlooringMasteryPersistenceException
                    | FlooringMasteryInvalidOrderNumberException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
    }

    private void exportAllData() throws FlooringMasteryPersistenceException {
        //Load all data from every order and export all of that data to the DataExport.txt file in the BackUp folder
        boolean hasErrors = false;
        view.displayExportAllDataBanner();
        try {
            service.exportAllData();
        } catch (FlooringMasteryPersistenceException e) {
            hasErrors = true;
            view.displayErrorMessage(e.getMessage());
        }
    }

    public void exit() {
        view.displayExit();
    }

    public void exitMessage() {
        view.displayExitMessage();
    }

    public void unknownCommand() {
        view.displayUnknownCommand();
    }

}
