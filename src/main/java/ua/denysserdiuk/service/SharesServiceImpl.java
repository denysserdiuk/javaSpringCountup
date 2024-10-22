package ua.denysserdiuk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.denysserdiuk.model.Shares;
import ua.denysserdiuk.repository.SharesRepository;

import java.util.List;

@Service
public class SharesServiceImpl implements SharesService{
    private final SharesRepository sharesRepository;

    @Autowired
    public SharesServiceImpl(SharesRepository sharesRepository){
        this.sharesRepository = sharesRepository;
    }

    @Override
    public String addShare(Shares share) {
        Shares share1 = new Shares();

        share1.setUser(share.getUser());
        share1.setTicker(share.getTicker());
        share1.setAmount(share.getAmount());
        share1.setPrice(share.getPrice());
        share1.setPurchaseDate(share.getPurchaseDate());

        sharesRepository.save(share1);

        return "Share added";
    }

    @Override
    public List<Shares> findByUserId(long userId) {
        return sharesRepository.findByUserId(userId);
    }
}
