package ua.denysserdiuk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Shares;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.BudgetRepository;
import ua.denysserdiuk.repository.SharesRepository;
import ua.denysserdiuk.utils.CalculateBalanceUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class BudgetService implements BudgetLinesService {
    private final BudgetRepository budgetRepository;
    private final SharesRepository sharesRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository, SharesRepository sharesRepository) {
        this.budgetRepository = budgetRepository;
        this.sharesRepository = sharesRepository;
    }

    //Add New Budget line to DB
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

    //Get budget items that have current month date per user

    public List<Budget> getCurrentMonthBudgetLines(Users user) {
        return budgetRepository.findCurrentMonthProfitsByUser(user.getId());
    }

    //Get Yearly Running balance per user (profits - losses per each user per month)

    public Map<String, Double> getMonthlyBalance(long userId) {
        Map<String, Double> monthlyBalance = new LinkedHashMap<>();
        double runningBalance = 0;

        for (int month = 1; month <= 12; month++) {

            int year = LocalDate.now().getYear();

            Double totalProfit = budgetRepository.findTotalByUserAndTypeAndMonth(userId, "profit", month, year);
            if (totalProfit == null) totalProfit = 0.0;

            Double totalExpense = budgetRepository.findTotalByUserAndTypeAndMonth(userId, "loss", month, year);
            if (totalExpense == null) totalExpense = 0.0;

            runningBalance += (totalProfit - totalExpense);

            String monthName = YearMonth.of(year, month).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthlyBalance.put(monthName, runningBalance);
        }

        return monthlyBalance;
    }

    //Shows all the losses by categories in percentage per current month (for Donut chart).

    public Map<String, Double> getCurrentMonthLossCategoryPercentages(Users user) {
        List<Budget> currentMonthBudgets = budgetRepository.findCurrentMonthProfitsByUser(user.getId());

        Map<String, Double> categoryAmounts = new HashMap<>();
        double totalLossAmount = 0;

        for (Budget budget : currentMonthBudgets) {
            if (budget.getType().equals("loss")) {
                String category = budget.getCategory();
                double amount = budget.getAmount();

                categoryAmounts.put(category, categoryAmounts.getOrDefault(category, 0.0) + amount);

                totalLossAmount += amount;
            }
        }

        Map<String, Double> categoryPercentages = new HashMap<>();
        for (Map.Entry<String, Double> entry : categoryAmounts.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();

            double percentage = (amount / totalLossAmount) * 100;
            categoryPercentages.put(category, percentage);
        }

        return categoryPercentages;
    }


    public Double getAnnualBalance(long userId, int year) {
        Double totalProfit = budgetRepository.findTotalByUserAndTypeAndYear(userId, "profit", year);
        Double totalExpense = budgetRepository.findTotalByUserAndTypeAndYear(userId, "loss", year);
        return CalculateBalanceUtil.calculateBalanceUtil(totalProfit, totalExpense);
    }

    public Double getMonthlyBalance(long userId, int month, int year) {
        Double totalProfit = budgetRepository.findTotalByUserAndTypeAndMonth(userId, "profit", month, year);
        Double totalExpense = budgetRepository.findTotalByUserAndTypeAndMonth(userId, "loss", month, year);
        return CalculateBalanceUtil.calculateBalanceUtil(totalProfit, totalExpense);
    }

    @Override
    public Double getAllTimeBalance(long userId) {
        Double totalProfit = budgetRepository.findTotalByUserAndType(userId, "profit");
        Double totalExpense = budgetRepository.findTotalByUserAndType(userId,"loss");
        return CalculateBalanceUtil.calculateBalanceUtil(totalProfit, totalExpense);
    }

    @Override
    public List<String> getUserCategories(long userId) {
        return budgetRepository.findCategoriesByUser(userId);
    }

    //
    public String updateBudgetLinesForUser(long userId) {
        List<Shares> sharesList = sharesRepository.findByUserId(userId);

        for (Shares share : sharesList) {
            Budget budgetLine = budgetRepository.findByDescriptionAndUserId(share.getTicker(), userId);

            if (budgetLine != null) {
                double updatedProfit = share.getProfit();

                budgetLine.setAmount(Math.abs(updatedProfit));

                if (updatedProfit > 0) {
                    budgetLine.setType("profit");
                } else {
                    budgetLine.setType("loss");
                }

                budgetRepository.save(budgetLine);
            }
        }

        return "Budget lines updated for user " + userId;
    }
}
