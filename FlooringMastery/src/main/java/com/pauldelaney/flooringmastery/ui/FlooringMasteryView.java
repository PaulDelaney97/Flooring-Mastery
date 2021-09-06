package com.pauldelaney.flooringmastery.ui;

import com.pauldelaney.flooringmastery.dto.Order;
import com.pauldelaney.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author pauldelaney
 */
public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("Main Menu");
        io.print("1. Display Orders");
        io.print("2. Add Order");
        io.print("3. Edit Order");
        io.print("4. Remove Order");
        io.print("5. Export All Data");
        io.print("6. Exit");

        return io.readInt("Please make your selection", 1, 6);
    }

    // ================== Display Orders Code =======================
    public void displayOrderList(List<Order> orderList) {

        String format = "%16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s  %16s   %16s";

        String fields = String.format(format,
                "Order Number",
                "Customer Name",
                "State",
                "Tax Rate",
                "Product Type",
                "Area (sqFt)",
                "Cost/SqFt ($)",
                "Labor/SqFt ($)",
                "Material Cost ($)",
                "Labor Cost ($)",
                "Tax ($)",
                "Total ($)");
        io.print(fields);

        orderList.stream().map(currentOrder -> String.format(format,
                currentOrder.getOrderNumber(),
                currentOrder.getCustomerName(),
                currentOrder.getState(),
                currentOrder.getTaxRate(),
                currentOrder.getProductType(),
                currentOrder.getArea().toString(),
                currentOrder.getCostPerSquareFoot().toString(),
                currentOrder.getLaborCostPerSquareFoot().toString(),
                currentOrder.getMaterialCost().toString(),
                currentOrder.getLaborCost().toString(),
                currentOrder.getTax().toString(),
                currentOrder.getTotal().toString())).forEachOrdered(orderInfo -> {
            io.print(orderInfo);
        });
        io.readString("Please hit enter to continue.");
    }

    public void displayOrderListBanner() {
        io.print("==== List Orders ====");
    }

    public void displayOrderListForDateBanner(LocalDate orderDate) {
        io.print("==== Orders from " + orderDate + " ====");
    }

    public LocalDate getOrderDate() {
        io.print("What date would you like to view orders from? ");
        return io.readLocalDate("YYYY-MM-DD");

    }

    // ================== Add Order Code =======================
    public void displayAddNewOrderBanner() {
        io.print("==== New Order ====");
    }

    public LocalDate getNewOrderDate() {
        io.print("What date would you like to place your order? "
                + " (You must choose a date in the future) ");
        return io.readLocalDate("YYYY-MM-DD");
    }

    public String getCustomerName() {
        return io.readString("Please enter your full name: ");
    }

    public String getState() {
        return io.readString("Please enter the state abbreviation. Example, for Washington enter 'WA': ");
    }

    public void displayProductDetails(Product product) {

        io.print(product.getProductType() + "  "
                + product.getCostPerSquareFoot().toString() + "  "
                + product.getLaborCostPerSquareFoot().toString());
    }

    public String displayAvailableProductsAndGetSelection(List<Product> productList) {

        io.print("ProductType   CostPerSquareFoot  LaborCostPerSquareFoot");
        for (Product prod : productList) {
            displayProductDetails(prod);
        }
        return io.readString("What type of flooring would you like? ");

    }

    public BigDecimal getArea() {
        return io.readBigDecimal("Please enter the area you require in square"
                + "feet. (100sqft Minimum)");
    }

    public String displayOrderSummaryAndGetConfirmation(LocalDate newDate, int orderNumber,
            String name, String stateAbbr, String productType, BigDecimal area, BigDecimal materialCost,
            BigDecimal laborCost, BigDecimal tax, BigDecimal total) {

        io.print("===== ORDER SUMMARY =====");
        io.print("Order date:         " + newDate);
        io.print("Customer Name:      " + name);
        io.print("State Abbreviation: " + stateAbbr);
        io.print("Product:            " + productType);
        io.print("Area required:      " + area);
        io.print("-------------------------------------");
        io.print("Material cost:     $" + materialCost);
        io.print("Labor cost:        $" + laborCost);
        io.print("Tax:               $" + tax);
        io.print("-------------------------------------");
        io.print("Total:             $" + total);

        return io.readString("Do you want to place the order? (Y/N). ");
    }

    public void displayNewOrderSuccessBanner() {
        io.print("=== Your Order Has Been Placed ===");
        io.readString("Press enter to continue.");
    }

    // ================== Edit Order Code =======================
    public void displayEditOrderBanner() {
        io.print("=== Edit Order ===");
    }

    public LocalDate getEditOrderDate() {
        io.print("Please enter the date of the order you would like to edit:");
        return io.readLocalDate("(YYYY-MM-DD)");
    }

    public int getEditOrderNumber() {
        return io.readInt("Please enter the order number: ");
    }

    public String getEditOrderCustomerName(Order orderToEdit) {
        return io.readString("Enter Customer Name (" + orderToEdit.getCustomerName() + "): ");
    }

    public String getEditOrderStateAbbr(Order orderToEdit) {
        return io.readString("Enter state abbreviation (" + orderToEdit.getState() + "): ");
    }

    public String getEditProductType(Order orderToEdit, List<Product> productList) {
        io.print("Enter the product type (" + orderToEdit.getProductType() + "). "
                + "Please select from the list below: ");
        return displayAvailableProductsAndGetSelection(productList);
    }

    public BigDecimal getEditArea(Order orderToEdit) {
        return io.readBigDecimal("Enter the required area in square feet (" + orderToEdit.getArea() + "): ");
    }

    public String displayEditedOrderSummaryAndGetConfirmation(LocalDate orderDateInput, Order editedOrder) {
        io.print("=== Edited Order Summary ===");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        io.print("Order date:          " + orderDateInput.format(formatter));
        io.print("Customer Name:       " + editedOrder.getCustomerName());
        io.print("State abbreviation:  " + editedOrder.getState());
        io.print("Product:             " + editedOrder.getProductType());
        io.print("Area required  :     " + editedOrder.getArea().toString() + " sqft");
        io.print("Material cost:       $" + editedOrder.getMaterialCost().toString());
        io.print("Labor cost:          $" + editedOrder.getLaborCost());
        io.print("Tax:                 $" + editedOrder.getTax().toString());
        io.print("---------------------------------");
        io.print("Total:               $" + editedOrder.getTotal().toString());

        return io.readString("Do you want to place the order? (Y/N). ");
    }

    public void displayEditSucessBanner(Order editedOrder) {
        if (editedOrder == null) {
            //do nothing, return to main menu
            io.readString("Please hit enter to continue to main menu.");
        } else {
            //If edited order is not null
            io.print("=== Order Succesfully Edited ===");
        }
    }

    // ================== Remove Order Code =======================
    public void displayRemoveOrderBanner() {
        io.print("=== Remove Order ===");
    }

    public LocalDate getRemoveOrderDate() {
        io.print("Please enter the date of the order which you would"
                + " like to remove.");
        return io.readLocalDate("YYYY-MM-DD");
    }

    public int getRemoveOrderNumber() {
        return io.readInt("Please enter the order number of the order you wish to remove: ");
    }

    public String displayRemovedOrderAndGetConfirmation(LocalDate orderDate, Order orderToRemove) {
        io.print("=== Order Summary ===");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        io.print("Order date:         " + orderDate.format(formatter));
        io.print("Customer Name:      " + orderToRemove.getCustomerName());
        io.print("State Abbreviation: " + orderToRemove.getState());
        io.print("Product:            " + orderToRemove.getProductType());
        io.print("Area required:      " + orderToRemove.getArea().toString() + " sqft");
        io.print("Material cost:      $" + orderToRemove.getMaterialCost().toString());
        io.print("Labor cost:         $" + orderToRemove.getLaborCost());
        io.print("Tax:                $" + orderToRemove.getTax().toString());
        io.print("---------------------------------");
        io.print("Total:              $" + orderToRemove.getTotal().toString());

        return io.readString("Do you want to delete the order? (Y/N). ");
    }

    public void displayRemoveSuccessBanner(Order removedOrder) {
        if (removedOrder == null) {
            //do nothing, return to main menu
            io.readString("Please hit enter to continue to main menu.");
        } else {
            //If new order is not null
            io.print("=== Order Succesfully Removed ===");
        }
    }

    // ================== Export All Data Code =======================
    public void displayExportAllDataBanner() {
        io.print("=== ExportingData ===");
    }
    // EXTRAS

    public void displayExit() {
        io.print("Exit");
    }

    public void displayUnknownCommand() {
        io.print("Unknown Command!");
    }

    public void displayExitMessage() {
        io.print("Good Bye!");
    }

    public void displayErrorMessage(String message) {
        io.print("ERROR! " + message);
    }

}
