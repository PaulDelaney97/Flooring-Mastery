package com.pauldelaney.flooringmastery;

import com.pauldelaney.flooringmastery.controller.FlooringMasteryController;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryOrderDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryOrderDaoImpl;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryProductDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryProductDaoImpl;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryTaxDao;
import com.pauldelaney.flooringmastery.dao.FlooringMasteryTaxDaoImpl;
import com.pauldelaney.flooringmastery.service.FlooringMasteryServiceLayer;
import com.pauldelaney.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.pauldelaney.flooringmastery.ui.FlooringMasteryView;
import com.pauldelaney.flooringmastery.ui.UserIO;
import com.pauldelaney.flooringmastery.ui.UserIOConsoleImpl;

/**
 *
 * @author pauldelaney
 */
public class App {

    public static void main(String[] args) {
        UserIO myIo = new UserIOConsoleImpl();
        FlooringMasteryView view = new FlooringMasteryView(myIo);

        FlooringMasteryOrderDao orderDao = new FlooringMasteryOrderDaoImpl();
        FlooringMasteryProductDao productDao = new FlooringMasteryProductDaoImpl();
        FlooringMasteryTaxDao taxDao = new FlooringMasteryTaxDaoImpl();

        FlooringMasteryServiceLayer service = new FlooringMasteryServiceLayerImpl(orderDao, productDao, taxDao);

        FlooringMasteryController controller = new FlooringMasteryController(view, service);
        controller.run();
    }

}
