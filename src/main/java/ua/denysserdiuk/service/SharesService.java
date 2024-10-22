package ua.denysserdiuk.service;

import ua.denysserdiuk.model.Shares;

import java.util.List;

public interface SharesService {
    public String addShare(Shares share);
    public List<Shares> findByUserId(long userId);
}
