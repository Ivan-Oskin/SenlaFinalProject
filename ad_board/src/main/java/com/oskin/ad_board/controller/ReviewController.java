package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.ReviewRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.service.ReviewService;
import com.oskin.ad_board.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public BooleanResponse createReview(@RequestBody ReviewRequest reviewRequest) {
        int authorId = jwtUtils.getCurrentId();
        return reviewService.save(reviewRequest, authorId);
    }

    @DeleteMapping("/{id}")
    public BooleanResponse deleteReview(@PathVariable("id") int commentId) {
        int authorId = jwtUtils.getCurrentId();
        return reviewService.delete(commentId, authorId);
    }
}