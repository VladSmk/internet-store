package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.internet.store.dao.UserDAO;
import ua.internet.store.model.User;

import javax.validation.Valid;


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

    @GetMapping("/postUserSignIn")
    public String postUserSignIn(@ModelAttribute("testingUser") User user){
        if(userDAO.signInAccountWithDb(user)) {
            int i = userDAO.searchIdByName(user);
            return "redirect:/store/" + i;
        }
        else {
            return "redirect:/user/signIn";
        }
    }

    @GetMapping("/signUp")
    public String UserSignUp(Model model){
        model.addAttribute("newUser", new User());
        return "internet/user-sign-up";
    }

    @PostMapping("/postUserSignUp")
    public String postUserSignUp(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "internet/user-sign-up";

        userDAO.signUpNewAccountInDb(user);
        return "redirect:/store";
    }

    @GetMapping("/settings/{userId}")
    public String settingsPageForSomeUser(@PathVariable("userId") int userId, Model model){
        model.addAttribute("userId", userId);
        return "internet/user-settings-USERID";
    }

    @GetMapping("/chp/{userId}")
    public String userSettingChangePassword(@PathVariable("userId") int userId, Model model){
        model.addAttribute("userId", userId);
        return "internet/user-chp-USERID";
    }

    @GetMapping("/chu/{userId}")
    public String userSettingChangeUsername(@PathVariable("userId") int userId, Model model){
        model.addAttribute("userId", userId);
        return "internet/user-chu-USERID";
    }





}
