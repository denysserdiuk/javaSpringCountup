package ua.denysserdiuk.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.AddBudgetLinesService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.time.LocalDate;

@Controller
public class LandingController {
    private final UserRepository userRepository;
    private final AddBudgetLinesService addBudgetLinesService;

    public LandingController(UserRepository userRepository, AddBudgetLinesService addBudgetLinesService) {
        this.userRepository = userRepository;
        this.addBudgetLinesService = addBudgetLinesService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/home")
    public String home(Model model) {

        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);
        model.addAttribute("username", user.getUsername());

        double annualBalance = addBudgetLinesService.getAnnualBalance(user.getId(), LocalDate.now().getYear());
        model.addAttribute("annualBalance", annualBalance);

        double monthlyBalance = addBudgetLinesService.getMonthlyBalance(user.getId(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear());
        model.addAttribute("monthlyBalance", monthlyBalance);

        return "home";
    }
}
