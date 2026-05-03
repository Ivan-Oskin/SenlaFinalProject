package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.DealRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.DealResponse;
import com.oskin.ad_board.exception.IdMatchException;
import com.oskin.ad_board.model.*;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.DealRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.utils.MapperDto;
import jakarta.persistence.EntityNotFoundException;
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
        Optional<User> sellerOptional = userRepository.findById(sellerId);
        User seller = sellerOptional.orElseThrow(() -> new EntityNotFoundException("not found seller with id " + sellerId));
        Optional<Ad> adOptional = adRepository.findById(dealRequest.getAdId());
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id " + dealRequest.getAdId()));
        Optional<User> buyerOptional = userRepository.findById(dealRequest.getBuyerId());
        User buyer = buyerOptional.orElseThrow(() -> new EntityNotFoundException("not found buyer with id " + dealRequest.getBuyerId()));
        if(seller.getId() == buyer.getId()) {
            throw new IdMatchException("the buyer and seller have the same ID");
        }
        if (ad.getSeller().getId() == seller.getId() && ad.getStatus() == StatusAd.ACTIVE) {
            ad.setStatus(StatusAd.RESERVED);
            Deal deal = new Deal(ad, buyer, StatusDeal.CREATED);
            if (adRepository.update(ad) && dealRepository.create(deal)) {
                return new BooleanResponse(true);
            } else {
                return new BooleanResponse(false);
            }
        } else throw new IdMatchException("the user's ID does not match the seller's ID");
    }

    @Transactional
    public BooleanResponse cancelDeal(int dealId, int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("not found user with id " + userId));
        Optional<Deal> dealOptional = dealRepository.findById(dealId);
        Deal deal = dealOptional.orElseThrow(() -> new EntityNotFoundException("not found deal with id " + dealId));
        if (user.getId() == deal.getBuyer().getId() || user.getId() == deal.getAd().getSeller().getId()) {
            return new BooleanResponse(changeStatus(StatusDeal.CANCELED, deal));
        } else throw new IdMatchException("the user's ID does not match the seller's ID or buyer's ID");
    }

    @Transactional
    public BooleanResponse soldDeal(int dealId, int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("not found user with id " + userId));
        Optional<Deal> dealOptional = dealRepository.findById(dealId);
        Deal deal = dealOptional.orElseThrow(() -> new EntityNotFoundException("not found deal with id " + dealId));
        if (user.getId() == deal.getAd().getSeller().getId()) {
            return new BooleanResponse(changeStatus(StatusDeal.SOLD, deal));
        } else throw new IdMatchException("the user's ID does not match the seller's ID");
    }

    @Transactional
    public BooleanResponse returnDeal(int dealId, int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("not found user with id " + userId));
        Optional<Deal> dealOptional = dealRepository.findById(dealId);
        Deal deal = dealOptional.orElseThrow(() -> new EntityNotFoundException("not found deal with id " + dealId));
        if (user.getId() == deal.getAd().getSeller().getId()) {
            return new BooleanResponse(changeStatus(StatusDeal.RETURNED, deal));
        } else throw new IdMatchException("the user's ID does not match the seller's ID");
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