package ua.internet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.internet.store.dao.UserDAO;
import ua.internet.store.model.User;
import ua.internet.store.model.UserPassword;

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
        return "user/user-sign-in";
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
        return "user/user-sign-up";
    }

    @PostMapping("/postUserSignUp/{userId}")
    public String postUserSignUp(@PathVariable("userId") int userId,
                                 @ModelAttribute("newUser") @Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "user/user-sign-up";

        userDAO.signUpNewAccountInDb(user);
        return "redirect:/store/"+userId;
    }

    @GetMapping("/settings/{userId}")
    public String settingsPageForSomeUser(@PathVariable("userId") int userId, Model model){
        model.addAttribute("chosenUser", userDAO.searchAccountById(userId));
        return "user/user-settings-USERID";
    }

    @GetMapping("/registration")
    public String userRegistration(){
        return "user/user-signIn-or-Up";
    }

    @PostMapping("/postItemInBasket/{userId}/{itemId}")
    public String postItemInBasket(@PathVariable("userId") int userId,
                                   @PathVariable("itemId") int itemId){
        userDAO.addProductToBasket(userId, itemId);
        return "redirect:/store/"+userId+"/lookItem/"+itemId;
    }

    @DeleteMapping("/deleteItemInBasket/{userId}/{itemId}")
    public String deleteItemInBasket(@PathVariable("userId") int userId,
                                   @PathVariable("itemId") int itemId){
        userDAO.deleteProductFromBasket(userId, itemId);
        return "redirect:/store/"+userId+"/lookItem/"+itemId;
    }


    @DeleteMapping("/deleteAccount/{userId}/")
    public String deleteItemInBasket(@ModelAttribute("newUser") User user,
                                     @PathVariable("userId") int userId){
        if(userDAO.searchPasswordById(userId).equals(user.getAccountPassword())){
            userDAO.deleteAccountFromDb(userId);
            return "redirect:/store";
        }
        return "redirect:/user/settings/"+userId;
    }

    @GetMapping("/deleteAccount/{userId}")
    public String deleteAccount(@PathVariable("userId") int userId, Model model){
        model.addAttribute("user", userDAO.searchAccountById(userId));
        model.addAttribute("chosenUser", userDAO.searchAccountById(userId));
        return "user/user-deleteAccount-USERID";
    }

    @DeleteMapping("/deleteAccount/{userId}")
    public String finalDeleteAccount(@ModelAttribute("user") User user,
                                     @PathVariable("userId") int userId){
        if (user.getAccountPassword().equals(userDAO.searchPasswordById(userId)))
            return "redirect:/store";
        else
            return "redirect:/user/deleteAccount"+userId;
    }



    private boolean PasswordIs = true;
    @GetMapping("/chp/{userId}")
    public String userSettingChangePassword(@PathVariable("userId") int userId, Model model){
        model.addAttribute("newPassword", new UserPassword(userId));
        model.addAttribute("oldPasswordIs", PasswordIs);
        return "user/user-chp-USERID";
    }
    @PatchMapping("/changePassword/{userId}")
    public String postUserSettingChangePassword(@PathVariable("userId") int userId,
                                                @ModelAttribute("newPassword") @Valid UserPassword userPassword){
        if (userDAO.passwordVerification(userPassword) && userPassword.getNewPassword1().equals(userPassword.getNewPassword2())) {
            userDAO.setNewPassword(userPassword);
            return "redirect:/user/settings/"+userId;
        } else {
            PasswordIs=false;
            return "redirect:/user/chp/"+userId;
        }
    }


    @GetMapping("/chu/{userId}")
    public String postSettingChangeUser(@PathVariable("userId") int userId, Model model){
        model.addAttribute("newUser", userDAO.searchAccountById(userId));
        return "user/user-chu-USERID";
    }

    @PatchMapping("/changeUser/{userId}")
    public String postUserSettingChangeUser(@PathVariable("userId") int userId,
                                            @ModelAttribute("newUser") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "user/user-chu-USERID";

        user.setAccountId(userId);
        userDAO.updateUserInDb(user);
        return "redirect:/user/settings/"+userId;
    }


}
