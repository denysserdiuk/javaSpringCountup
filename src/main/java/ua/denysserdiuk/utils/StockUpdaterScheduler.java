package ua.denysserdiuk.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.denysserdiuk.repository.SharesRepository;
import ua.denysserdiuk.service.StockPriceService;

import java.util.List;


@Component
public class StockUpdaterScheduler {

    private final SharesRepository sharesRepository;
    private final StockPriceService stockPriceService;

    @Autowired
    public StockUpdaterScheduler(SharesRepository sharesRepository, StockPriceService stockPriceService) {
        this.sharesRepository = sharesRepository;
        this.stockPriceService = stockPriceService;
    }

    // Scheduled this job to run at 4:30 PM eastern time (New York), Monday - Friday
    @Scheduled(cron = "0 30 16 ? * MON-FRI", zone = "America/New_York")
    public void fetchStockPrices() {
        updateStockPrices();
    }

    //find and update all stock prices
    private void updateStockPrices() {
        List<String> tickers = sharesRepository.findDistinctTickers();

        for (String ticker : tickers) {
            stockPriceService.updateStockPrice(ticker);
        }
    }
}
