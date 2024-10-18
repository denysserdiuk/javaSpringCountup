package ua.denysserdiuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Budget findByUser(Users user);
}
