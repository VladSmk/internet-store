package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.internet.store.dao.UserDAO;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserDAO userDAO;
    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/chp/{userid}")
    public String changePassword(@PathVariable("userid") int id, Model model){
        model.addAttribute("changePassword", id);
        return "internet/user-chp-USERID";
    }
}
