package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.service.AdService;
import com.oskin.ad_board.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public BooleanResponse addNewAdd(@RequestBody AdRequest adRequest) {
        int idSeller = jwtUtils.getCurrentId();
        return adService.save(adRequest, idSeller);
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
