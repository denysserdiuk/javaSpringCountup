package ua.denysserdiuk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.BudgetLinesService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    private final BudgetLinesService budgetLinesService;

    public HomeController(UserRepository userRepository, BudgetLinesService budgetLinesService) {
        this.userRepository = userRepository;
        this.budgetLinesService = budgetLinesService;
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

        LocalDate today = LocalDate.now();
        model.addAttribute("currentDate", today);

        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);
        model.addAttribute("username", user.getUsername());

        double annualBalance = budgetLinesService.getAnnualBalance(user.getId(), LocalDate.now().getYear());
        double lastYearBalance = budgetLinesService.getAnnualBalance(user.getId(), LocalDate.now().minusYears(1).getYear());

        double yearToYearRatio;
        if (lastYearBalance != 0) {
            yearToYearRatio = (annualBalance / lastYearBalance) * 100;
            yearToYearRatio = Math.round(yearToYearRatio * 10.0) / 10.0;
        } else {
            yearToYearRatio = 0.0;
        }

        model.addAttribute("annualBalance", annualBalance);
        model.addAttribute("yearToYear", yearToYearRatio);


        List<String> categories = budgetLinesService.getUserCategories(user.getId());
        model.addAttribute("categories", categories);

        double monthlyBalance = budgetLinesService.getMonthlyBalance(user.getId(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear());
        model.addAttribute("monthlyBalance", monthlyBalance);

        double allTimeBalance = budgetLinesService.getAllTimeBalance(user.getId());
        model.addAttribute("allTimeBalance", allTimeBalance);

        budgetLinesService.updateBudgetLinesForUser(user.getId());

        return "home";
    }
    @GetMapping("/shares")
    public String shares(){
        return "shares";
    }

    @GetMapping("/data")
    public String data(){ return "data"; }

    @GetMapping("/404NotFound")
    public String notFound(){
        return "404-not-found";
    }
}
