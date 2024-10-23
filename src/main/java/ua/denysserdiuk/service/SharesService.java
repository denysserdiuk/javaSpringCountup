package ua.denysserdiuk.service;

import ua.denysserdiuk.model.Shares;
import ua.denysserdiuk.model.Users;
import ua.denysserdiuk.model.StockPrice;
import java.math.BigDecimal;
import java.util.List;

public interface SharesService {
    public String addShare(Shares share);
    public List<Shares> findByUserId(long userId);
    Double updateShareProfit(Shares share, StockPrice stockprice);
}
