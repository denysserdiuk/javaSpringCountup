package ua.denysserdiuk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.denysserdiuk.model.StockPrice;
import ua.denysserdiuk.service.StockPriceService;

@RestController
@RequestMapping("/api")
public class StockPriceRestController {
    private final StockPriceService stockPriceService;

    public StockPriceRestController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    @GetMapping("/stock-price")
    public ResponseEntity<StockPrice> getStockPrice(@RequestParam String ticker) {
        StockPrice price = stockPriceService.getStockPrice(ticker);  // Get the latest stock price (single)

        if (price == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if no price is found
        }

        return new ResponseEntity<>(price, HttpStatus.OK);  // Return the stock price with 200 OK
    }
}
