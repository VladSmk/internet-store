package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.internet.store.dao.BasketDAO;
@Controller
@RequestMapping("/basket")
public class BasketController {

    private final BasketDAO basketDAO;
    @Autowired
    public BasketController(BasketDAO basketDAO) {
        this.basketDAO = basketDAO;
    }


}
