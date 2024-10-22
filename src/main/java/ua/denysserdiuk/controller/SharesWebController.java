package ua.denysserdiuk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ua.denysserdiuk.model.Shares;
import ua.denysserdiuk.model.StockPrice;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.repository.StockPriceRepository;
import ua.denysserdiuk.repository.UserRepository;
import ua.denysserdiuk.service.SharesService;
import ua.denysserdiuk.service.StockPriceService;
import ua.denysserdiuk.utils.SecurityUtils;

import java.util.List;

@Controller
public class SharesWebController {
    private final UserRepository userRepository;
    private final SharesService sharesService;
    private final StockPriceRepository stockPriceRepository;
    private final StockPriceService stockPriceService;

    public SharesWebController(UserRepository userRepository, SharesService sharesService, StockPriceRepository stockPriceRepository, StockPriceService stockPriceService) {
        this.userRepository = userRepository;
        this.sharesService = sharesService;
        this.stockPriceRepository = stockPriceRepository;
        this.stockPriceService = stockPriceService;
    }

    @PostMapping("/addShare")
    public String addShare(Shares share, Model model){
        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);
        share.setUser(user);

        StockPrice existingStockPrice = stockPriceRepository.findByTicker(share.getTicker());
        if (existingStockPrice == null) {
            // If the stock price doesn't exist, fetch it using the Polygon.io API
            stockPriceService.updateStockPrice(share.getTicker());
        }

        sharesService.addShare(share);
        model.addAttribute("message", "New share saved");

        return "shares";
    }
    @GetMapping("/user-shares")
    public ResponseEntity<List<Shares>> getUserShares() {
        String username = SecurityUtils.getAuthenticatedUsername();

        Users user = userRepository.findByUsername(username);

        List<Shares> userShares = sharesService.findByUserId(user.getId());
        return new ResponseEntity<>(userShares, HttpStatus.OK);
    }


}
