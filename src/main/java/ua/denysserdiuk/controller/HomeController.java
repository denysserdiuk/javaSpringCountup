package ua.denysserdiuk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.AddBudgetLinesService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    private final AddBudgetLinesService addBudgetLinesService;

    public HomeController(UserRepository userRepository, AddBudgetLinesService addBudgetLinesService) {
        this.userRepository = userRepository;
        this.addBudgetLinesService = addBudgetLinesService;
    }

    @GetMapping("/home-sidebar")

    public String homeSidebar(Model model) {
        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);
        model.addAttribute("username", user.getUsername());

        return "home-sidebar";
    }

    @GetMapping("/home")
    public String home(Model model) {

        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);
        model.addAttribute("username", user.getUsername());

        double annualBalance = addBudgetLinesService.getAnnualBalance(user.getId(), LocalDate.now().getYear());
        model.addAttribute("annualBalance", annualBalance);

        List<String> categories = addBudgetLinesService.getUserCategories(user.getId());
        model.addAttribute("categories", categories);

        double monthlyBalance = addBudgetLinesService.getMonthlyBalance(user.getId(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear());
        model.addAttribute("monthlyBalance", monthlyBalance);

        return "home";
    }
}
