package com.harjoitus.sovellus.controller;

import com.harjoitus.sovellus.model.User;
import com.harjoitus.sovellus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());  // This will be used for both login and registration
        model.addAttribute("registerUser", new User()); // Separate ModelAttribute for registration
        return "login";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("registerUser") User user) {
        userService.registerNewUser(user);
        return "redirect:/login?registered=true"; // Redirect with a flag that shows registration was successful
    }
}
