package com.oskin.ad_board.utils;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.request.ProfileRequest;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.dto.response.ProfileResponse;
import com.oskin.ad_board.model.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
public class MapperDto {
    public Ad adToEntity(AdRequest adRequest, User user, City city) {
        Ad ad = new Ad();
        ad.setCity(city);
        ad.setSeller(user);
        ad.setDescription(adRequest.getDescription());
        ad.setPrice(adRequest.getPrice());
        ad.setStatus(StatusAd.MODERATION);
        ad.setTitle(adRequest.getTitle());
        ad.setPaid(false);
        return ad;
    }

    public AdResponse adToResponse(Ad ad) {
        AdResponse adResponse = new AdResponse();
        adResponse.setId(ad.getId());
        adResponse.setDescription(ad.getDescription());
        adResponse.setTitle(ad.getTitle());
        adResponse.setCity(ad.getCity().getName());
        adResponse.setSeller_id(ad.getSeller().getId());
        adResponse.setStatusAd(ad.getStatus());
        adResponse.setCreatedDateTime(ad.getCreatedDateTime());
        return adResponse;
    }

    public Profile profileToEntity(ProfileRequest profileRequest, User user, City city) {
        Profile profile = new Profile();
        profile.setCity(city);
        profile.setUser(user);
        profile.setName(profileRequest.getName());
        profile.setSurname(profileRequest.getSurname());
        profile.setAge(profileRequest.getAge());
        return profile;
    }

    public ProfileResponse profileToResponse(Profile profile) {
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setId(profile.getId());
        profileResponse.setName(profile.getName());
        profileResponse.setSurname(profile.getSurname());
        profileResponse.setAge(profile.getAge());
        profileResponse.setCityId(profile.getCity().getId());
        profileResponse.setUser_id(profile.getUser().getId());
        profileResponse.setRating(profile.getRating());
        profileResponse.setRating_count(profile.getRatingCount());
        profileResponse.setCreated_date_time(profile.getCreatedDateTime());
        return profileResponse;
    }
}
