package ua.denysserdiuk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.service.UserCreationService;

@Controller
public class UserWebController {

    private final UserCreationService userService;

    @Autowired
    public UserWebController(UserCreationService userService) {
        this.userService = userService;
    }

    @PostMapping("/CreateUser")
    public String createUser(Users users, Model model) {
        userService.createUser(users);
        model.addAttribute("message", "User created successfully!");
        return "redirect:/login";
    }
}
