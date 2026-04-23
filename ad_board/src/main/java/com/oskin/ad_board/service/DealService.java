package com.oskin.ad_board.service;

import com.oskin.ad_board.model.Deal;
import com.oskin.ad_board.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DealService {
    private final DealRepository dealRepository;

    @Autowired
    public DealService(DealRepository dealRepository){
        this.dealRepository = dealRepository;
    }

    public boolean save(Deal deal) {
        return dealRepository.create(deal);
    }

    public boolean update(Deal deal) {
        return dealRepository.update(deal);
    }

    public boolean delete(int id) {
        return dealRepository.delete(id);
    }

    public Deal findById(int id) {
        Optional<Deal> optional = dealRepository.findById(id);
        return optional.orElse(null);
    }
}
