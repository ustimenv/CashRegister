package shop.cashregister.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Controller responsible for all requests made by the cashier during the checkout process
 */
@RestController
@RequestMapping("/checkout")
public class CheckOutController{
    private static Logger log = LogManager.getLogger(CheckOutController.class);

    @Transactional
    @PostMapping(value="/begin", produces="application/json")
    public Object initiateTransaction(){
        return null;
    }

    @Transactional
    @PostMapping(value="/end", produces="application/json")
    public Object endTransaction(){
        return null;
    }

    @Transactional
    @PostMapping(value="/add_item", produces="application/json")
    public Object addItem(){
        return null;
    }

    @Transactional
    @PostMapping(value="/remove_item", produces="application/json")
    public Object removeItem(){
        return null;
    }
}
