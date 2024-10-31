package denysserdiuk.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import denysserdiuk.model.Budget;
import denysserdiuk.model.Users;
import denysserdiuk.repository.UserRepository;
import denysserdiuk.service.BudgetLinesService;
import denysserdiuk.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class BudgetWebController {
    private final BudgetLinesService budgetLinesService;
    private final UserRepository userRepository;


    public BudgetWebController(BudgetLinesService budgetLinesService, UserRepository userRepository) {
        this.budgetLinesService = budgetLinesService;
        this.userRepository = userRepository;
    }

    @PostMapping("/AddBudgetItem")
    public String addBudgetLineExpanse(Budget budget, Model model) {
        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);
        budget.setUser(user);

        if (budget.getDate() == null) {
            budget.setDate(LocalDate.now());
        }

        budgetLinesService.addBudgetLine(budget);

        model.addAttribute("message", "Budget Line added!");

        return "redirect:/home";
    }

    @GetMapping("/currentMonthBudgets")
    @ResponseBody
    public List<Budget> getCurrentMonthBudgets() {
        String username = SecurityUtils.getAuthenticatedUsername();
        Users user = userRepository.findByUsername(username);
        return budgetLinesService.getCurrentMonthBudgetLines(user, LocalDate.now().getMonthValue(), LocalDate.now().getYear());
    }
}
