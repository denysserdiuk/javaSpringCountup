package ua.denysserdiuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.denysserdiuk.model.Budget;
import ua.denysserdiuk.model.Users;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Budget findByUser(Users user);

    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId AND MONTH(b.date) = MONTH(CURRENT_DATE) AND YEAR(b.date) = YEAR(CURRENT_DATE)")
    List<Budget> findCurrentMonthProfitsByUser(@Param("userId") Long userId);

    @Query("SELECT SUM(b.amount) FROM Budget b WHERE b.user.id = :userId AND b.type = :type AND MONTH(b.date) = :month AND YEAR(b.date) = :year")
    Double findTotalByUserAndTypeAndMonth(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT SUM(b.amount) FROM Budget b WHERE b.user.id = :userId AND b.type = :type AND YEAR(b.date) = :year")
    Double findTotalByUserAndTypeAndYear(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("year") int year
    );
}
