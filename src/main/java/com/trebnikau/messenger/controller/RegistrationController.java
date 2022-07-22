package com.trebnikau.messenger.controller;

import com.trebnikau.messenger.entity.User;
import com.trebnikau.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
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
    public String addUser(
            @RequestParam("password2") String passwordConfirmation,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirmation);
        if (StringUtils.isEmpty(passwordConfirmation)){
            model.addAttribute("password2Error","Password confirmation can't be empty");
        }

        // Проверка на сходство двух введённых паролей
        boolean isDifferentPasswords = false;
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)) {
            model.addAttribute("passwordError", "Passwords are different!");
            isDifferentPasswords = true;
        }


        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errors = UtilsController.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        } else {
            if (isDifferentPasswords) {
                return "registration";
            } else if (userService.addUser(user, user.getEmail()) == -1) {
                model.addAttribute("usernameError", "User with entered username exist");
                return "registration";
            } else if (userService.addUser(user, user.getEmail()) == 0) {
                model.addAttribute("emailError", "Entered email is busy");
                return "registration";
            } else {
                return "redirect:/login";
            }
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable("code") String code) {
        System.out.println("123");
        boolean isActivate = userService.activateUser(code);
        if (isActivate) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
