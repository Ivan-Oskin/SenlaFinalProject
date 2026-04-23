package com.oskin.ad_board.service;

import com.oskin.ad_board.model.Profile;
import com.oskin.ad_board.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public boolean save(Profile profile) {
        return profileRepository.create(profile);
    }

    public boolean update(Profile profile) {
        return profileRepository.update(profile);
    }

    public boolean delete(int id) {
        return profileRepository.delete(id);
    }

    public Profile findById(int id) {
        Optional<Profile> optional = profileRepository.findById(id);
        return optional.orElse(null);
    }
}
