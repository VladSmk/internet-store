package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.internet.store.dao.UserDAO;
import ua.internet.store.model.Users;

@Controller
@RequestMapping("/internetStore")
public class UserController {

    private final UserDAO userDAO;

    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping()
    public String showFirstPage(){
        return "users/first-page";
    }
    @GetMapping("/signIn")
    public String signInUser(){
        return "users/user-sign-in";
    }
    @GetMapping("/signUp")
    public String signUpUser(Model model){
        model.addAttribute("account", new Users());
        return "users/user-sign-up";
    }

    @PostMapping()
    public String saveNewAccount(@ModelAttribute("account") Users user){
        userDAO.saveAccountInDb(user);
        return "redirect:/internetStore";
    }

}
