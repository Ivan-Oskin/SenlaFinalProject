package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.ProfileRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.ProfileResponse;
import com.oskin.ad_board.service.ProfileService;
import com.oskin.ad_board.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;
    private final JwtUtils jwtUtils;

    @Autowired
    public ProfileController(ProfileService profileService, JwtUtils jwtUtils) {
        this.profileService = profileService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/{id}")
    public ProfileResponse findById(@PathVariable("id") int id) {
        return profileService.findById(id);
    }

    @GetMapping
    public ProfileResponse findByUserId() {
        int userId = jwtUtils.getCurrentId();
        return profileService.findByUserId(userId);
    }

    @PostMapping
    public BooleanResponse createProfile(@RequestBody @Valid ProfileRequest profileRequest) {
        int idUser = jwtUtils.getCurrentId();
        return profileService.save(profileRequest, idUser);
    }

    @PutMapping
    public BooleanResponse updateProfileByUser(@RequestBody @Valid ProfileRequest profileRequest) {
        int idUser = jwtUtils.getCurrentId();
        return profileService.update(profileRequest, idUser);
    }

    @DeleteMapping
    public BooleanResponse delete() {
        int userId = jwtUtils.getCurrentId();
        return profileService.delete(userId);
    }
}
