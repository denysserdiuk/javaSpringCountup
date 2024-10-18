package ua.denysserdiuk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.repository.BudgetRepository;

@Service
public class AddBudgetLinesServiceImpl implements AddBudgetLinesService {
    private final BudgetRepository budgetRepository;

    @Autowired
    public AddBudgetLinesServiceImpl(BudgetRepository budgetRepository) {
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
        budgetRepository.save(budget1);

        return "budget line " + budget.getDescription() + "added";
    }
}
