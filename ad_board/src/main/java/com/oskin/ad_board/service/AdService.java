package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.model.Profile;
import com.oskin.ad_board.model.City;
import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.model.AdSortType;
import com.oskin.ad_board.model.StatusAd;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.CityRepository;
import com.oskin.ad_board.repository.ProfileRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.utils.MapperDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final MapperDto mapperDto;
    private final ProfileRepository profileRepository;

    @Autowired
    public AdService(AdRepository adRepository,
                     CityRepository cityRepository,
                     UserRepository userRepository,
                     MapperDto mapperDto,
                     ProfileRepository profileRepository) {
        this.adRepository = adRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.mapperDto = mapperDto;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public BooleanResponse save(AdRequest adRequest, int idSeller) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        String cityName = adRequest.getCityLowerCase();
        Optional<User> userOptional = userRepository.findById(idSeller);
        Optional<City> cityOptional = cityRepository.findByName(cityName);
        Optional<Profile> profileOptional = profileRepository.findByUserId(idSeller);
        if (userOptional.isPresent() && cityOptional.isPresent() && profileOptional.isPresent()) {
            User user = userOptional.get();
            City city = cityOptional.get();
            Ad ad = mapperDto.adRequestToEntity(adRequest, user, city);
            ad.setStatus(StatusAd.DRAFT);
            booleanResponse.setBool(adRepository.create(ad));
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse update(AdRequest adRequest, int idAd, int idSeller) {
        Optional<Ad> adOptional = adRepository.findById(idAd);
        Optional<City> cityOptional = cityRepository.findByName(adRequest.getCityLowerCase());
        BooleanResponse booleanResponse = new BooleanResponse(false);
        if (adOptional.isPresent() && cityOptional.isPresent()) {
            Ad ad = adOptional.get();
            if (ad.getSeller().getId() == idSeller && ad.getStatus() != StatusAd.SOLD && ad.getStatus() != StatusAd.RESERVED) {
                ad.setTitle(adRequest.getTitle());
                ad.setPrice(adRequest.getPrice());
                ad.setCity(cityOptional.get());
                ad.setDescription(adRequest.getDescription());
                if (ad.getStatus() == StatusAd.HIDDEN || ad.getStatus() == StatusAd.ACTIVE) {
                    ad.setStatus(StatusAd.MODERATION);
                }
                booleanResponse.setBool(adRepository.update(ad));
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse delete(int idAd, int idSeller) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Ad> adOptional = adRepository.findById(idAd);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            if (ad.getSeller().getId() == idSeller) {
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

    public List<AdResponse> findByTitle(String title) {
        List<Ad> list = adRepository.searchByTitle(title, AdSortType.DEFAULT, false);
        return list.stream().map(mapperDto::adToResponse).toList();
    }

    public List<AdResponse> findByTitleSortedByCreatedDateTime(String title) {
        List<Ad> list = adRepository.searchByTitle(title, AdSortType.CREATE_DATE_TIME, false);
        return list.stream().map(mapperDto::adToResponse).toList();
    }

    public List<AdResponse> findByTitleSortedByCostly(String title) {
        List<Ad> list = adRepository.searchByTitle(title, AdSortType.PRICE_MAX, false);
        return list.stream().map(mapperDto::adToResponse).toList();
    }

    public List<AdResponse> findByTitleSortedByLessCostly(String title) {
        List<Ad> list = adRepository.searchByTitle(title, AdSortType.PRICE_MIN, false);
        return list.stream().map(mapperDto::adToResponse).toList();
    }

    @Transactional
    public BooleanResponse publishBySeller(int adId, int sellerId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<User> userOptional = userRepository.findById(sellerId);
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (userOptional.isPresent() && adOptional.isPresent()) {
            Ad ad = adOptional.get();
            if (ad.getSeller().getId() == userOptional.get().getId()) {
                ad.setStatus(StatusAd.MODERATION);
                booleanResponse.setBool(adRepository.update(ad));
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse archiveBySeller(int adId, int sellerId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<User> userOptional = userRepository.findById(sellerId);
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (userOptional.isPresent() && adOptional.isPresent()) {
            Ad ad = adOptional.get();
            if (ad.getSeller().getId() == userOptional.get().getId()) {
                ad.setStatus(StatusAd.ARCHIVED);
                booleanResponse.setBool(adRepository.update(ad));
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse publishAdByModeration(int adId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            if (ad.getStatus() == StatusAd.MODERATION || ad.getStatus() == StatusAd.HIDDEN) {
                ad.setStatus(StatusAd.ACTIVE);
                booleanResponse.setBool(adRepository.update(ad));
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse hideAdByModeration(int adId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            if (ad.getStatus() == StatusAd.MODERATION || ad.getStatus() == StatusAd.ACTIVE) {
                ad.setStatus(StatusAd.HIDDEN);
                booleanResponse.setBool(adRepository.update(ad));
            }
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse payAd(int adId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            ad.setPaid(true);
            booleanResponse.setBool(adRepository.update(ad));
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse removePaidAd(int adId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            ad.setPaid(false);
            booleanResponse.setBool(adRepository.update(ad));
        }
        return booleanResponse;
    }

    public List<AdResponse> getModerationList() {
        List<Ad> list = adRepository.findAllToModeration();
        return list.stream().map(mapperDto::adToResponse).toList();
    }

    public List<AdResponse> findBySeller(int id) {
        List<Ad> list = adRepository.findBySeller(id);
        return list.stream().map(mapperDto::adToResponse).toList();
    }

}
