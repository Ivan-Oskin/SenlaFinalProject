package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ad")
public class AdController {
    private final AdService adService;
    @Autowired
    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/{id}")
    public AdResponse findById(@PathVariable("id") int id)
    {
        return adService.findById(id);
    }


}
