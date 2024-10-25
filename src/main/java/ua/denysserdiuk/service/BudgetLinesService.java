package ua.denysserdiuk.service;

import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BudgetLinesService {
    String addBudgetLine(Budget budget);
    List<Budget> getCurrentMonthBudgetLines(Users user);
    Map<String, Double> getMonthlyBalance(long id);
    Double getAnnualBalance(long userId, int year);
    Double getMonthlyBalance(long userId, int month, int year);
    Double getAllTimeBalance(long userId);
    List<String>getUserCategories(long userID);
    Map<String, Double> getCurrentMonthLossCategoryPercentages(Users user);
    String updateBudgetLinesForUser(long userID);

}