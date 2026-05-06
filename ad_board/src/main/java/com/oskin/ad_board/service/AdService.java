package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.request.GetAdRequest;
import com.oskin.ad_board.dto.request.GetAdToModeration;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.PaginationAdModerationResponse;
import com.oskin.ad_board.dto.response.PaginationAdResponse;
import com.oskin.ad_board.exception.IdMatchException;
import com.oskin.ad_board.exception.StatusNoValidException;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.model.Profile;
import com.oskin.ad_board.model.City;
import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.model.StatusAd;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.CityRepository;
import com.oskin.ad_board.repository.ProfileRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.utils.MapperDto;
import jakarta.persistence.EntityNotFoundException;
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
    public BooleanResponse save(AdRequest adRequest, int sellerId) {
        String cityName = adRequest.getCityLowerCase();
        Optional<User> userOptional = userRepository.findById(sellerId);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("not found user with id = " + sellerId));
        Optional<Profile> profileOptional = profileRepository.findByUserId(sellerId);
        profileOptional.orElseThrow(() -> new EntityNotFoundException("not found profile with user id = " + sellerId));
        Optional<City> cityOptional = cityRepository.findByName(cityName);
        City city = cityOptional.orElseThrow(() -> new EntityNotFoundException("not found city with name = " + adRequest.getCity()));
        Ad ad = mapperDto.adRequestToEntity(adRequest, user, city);
        ad.setStatus(StatusAd.DRAFT);
        return new BooleanResponse(adRepository.create(ad));
    }

    @Transactional
    public BooleanResponse update(AdRequest adRequest, int adId, int sellerId) {
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + adId));
        Optional<City> cityOptional = cityRepository.findByName(adRequest.getCityLowerCase());
        City city = cityOptional.orElseThrow(() -> new EntityNotFoundException("not found city with name = " + adRequest.getCity()));
        if (ad.getSeller().getId() != sellerId) {
            throw new IdMatchException("the user's ID does not match the seller's ID");
        }
        if (ad.getStatus() != StatusAd.COMPLETED && ad.getStatus() != StatusAd.RESERVED) {
            ad.setTitle(adRequest.getTitle());
            ad.setPrice(adRequest.getPrice());
            ad.setCity(city);
            ad.setDescription(adRequest.getDescription());
            if (ad.getStatus() == StatusAd.HIDDEN || ad.getStatus() == StatusAd.ACTIVE) {
                ad.setStatus(StatusAd.MODERATION);
            }
            return new BooleanResponse(adRepository.update(ad));
        } else throw new StatusNoValidException("ad have status COMPLETED or RESERVED");
    }

    @Transactional
    public BooleanResponse delete(int adId, int sellerId) {
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + adId));
        if (ad.getSeller().getId() == sellerId) {
            return new BooleanResponse(adRepository.delete(ad));
        } else throw new IdMatchException("the user's ID does not match the seller's ID");
    }

    public AdResponse findById(int id) {
        Optional<Ad> adOptional = adRepository.findById(id);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + id));
        return mapperDto.adToResponse(ad);
    }

    public PaginationAdResponse findByTitle(GetAdRequest getAdRequest) {
        List<Ad> list = adRepository.searchByTitle(getAdRequest);
        int page = getAdRequest.getPage();
        List<AdResponse> adResponseList = list.stream().map(mapperDto::adToResponse).toList();
        return mapperDto.adToPaginationResponse(adResponseList, page);
    }

    @Transactional
    public BooleanResponse publishBySeller(int adId, int sellerId) {
        Optional<User> userOptional = userRepository.findById(sellerId);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("not found user with id = " + sellerId));
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + adId));
        if (ad.getSeller().getId() == sellerId) {
            if (ad.getStatus() != StatusAd.COMPLETED && ad.getStatus() != StatusAd.RESERVED) {
                ad.setStatus(StatusAd.MODERATION);
                return new BooleanResponse(adRepository.update(ad));
            } else throw new StatusNoValidException("ad have status COMPLETED or RESERVED");
        } else throw new IdMatchException("the user's ID does not match the seller's ID");
    }

    @Transactional
    public BooleanResponse archiveBySeller(int adId, int sellerId) {
        Optional<User> userOptional = userRepository.findById(sellerId);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("not found user with id = " + sellerId));
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + adId));
        if (ad.getSeller().getId() == sellerId) {
            if (ad.getStatus() != StatusAd.COMPLETED && ad.getStatus() != StatusAd.RESERVED) {
                ad.setStatus(StatusAd.ARCHIVED);
                return new BooleanResponse(adRepository.update(ad));
            } else throw new StatusNoValidException("ad have status COMPLETED or RESERVED");
        } else throw new IdMatchException("the user's ID does not match the seller's ID");
    }

    @Transactional
    public BooleanResponse publishAdByModeration(int adId) {
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + adId));
        if (ad.getStatus() == StatusAd.MODERATION) {
            ad.setStatus(StatusAd.ACTIVE);
            return new BooleanResponse(adRepository.update(ad));
        } else throw new StatusNoValidException("ad haven't status MODERATION or HIDDEN");
    }

    @Transactional
    public BooleanResponse hideAdByModeration(int adId) {
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + adId));
        if (ad.getStatus() == StatusAd.MODERATION || ad.getStatus() == StatusAd.ACTIVE) {
            ad.setStatus(StatusAd.HIDDEN);
            return new BooleanResponse(adRepository.update(ad));
        } else throw new StatusNoValidException("ad haven't status MODERATION or ACTIVE");
    }

    @Transactional
    public BooleanResponse payAd(int adId) {
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + adId));
        ad.setPaid(true);
        return new BooleanResponse(adRepository.update(ad));
    }

    @Transactional
    public BooleanResponse removePaidAd(int adId) {
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id = " + adId));
        ad.setPaid(false);
        return new BooleanResponse(adRepository.update(ad));
    }

    public PaginationAdModerationResponse getModerationList(GetAdToModeration getAdToModeration) {
        List<Ad> list = adRepository.findAllToModeration(getAdToModeration);
        List<AdResponse> adResponseList = list.stream().map(mapperDto::adToResponse).toList();
        return mapperDto.adToModerationPaginationResponse(adResponseList);
    }
}
