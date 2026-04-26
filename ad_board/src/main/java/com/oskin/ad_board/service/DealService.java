package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.DealRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.model.Deal;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.DealRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.utils.MapperDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DealService {
    private final DealRepository dealRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final MapperDto mapperDto;

    @Autowired
    public DealService(DealRepository dealRepository, AdRepository adRepository, UserRepository userRepository, MapperDto mapperDto){
        this.dealRepository = dealRepository;
        this.mapperDto = mapperDto;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BooleanResponse save(DealRequest dealRequest, int buyerId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Ad> adOptional = adRepository.findById(dealRequest.getAdId());
        Optional<User> buyerOptional = userRepository.findById(buyerId);
        if(adOptional.isPresent() && buyerOptional.isPresent()) {
            Ad ad = adOptional.get();
            User buyer = buyerOptional.get();
            Deal deal = mapperDto.dealRequestToEntity(ad, buyer);
            booleanResponse.setBool(dealRepository.create(deal));
            
        }
        return booleanResponse;
    }

    @Transactional
    public boolean delete(int id) {
        return dealRepository.delete(id);
    }
}
