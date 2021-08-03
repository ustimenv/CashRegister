package shop.cashregister.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
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
    @PostMapping(value="/{username}/begin", produces="application/json")
    public Object initiateTransaction(@PathVariable(value = "username") String username){
        return null;
    }

    @Transactional
    @PostMapping(value="/{username}/end", produces="application/json")
    public Object endTransaction(@PathVariable(value = "username") String username){
        return null;
    }

    @Transactional
    @PostMapping(value="/{username}/add_item", produces="application/json")
    public Object addItem(@PathVariable(value = "username") String username){
        return null;
    }

    @Transactional
    @PostMapping(value="/{username}/remove_item", produces="application/json")
    public Object removeItem(@PathVariable(value = "username") String username){
        return null;
    }
}
