package ua.denysserdiuk.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.denysserdiuk.repository.SharesRepository;
import ua.denysserdiuk.service.StockPriceService;

import java.util.List;

@Component
public class StockUpdaterScheduler {

    @Autowired
    private SharesRepository sharesRepository;

    @Autowired
    private StockPriceService stockPriceService;

    // This will run 20 times per day (every 72 minutes)
    @Scheduled(fixedRate = 72 * 60 * 1000)
    public void fetchStockPrices() {
        updateStockPrices();
    }

    private void updateStockPrices() {
        // Fetch all unique stock tickers from the shares table
        List<String> tickers = sharesRepository.findDistinctTickers();

        for (String ticker : tickers) {
            stockPriceService.updateStockPrice(ticker);  // Update stock price in the database
        }
    }
}
