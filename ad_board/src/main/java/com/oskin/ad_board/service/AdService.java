package com.oskin.ad_board.service;

import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdService {
    private final AdRepository adRepository;

    @Autowired
    public AdService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    public boolean save(Ad ad) {
        return adRepository.create(ad);
    }

    public boolean update(Ad ad) {
        return adRepository.update(ad);
    }

    public boolean delete(int id) {
        return adRepository.delete(id);
    }

    public Ad findById(int id) {
        Optional<Ad> optional = adRepository.findById(id);
        return optional.orElse(null);
    }
}
