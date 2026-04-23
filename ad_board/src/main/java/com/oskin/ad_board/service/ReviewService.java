package com.oskin.ad_board.service;

import com.oskin.ad_board.model.Review;
import com.oskin.ad_board.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public boolean save(Review review) {
        return reviewRepository.create(review);
    }

    public boolean update(Review review) {
        return reviewRepository.update(review);
    }

    public boolean delete(int id) {
        return reviewRepository.delete(id);
    }

    public Review findById(int id) {
        Optional<Review> optional = reviewRepository.findById(id);
        return optional.orElse(null);
    }
}
