package ua.denysserdiuk.service;

import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;

import java.util.List;

public interface AddBudgetLinesService {
    String addBudgetLine(Budget budget);
    List<Budget> getCurrentMonthBudgetLines(Users user);
}
