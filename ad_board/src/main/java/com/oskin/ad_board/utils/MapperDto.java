package com.oskin.ad_board.utils;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.request.MessageRequest;
import com.oskin.ad_board.dto.request.ProfileRequest;
import com.oskin.ad_board.dto.request.ReviewRequest;
import com.oskin.ad_board.dto.response.*;
import com.oskin.ad_board.model.*;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MapperDto {
    public Ad adRequestToEntity(AdRequest adRequest, User user, City city) {
        Ad ad = new Ad();
        ad.setCity(city);
        ad.setSeller(user);
        ad.setDescription(adRequest.getDescription());
        ad.setPrice(adRequest.getPrice());
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
        adResponse.setPrice(ad.getPrice());
        return adResponse;
    }

    public Profile profileRequestToEntity(ProfileRequest profileRequest, User user, City city) {
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
        profileResponse.setCity(profile.getCity().getName().toLowerCase());
        profileResponse.setUserId(profile.getUser().getId());
        profileResponse.setRating(profile.getRating());
        profileResponse.setRatingCount(profile.getRatingCount());
        profileResponse.setCreatedDateTime(profile.getCreatedDateTime());
        return profileResponse;
    }

    public Review reviewRequestToEntity(ReviewRequest reviewRequest, Ad ad, User author) {
        Review review = new Review();
        review.setAd(ad);
        review.setAuthor(author);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        return review;
    }

    public Deal dealRequestToEntity(Ad ad, User user) {
        Deal deal = new Deal();
        deal.setAd(ad);
        deal.setBuyer(user);
        deal.setStatus(StatusDeal.CREATED);
        return deal;
    }

    public DealResponse dealToResponse(Deal deal) {
        DealResponse dealResponse = new DealResponse();
        dealResponse.setAdId(deal.getAd().getId());
        dealResponse.setAdTitle(deal.getAd().getTitle());
        dealResponse.setBuyerId(deal.getBuyer().getId());
        dealResponse.setCreatedDateTime(deal.getCreatedDateTime());
        dealResponse.setId(deal.getId());
        return dealResponse;
    }

    public ReviewResponse tupleReviewToResponse(Tuple tuple) {
        Review review = tuple.get("review", Review.class);
        String authorName = tuple.get("authorName", String.class);
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setAuthorId(review.getAuthor().getId());
        reviewResponse.setId(review.getId());
        reviewResponse.setAuthorName(authorName);
        reviewResponse.setComment(review.getComment());
        reviewResponse.setCreatedDateTime(review.getCreatedDateTime());
        reviewResponse.setRating(review.getRating());
        return reviewResponse;
    }

    public MessageResponse messageToResponse(Tuple tuple) {
        MessageResponse messageResponse = new MessageResponse();
        Message message = tuple.get("message", Message.class);
        String userName = tuple.get("userName", String.class);
        messageResponse.setSenderName(userName);
        messageResponse.setText(message.getMessage());
        messageResponse.setSendDateTime(message.getSendDateTime());
        messageResponse.setId(message.getId());
        return messageResponse;
    }

    public MessageResponse messageToResponse(Message message, Profile profile) {
        MessageResponse messageResponse = new MessageResponse();
        String userName = profile.getName();
        messageResponse.setSenderName(userName);
        messageResponse.setText(message.getMessage());
        messageResponse.setSendDateTime(message.getSendDateTime());
        messageResponse.setId(message.getId());
        return messageResponse;
    }

    public DialogResponse dialogToResponse(Dialog dialog, List<MessageResponse> messageResponses) {
        DialogResponse dialogResponse = new DialogResponse();
        int lastId = 0;
        if(!messageResponses.isEmpty()) {
            lastId = messageResponses.get(messageResponses.size()-1).getId();
        }
        dialogResponse.setAdTitle(dialog.getAd().getTitle());
        dialogResponse.setList(messageResponses);
        dialogResponse.setLastId(lastId);
        return dialogResponse;
    }

    public Message messageRequestToEntity(MessageRequest messageRequest, User user, Dialog dialog) {
        Message message = new Message();
        message.setMessage(messageRequest.getMessage());
        message.setDialog(dialog);
        message.setUser(user);
        return message;
    }

    public PaginationReviewResponse ReviewsToPaginationResponse(List<ReviewResponse> list) {
        PaginationReviewResponse paginationReviewResponse = new PaginationReviewResponse();
        paginationReviewResponse.setList(list);
        if(!list.isEmpty()) {
            ReviewResponse lastReviewResponse = list.get(list.size() - 1);
            paginationReviewResponse.setLastId(lastReviewResponse.getId());
            paginationReviewResponse.setLastDateTime(lastReviewResponse.getCreatedDateTime());
            paginationReviewResponse.setLastRating(lastReviewResponse.getRating());
        } else {
            paginationReviewResponse.setLastId(0);
            paginationReviewResponse.setLastDateTime(null);
            paginationReviewResponse.setLastRating(0);
        }
        return paginationReviewResponse;
    }

    public PaginationAdResponse adToPaginationResponse(List<AdResponse> list, int page) {
        PaginationAdResponse paginationAdResponse = new PaginationAdResponse();
        paginationAdResponse.setAdResponseList(list);
        paginationAdResponse.setThisPage(page);
        return paginationAdResponse;
    }

    public PaginationAdModerationResponse adToModerationPaginationResponse(List<AdResponse> list) {
        PaginationAdModerationResponse paginationAdResponse = new PaginationAdModerationResponse();
        int lastId;
        if(!list.isEmpty()) {
            lastId = list.get(list.size() - 1).getId();
        } else {
            lastId = 0;
        }
        paginationAdResponse.setAdResponseList(list);
        paginationAdResponse.setLastId(lastId);
        return paginationAdResponse;
    }
}
