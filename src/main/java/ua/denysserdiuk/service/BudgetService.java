package ua.denysserdiuk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.BudgetRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class BudgetService implements AddBudgetLinesService {
    private final BudgetRepository budgetRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public String addBudgetLine(Budget budget) {

        Budget budget1 = new Budget();
        budget1.setUser(budget.getUser());
        budget1.setDescription(budget.getDescription());
        budget1.setAmount(budget.getAmount());
        budget1.setType(budget.getType());
        budget1.setDate(budget.getDate());
        budget1.setCategory(budget.getCategory());
        budgetRepository.save(budget1);
        return "Budget line added";
    }

    public List<Budget> getCurrentMonthBudgetLines(Users user){
        return budgetRepository.findCurrentMonthProfitsByUser(user.getId());
    }

    public Map<String, Double> getMonthlyBalance(long userId){
        Map<String, Double> monthlyBalance = new LinkedHashMap<>();
        double runningBalance = 0;

        for (int month = 1; month <= 12; month++) {
            // Get current year
            int year = LocalDate.now().getYear();

            // Find total profits for the month
            Double totalProfit = budgetRepository.findTotalByUserAndTypeAndMonth(userId, "profit", month, year);
            if (totalProfit == null) totalProfit = 0.0;

            // Find total expenses for the month
            Double totalExpense = budgetRepository.findTotalByUserAndTypeAndMonth(userId, "loss", month, year);
            if (totalExpense == null) totalExpense = 0.0;

            // Calculate net balance for the month
            runningBalance += (totalProfit - totalExpense);

            // Store the balance in the map with month name as key
            String monthName = YearMonth.of(year, month).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthlyBalance.put(monthName, runningBalance);
        }

        return monthlyBalance;
    }

    public Double getAnnualBalance(long userId, int year){
        Double totalProfit = budgetRepository.findTotalByUserAndTypeAndYear(userId, "profit", year);
        Double totalExpense = budgetRepository.findTotalByUserAndTypeAndYear(userId, "loss", year);
        return (totalProfit == null ? 0 : totalProfit) - (totalExpense == null ? 0 : totalExpense);
    }

    public Double getMonthlyBalance(long userId, int month, int year) {
        Double totalProfit = budgetRepository.findTotalByUserAndTypeAndMonth(userId, "profit", month, year);
        Double totalExpense = budgetRepository.findTotalByUserAndTypeAndMonth(userId, "loss", month, year);
        return (totalProfit == null ? 0 : totalProfit) - (totalExpense == null ? 0 : totalExpense);
    }

    @Override
    public List<String> getUserCategories(long userId) {
        return budgetRepository.findCategoriesByUser(userId);
    }
}
