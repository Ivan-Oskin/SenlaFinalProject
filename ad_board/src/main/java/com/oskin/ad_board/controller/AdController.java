package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.service.AdService;
import com.oskin.ad_board.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ad")
public class AdController {
    private final AdService adService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AdController(AdService adService, JwtUtils jwtUtils) {
        this.adService = adService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/{id}")
    public AdResponse findById(@PathVariable("id") int id) {
        return adService.findById(id);
    }

    @GetMapping("/default/{title}")
    public List<AdResponse> findByTitleSortedByRating(@PathVariable("title") String title) {
        return adService.findByTitle(title);
    }

    @GetMapping("/created_date_time/{title}")
    public List<AdResponse> findByTitleSortedByCreatedDateTime(@PathVariable("title") String title) {
        return adService.findByTitleSortedByCreatedDateTime(title);
    }

    @GetMapping("/price_desc/{title}")
    public List<AdResponse> findByTitleSortedByPriceDESC(@PathVariable("title") String title) {
        return adService.findByTitleSortedByPriceDESC(title);
    }

    @GetMapping("/price_asc/{title}")
    public List<AdResponse> findByTitleSortedByPriceASC(@PathVariable("title") String title) {
        return adService.findByTitleSortedByPriceASC(title);
    }

    @GetMapping("/seller/{id}")
    public List<AdResponse> findBySeller(@PathVariable("id") int id) {
        return adService.findBySeller(id);
    }

    @PostMapping
    public BooleanResponse addNewAdd(@RequestBody AdRequest adRequest) {
        int idSeller = jwtUtils.getCurrentId();
        return adService.save(adRequest, idSeller);
    }

    @PutMapping("/publish/{id}")
    public BooleanResponse publishAdd(@PathVariable("id") int adId) {
        int sellerId = jwtUtils.getCurrentId();
        return adService.publishBySeller(adId, sellerId);
    }

    @PutMapping("/archive/{id}")
    public BooleanResponse archiveAdd(@PathVariable("id") int adId) {
        int sellerId = jwtUtils.getCurrentId();
        return adService.archiveBySeller(adId, sellerId);
    }

    @PutMapping("/{id}")
    public BooleanResponse update(@PathVariable("id") int idAd, @RequestBody AdRequest adRequest) {
        int idSeller = jwtUtils.getCurrentId();
        return adService.update(adRequest, idAd, idSeller);
    }

    @DeleteMapping("/{id}")
    public BooleanResponse delete(@PathVariable("id") int idAd) {
        int idSeller = jwtUtils.getCurrentId();
        return adService.delete(idAd, idSeller);
    }
}
