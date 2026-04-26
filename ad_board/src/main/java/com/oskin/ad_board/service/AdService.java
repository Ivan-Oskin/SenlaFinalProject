package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.model.City;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.CityRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.utils.MapperDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final MapperDto mapperDto;

    @Autowired
    public AdService(AdRepository adRepository, CityRepository cityRepository, UserRepository userRepository, MapperDto mapperDto) {
        this.adRepository = adRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.mapperDto = mapperDto;
    }

    @Transactional
    public BooleanResponse save(AdRequest adRequest, int idSeller) {
        Ad ad = new Ad();
        BooleanResponse booleanResponse = new BooleanResponse(false);
        String cityName = adRequest.getCityLowerCase();
        Optional<User> userOptional = userRepository.findById(idSeller);
        Optional<City> cityOptional = cityRepository.findByName(cityName);
        if(userOptional.isPresent() && cityOptional.isPresent()) {
            User user = userOptional.get();
            City city = cityOptional.get();
            ad = mapperDto.adToEntity(adRequest, user, city);
            booleanResponse.setBool(adRepository.create(ad));
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse update(AdRequest adRequest, int idAd, int idSeller) {
        Optional<Ad> adOptional = adRepository.findById(idAd);
        Optional<City> cityOptional = cityRepository.findByName(adRequest.getCityLowerCase());
        BooleanResponse booleanResponse = new BooleanResponse(false);
        if(adOptional.isPresent() && cityOptional.isPresent()) {
            Ad ad = adOptional.get();
            if(ad.getSeller().getId() == idSeller) {
                ad.setTitle(adRequest.getTitle());
                ad.setPrice(adRequest.getPrice());
                ad.setCity(cityOptional.get());
                ad.setDescription(adRequest.getDescription());
                booleanResponse.setBool(adRepository.update(ad));
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse delete(int idAd, int idSeller) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Ad> adOptional = adRepository.findById(idAd);
        if(adOptional.isPresent()) {
            Ad ad = adOptional.get();
            if(ad.getSeller().getId() == idSeller) {
                booleanResponse.setBool(adRepository.delete(ad));
            }
        }
        return booleanResponse;
    }

    public AdResponse findById(int id) {
        Optional<Ad> optional = adRepository.findById(id);
        Ad ad = optional.orElseThrow();
        return mapperDto.adToResponse(ad);
    }
}
