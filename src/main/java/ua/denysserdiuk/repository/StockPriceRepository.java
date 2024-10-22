package ua.denysserdiuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.denysserdiuk.model.StockPrice;

import java.util.List;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    StockPrice findByTicker(String ticker);
}
