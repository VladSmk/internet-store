package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.internet.store.dao.StoreDAO;
import ua.internet.store.dao.UserDAO;
import ua.internet.store.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserDAO userDAO;
    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @GetMapping("/signIn")
    public String UserSignIn(Model model){
        model.addAttribute("testingUser", new User());
        return "internet/user-sign-in";
    }

//    @PostMapping()
//    public String postUserSignIn(@ModelAttribute("testingUser") User user){
//        if(userDAO.signInAccountWithDb(user))
//            return "redirect:/store";
//        else
//            return "redirect:/user/signIn";
//    }

    @GetMapping("/signUp")
    public String UserSignUp(Model model){
        model.addAttribute("newUser", new User());
        return "internet/user-sign-up";
    }

    @PostMapping()
    public String postUserSignUp(@ModelAttribute("newUser") User user){
        userDAO.signUpNewAccountInDb(user);
        return "redirect:/store";
    }

}
