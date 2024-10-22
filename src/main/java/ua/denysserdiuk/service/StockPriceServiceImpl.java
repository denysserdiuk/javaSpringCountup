package ua.denysserdiuk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import ua.denysserdiuk.model.StockPrice;
import ua.denysserdiuk.repository.StockPriceRepository;

import java.time.LocalDateTime;

@Service
public class StockPriceServiceImpl implements StockPriceService {

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    @Autowired
    private StockPriceRepository stockPriceRepository;

    @Override
    public void updateStockPrice(String ticker) {
        try {
            // Fetch the stock price for the given ticker
            StockPrice stockPrice = stockPriceRepository.findByTicker(ticker);

            // Check if the price is outdated (e.g., older than 1 hour)
            if (stockPrice != null && stockPrice.getLastUpdated().isAfter(LocalDateTime.now().minusHours(1))) {
                return; // Stock price is up-to-date, no need to call the API
            }

            // Fetch stock price from Alpha Vantage API
            String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + ticker + "&interval=5min&apikey=" + apiKey;
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject timeSeries = jsonObject.getJSONObject("Time Series (5min)");
            String latestTime = timeSeries.keys().next();
            JSONObject latestData = timeSeries.getJSONObject(latestTime);

            double price = latestData.getDouble("4. close");

            // If no stock price exists, create a new one; otherwise, update the existing entry
            if (stockPrice == null) {
                stockPrice = new StockPrice(ticker, price);
            } else {
                stockPrice.setPrice(price);
                stockPrice.setLastUpdated(LocalDateTime.now());
            }

            // Save the updated/new stock price to the database
            stockPriceRepository.save(stockPrice);

        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // Catch the duplicate entry error
            System.err.println("Duplicate value - ticker: " + ticker);
        } catch (Exception e) {
            // Catch all other exceptions
            e.printStackTrace();
        }
    }


    @Override
    public StockPrice getStockPrice(String ticker) {
        // Fetch the stock price for the given ticker from the database
        return stockPriceRepository.findByTicker(ticker);
    }
}
