package ua.denysserdiuk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.BudgetService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class BudgetRestController {

    @Autowired
    private BudgetService budgetService;
    private final UserRepository userRepository;

    public BudgetRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/userBalance")
    public Map<String, Double> getUserMonthlyBalance() {
        String username = SecurityUtils.getAuthenticatedUsername();
        Users user = userRepository.findByUsername(username);
        return budgetService.getMonthlyBalance(user.getId());
    }
}