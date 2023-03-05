package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/lookItem/{userId}/{itemId}")
    public String lookSomeItemForSomeUser(@PathVariable("userId") int useId, @PathVariable("itemId") int itemId, Model model){
        return "basket/basket-lookitem-USERID-ITEMID";
    }

    @GetMapping("/{userId}")
    public String lookSomeUserBasket(@PathVariable("userId") int userId, Model model){
        model.addAttribute("productList", basketDAO.searchAllUserItemInBasket(userId));
        model.addAttribute("idUser", userId);
        return "basket/basket-USERID";
    }


}
