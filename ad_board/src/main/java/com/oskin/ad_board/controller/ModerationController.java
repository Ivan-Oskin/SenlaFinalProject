package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.GetAdToModeration;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.PaginationAdModerationResponse;
import com.oskin.ad_board.service.AdService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moderation")
public class ModerationController {
    private final AdService adService;

    @Autowired
    public ModerationController(AdService adService) {
        this.adService = adService;
    }

    @PutMapping("/pay/{id}")
    public BooleanResponse payAd(@PathVariable("id") int id) {
        return adService.payAd(id);
    }

    @PutMapping("/Remove_paid/{id}")
    public BooleanResponse removePayAd(@PathVariable("id") int id) {
        return adService.removePaidAd(id);
    }

    @PutMapping("/publish/{id}")
    public BooleanResponse publishAd(@PathVariable("id") int id) {
        return adService.publishAdByModeration(id);
    }

    @PutMapping("/hide/{id}")
    public BooleanResponse hideAd(@PathVariable("id") int id) {
        return adService.hideAdByModeration(id);
    }

    @GetMapping
    public PaginationAdModerationResponse getModerationList(@RequestBody @Valid GetAdToModeration getAdToModeration) {
        return adService.getModerationList(getAdToModeration);
    }
}
