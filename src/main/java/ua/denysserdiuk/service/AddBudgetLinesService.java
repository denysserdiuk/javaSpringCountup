package ua.denysserdiuk.service;

import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;

import java.util.List;
import java.util.Map;

public interface AddBudgetLinesService {
    String addBudgetLine(Budget budget);
    List<Budget> getCurrentMonthBudgetLines(Users user);
    Map<String, Double> getMonthlyBalance(long id);
    Double getAnnualBalance(long userId, int year);
    Double getMonthlyBalance(long userId, int month, int year);
}
