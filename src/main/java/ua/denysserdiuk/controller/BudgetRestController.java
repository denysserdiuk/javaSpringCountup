package ua.denysserdiuk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.BudgetLinesService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BudgetRestController {

    private final BudgetLinesService budgetLinesService;
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

    @GetMapping("/GetCustomMonthYearBudget")
    @ResponseBody
    public ResponseEntity<List<Budget>> customBudgets(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {
        String username = SecurityUtils.getAuthenticatedUsername();
        Users user = userRepository.findByUsername(username);

        List<Budget> budgetLines = budgetLinesService.getCurrentMonthBudgetLines(user, month, year);

        return new ResponseEntity<>(budgetLines, HttpStatus.OK);
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

    @PostMapping("/updateBudgetItem")
    @ResponseBody
    public ResponseEntity<String> updateBudgetItem(@RequestParam Map<String, String> params) {
        String username = SecurityUtils.getAuthenticatedUsername();
        Users user = userRepository.findByUsername(username);

        Long id = Long.parseLong(params.get("id"));
        Optional<Budget> budgetOptional = budgetLinesService.findByIdAndUser(id, user);

        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();
            budget.setDescription(params.get("description"));
            budget.setAmount(Double.parseDouble(params.get("amount")));
            budget.setDate(LocalDate.parse(params.get("date")));
            budgetLinesService.saveBudgetLine(budget);
            return new ResponseEntity<>("Budget item updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Budget item not found or unauthorized", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/deleteBudgetItem/{id}")
    public ResponseEntity<String> deleteBudgetItem(@PathVariable("id") Long id) {
        String username = SecurityUtils.getAuthenticatedUsername();
        Users user = userRepository.findByUsername(username);
        System.out.println("***** updating budget item.");
        Optional<Budget> budgetOptional = budgetLinesService.findByIdAndUser(id, user);

        if (budgetOptional.isPresent()) {
            budgetLinesService.deleteBudgetLine(budgetOptional.get());
            return ResponseEntity.ok("Budget item deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Budget item not found or unauthorized");
        }
    }
}