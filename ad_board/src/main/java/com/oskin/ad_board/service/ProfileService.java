package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.ProfileRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.ProfileResponse;
import com.oskin.ad_board.model.City;
import com.oskin.ad_board.model.Profile;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.CityRepository;
import com.oskin.ad_board.repository.ProfileRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.utils.MapperDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final MapperDto mapperDto;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, MapperDto mapperDto, CityRepository cityRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.mapperDto = mapperDto;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BooleanResponse save(ProfileRequest profileRequest, int idUser) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<City> cityOptional = cityRepository.findByName(profileRequest.getCityLowerCase());
        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent() && cityOptional.isPresent()) {
            User user = userOptional.get();
            City city = cityOptional.get();
            Profile profile = mapperDto.profileRequestToEntity(profileRequest, user, city);
            booleanResponse.setBool(profileRepository.create(profile));
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse update(ProfileRequest profileRequest, int idUser) {
        Optional<Profile> profileOptional = profileRepository.findByUserId(idUser);
        Optional<City> cityOptional = cityRepository.findByName(profileRequest.getCityLowerCase());
        BooleanResponse booleanResponse = new BooleanResponse(false);
        if (profileOptional.isPresent() && cityOptional.isPresent()) {
            Profile profile = profileOptional.get();
            profile.setName(profileRequest.getName());
            profile.setSurname(profileRequest.getSurname());
            profile.setCity(cityOptional.get());
            profile.setAge(profileRequest.getAge());
            booleanResponse.setBool(profileRepository.update(profile));
        }
        return booleanResponse;
    }

    @Transactional
    public BooleanResponse delete(int userId) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);
        if(profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            booleanResponse.setBool(profileRepository.delete(profile));
        }
        return booleanResponse;
    }

    public ProfileResponse findById(int id) {
        Optional<Profile> optional = profileRepository.findById(id);
        Profile profile = optional.orElseThrow();
        return mapperDto.profileToResponse(profile);
    }

    public ProfileResponse findByUserId(int userId) {
        Optional<Profile> optional = profileRepository.findByUserId(userId);
        Profile profile = optional.orElseThrow();
        return mapperDto.profileToResponse(profile);
    }
}
