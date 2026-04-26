package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.ReviewRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.model.Review;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.ReviewRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.utils.MapperDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final MapperDto mapperDto;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, AdRepository adRepository, MapperDto mapperDto){
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.mapperDto = mapperDto;
    }

    @Transactional
    public BooleanResponse save(ReviewRequest reviewRequest, int idAuthor) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<User> userOptional = userRepository.findById(idAuthor);
        Optional<Ad> adOptional = adRepository.findById(reviewRequest.getAdId());
        if(userOptional.isPresent() && adOptional.isPresent()) {
            Review review = mapperDto.reviewRequestToEntity(reviewRequest, adOptional.get(),  userOptional.get());
            booleanResponse.setBool(reviewRepository.create(review));
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse delete(int reviewId, int authorId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if(reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            if(review.getAuthor().getId() == authorId) {
                booleanResponse.setBool(reviewRepository.delete(review));
            }
        }
        return booleanResponse;
    }
}
