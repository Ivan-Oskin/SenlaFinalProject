package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.GetReviewRequest;
import com.oskin.ad_board.dto.request.ReviewRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.PaginationReviewResponse;
import com.oskin.ad_board.dto.response.ReviewResponse;
import com.oskin.ad_board.service.ReviewService;
import com.oskin.ad_board.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private final JwtUtils jwtUtils;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(JwtUtils jwtUtils, ReviewService reviewService) {
        this.reviewService = reviewService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/{ad_id}")
    public BooleanResponse createReview(@PathVariable("ad_id") int adId, @RequestBody ReviewRequest reviewRequest) {
        int authorId = jwtUtils.getCurrentId();
        return reviewService.save(reviewRequest, adId, authorId);
    }

    @DeleteMapping("/{id}")
    public BooleanResponse deleteReview(@PathVariable("id") int commentId) {
        int authorId = jwtUtils.getCurrentId();
        return reviewService.delete(commentId, authorId);
    }

    @GetMapping("/{ad_id}")
    public PaginationReviewResponse getReviewsAd(@PathVariable("ad_id") int adId, @RequestBody GetReviewRequest getReviewRequest) {
        return reviewService.getReviewByAd(adId, getReviewRequest);
    }
}