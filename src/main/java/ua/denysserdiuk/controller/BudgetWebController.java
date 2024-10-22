package ua.denysserdiuk.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.BudgetLinesService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.List;

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
        return budgetLinesService.getCurrentMonthBudgetLines(user);
    }



}
