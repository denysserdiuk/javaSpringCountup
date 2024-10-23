package ua.denysserdiuk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.BudgetLinesService;
import ua.denysserdiuk.service.BudgetService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BudgetRestController {


    private BudgetLinesService budgetLinesService;
    private final UserRepository userRepository;

    @Autowired
    public BudgetRestController(BudgetLinesService budgetLinesService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.budgetLinesService = budgetLinesService;
    }

    @GetMapping("/userBalance")
    public Map<String, Double> getUserMonthlyBalance() {
        String username = SecurityUtils.getAuthenticatedUsername();
        Users user = userRepository.findByUsername(username);
        return budgetLinesService.getMonthlyBalance(user.getId());
    }

    @GetMapping("/CurrentMonthLossesByCategory")
    @ResponseBody
    public Map<String, Double> getCurrentMonthLossesByCategory() {
        String username = SecurityUtils.getAuthenticatedUsername();
        Users user = userRepository.findByUsername(username);
        return budgetLinesService.getCurrentMonthLossCategoryPercentages(user);
    }

    @PostMapping("/AddBudgetItem")
    public ResponseEntity<String> addBudgetLineExpanse(@RequestBody Budget budget) {
        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);
        budget.setUser(user);

        if (budget.getDate() == null) {
            budget.setDate(LocalDate.now());
        }

        budgetLinesService.addBudgetLine(budget);


        return ResponseEntity.ok("New share saved successfully");
    }

}