package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.DealRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.DealResponse;
import com.oskin.ad_board.service.DealService;
import com.oskin.ad_board.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deal")
@Validated
public class DealController {
    private final JwtUtils jwtUtils;
    private final DealService dealService;

    @Autowired
    public DealController(JwtUtils jwtUtils, DealService dealService) {
        this.jwtUtils = jwtUtils;
        this.dealService = dealService;
    }

    @GetMapping("/history/buy")
    public List<DealResponse> getHistoryBuyByUser() {
        int userId = jwtUtils.getCurrentId();
        return dealService.getHistoryBuyByUser(userId);
    }

    @GetMapping("/history/sell")
    public List<DealResponse> getHistorySellByUser() {
        int userId = jwtUtils.getCurrentId();
        return dealService.getHistorySellByUser(userId);
    }

    @PostMapping
    public BooleanResponse createDeal(@RequestBody @Valid DealRequest dealRequest) {
        int userId = jwtUtils.getCurrentId();
        return dealService.save(dealRequest, userId);
    }

    @PutMapping("/cancel/{deal_id}")
    public BooleanResponse cancelDeal(@PathVariable("deal_id") int dealId) {
        int userId = jwtUtils.getCurrentId();
        return dealService.cancelDeal(dealId, userId);
    }

    @PutMapping("/sold/{deal_id}")
    public BooleanResponse soldDeal(@PathVariable("deal_id") int dealId) {
        int userId = jwtUtils.getCurrentId();
        return dealService.soldDeal(dealId, userId);
    }

    @PutMapping("/return/{deal_id}")
    public BooleanResponse returnDeal(@PathVariable("deal_id") int dealId) {
        int userId = jwtUtils.getCurrentId();
        return dealService.returnDeal(dealId, userId);
    }
}
