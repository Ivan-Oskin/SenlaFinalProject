package com.oskin.ad_board.utils;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.model.City;
import com.oskin.ad_board.model.StatusAd;
import com.oskin.ad_board.model.User;
import org.springframework.stereotype.Component;

@Component
public class MapperDto {
    public Ad AdToEntity(AdRequest adRequest, User user, City city) {
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

    public AdResponse AdToResponse(Ad ad) {
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
}
