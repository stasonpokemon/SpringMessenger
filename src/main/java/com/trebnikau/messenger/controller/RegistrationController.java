package com.trebnikau.messenger.controller;

import com.trebnikau.messenger.entity.User;
import com.trebnikau.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        if (userService.addUser(user, user.getEmail()) == -1) {
            model.put("message", "User with entered username exist");
            return "registration";
        } else if (userService.addUser(user, user.getEmail()) == 0) {
            model.put("message", "Entered email is busy");
            return "registration";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable("code") String code) {
        boolean isActivate = userService.activateUser(code);
        if (isActivate) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
