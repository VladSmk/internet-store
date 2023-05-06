package ua.internet.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
public class MessageController {

    @GetMapping("/message/{myUserId}/to/{userId}")
    public String message(@PathVariable("myUserId") int myUserId,
                          @PathVariable("userId") int userId, Model model){
        

        return "message/message-USERID_ME-to-USERID";
    }

}
