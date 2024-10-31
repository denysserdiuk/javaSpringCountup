package denysserdiuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import denysserdiuk.model.Budget;
import denysserdiuk.model.Shares;
import denysserdiuk.model.Users;

import java.util.List;

public interface SharesRepository extends JpaRepository<Shares, Long> {
    List<Shares> findByUserId(long id);
    List<Shares> findByTicker(String ticker);

    // Fetch all unique stock tickers from the shares table
    @Query("SELECT DISTINCT s.ticker FROM Shares s")
    List<String> findDistinctTickers();

}
