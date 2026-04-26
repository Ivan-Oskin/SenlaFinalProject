package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.DealRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.repository.DealRepository;
import com.oskin.ad_board.service.DealService;
import com.oskin.ad_board.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("deal")
public class DealController {
    private final JwtUtils jwtUtils;
    private final DealService dealService;

    @Autowired
    public DealController(JwtUtils jwtUtils, DealService dealService) {
        this.jwtUtils = jwtUtils;
        this.dealService = dealService;
    }

    @PostMapping
    public BooleanResponse createDeal(@RequestBody DealRequest dealRequest) {
        int buyerId = jwtUtils.getCurrentId();
        return dealService.save(dealRequest, buyerId);
    }
}
