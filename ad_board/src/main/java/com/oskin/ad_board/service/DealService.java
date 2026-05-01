package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.DealRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.DealResponse;
import com.oskin.ad_board.model.*;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.DealRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.utils.MapperDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DealService {
    private final DealRepository dealRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final MapperDto mapperDto;

    @Autowired
    public DealService(DealRepository dealRepository,
                       AdRepository adRepository,
                       UserRepository userRepository,
                       MapperDto mapperDto) {
        this.dealRepository = dealRepository;
        this.mapperDto = mapperDto;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean delete(int id) {
        return dealRepository.delete(id);
    }

    public List<DealResponse> getHistorySellByUser(int id) {
        List<Deal> list = dealRepository.getHistory(id, true);
        return list.stream().map(mapperDto::dealToResponse).toList();
    }

    public List<DealResponse> getHistoryBuyByUser(int id) {
        List<Deal> list = dealRepository.getHistory(id, false);
        return list.stream().map(mapperDto::dealToResponse).toList();
    }

    @Transactional
    public BooleanResponse save(DealRequest dealRequest, int sellerId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<User> sellerOptional = userRepository.findById(sellerId);
        Optional<Ad> adOptional = adRepository.findById(dealRequest.getAdId());
        Optional<User> buyerOptional = userRepository.findById(dealRequest.getBuyerId());
        if (sellerOptional.isPresent() && adOptional.isPresent() && buyerOptional.isPresent()) {
            User seller = sellerOptional.get();
            Ad ad = adOptional.get();
            User buyer = buyerOptional.get();
            if (ad.getSeller().getId() == seller.getId() && ad.getStatus() == StatusAd.ACTIVE) {
                ad.setStatus(StatusAd.RESERVED);
                Deal deal = new Deal(ad, buyer, StatusDeal.CREATED);
                if (adRepository.update(ad) && dealRepository.create(deal)) {
                    booleanResponse.setBool(true);
                }
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse cancelDeal(int dealId, int userId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Deal> dealOptional = dealRepository.findById(dealId);
        if (userOptional.isPresent() && dealOptional.isPresent()) {
            User user = userOptional.get();
            Deal deal = dealOptional.get();
            if (user.getId() == deal.getBuyer().getId() || user.getId() == deal.getAd().getSeller().getId()) {
                booleanResponse.setBool(changeStatus(StatusDeal.CANCELED, deal));
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse soldDeal(int dealId, int userId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Deal> dealOptional = dealRepository.findById(dealId);
        if (userOptional.isPresent() && dealOptional.isPresent()) {
            User user = userOptional.get();
            Deal deal = dealOptional.get();
            if (user.getId() == deal.getAd().getSeller().getId()) {
                booleanResponse.setBool(changeStatus(StatusDeal.SOLD, deal));
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse returnDeal(int dealId, int userId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Deal> dealOptional = dealRepository.findById(dealId);
        if (userOptional.isPresent() && dealOptional.isPresent()) {
            User user = userOptional.get();
            Deal deal = dealOptional.get();
            if (user.getId() == deal.getAd().getSeller().getId()) {
                booleanResponse.setBool(changeStatus(StatusDeal.RETURNED, deal));
            }
        }
        return booleanResponse;
    }

    private boolean changeStatus(StatusDeal statusDeal, Deal deal) {
        StatusAd statusAd;
        StatusDeal verifyStatusDeal;
        switch (statusDeal) {
            case SOLD -> {
                verifyStatusDeal = StatusDeal.CREATED;
                statusAd = StatusAd.COMPLETED;
            }
            case CANCELED -> {
                verifyStatusDeal = StatusDeal.CREATED;
                statusAd = StatusAd.ACTIVE;
            }
            case RETURNED -> {
                verifyStatusDeal = StatusDeal.SOLD;
                statusAd = StatusAd.ACTIVE;
            }
            default -> {
                return false;
            }
        }
        if (deal.getStatus() == verifyStatusDeal) {
            Ad ad = deal.getAd();
            ad.setStatus(statusAd);
            deal.setStatus(statusDeal);
            return (adRepository.update(ad) && dealRepository.update(deal));
        }
        return false;
    }
}