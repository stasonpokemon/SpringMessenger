package com.trebnikau.messenger.controller;

import com.trebnikau.messenger.entity.User;
import com.trebnikau.messenger.entity.dto.CaptchaResponseDto;
import com.trebnikau.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirmation,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {
        // заполняем captcha url параметрами
        String url = String.format(CAPTCHA_URL, recaptchaSecret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirmation);
        if (StringUtils.isEmpty(passwordConfirmation)) {
            model.addAttribute("password2Error", "Password confirmation can't be empty");
        }

        // Проверка на сходство двух введённых паролей
        boolean isDifferentPasswords = false;
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)) {
            model.addAttribute("passwordError", "Passwords are different!");
            isDifferentPasswords = true;
        }


        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
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
    public String activate(@AuthenticationPrincipal User user,
                           Model model,
                           @PathVariable("code") String code) {
        System.out.println("123");
        if (user != null) {
            if (user.isActive()) {
                return "main";
            }
        }
        boolean isActivate = userService.activateUser(code);
        if (isActivate) {
            model.addAttribute("messageType", "success");
            model.addAttribute("messageForLogin", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("messageForLogin", "Activation code is not found");
        }
        return "login";
    }
}
