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
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    public BooleanResponse save(ProfileRequest profileRequest, int userId) {
        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);
        if (profileOptional.isPresent()) {
            throw new EntityExistsException("Profile already exists with user id " + userId);
        }
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new EntityNotFoundException("not found user with id " + userId));
        Optional<City> cityOptional = cityRepository.findByName(profileRequest.getCityLowerCase());
        City city = cityOptional.orElseThrow(() -> new EntityNotFoundException("not found city with name " + profileRequest.getCity()));
        Profile profile = mapperDto.profileRequestToEntity(profileRequest, user, city);
        return new BooleanResponse(profileRepository.create(profile));
    }

    @Transactional
    public BooleanResponse update(ProfileRequest profileRequest, int userId) {
        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);
        Profile profile = profileOptional.orElseThrow(() -> new EntityNotFoundException("not found profile with user id " + userId));
        Optional<City> cityOptional = cityRepository.findByName(profileRequest.getCityLowerCase());
        City city = cityOptional.orElseThrow(() -> new EntityNotFoundException("not found city with name " + profileRequest.getCity()));
        profile.setName(profileRequest.getName());
        profile.setSurname(profileRequest.getSurname());
        profile.setCity(city);
        profile.setAge(profileRequest.getAge());
        return new BooleanResponse(profileRepository.update(profile));
    }

    @Transactional
    public BooleanResponse delete(int userId) {
        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);
        Profile profile = profileOptional.orElseThrow(() -> new EntityNotFoundException("not found profile with user id " + userId));
        return new BooleanResponse(profileRepository.delete(profile));

    }

    public ProfileResponse findById(int id) {
        Optional<Profile> optional = profileRepository.findById(id);
        Profile profile = optional.orElseThrow(() -> new EntityNotFoundException("not found profile with id " + id));
        return mapperDto.profileToResponse(profile);
    }

    public ProfileResponse findByUserId(int userId) {
        Optional<Profile> optional = profileRepository.findByUserId(userId);
        Profile profile = optional.orElseThrow(() -> new EntityNotFoundException("not found profile with user id " + userId));
        return mapperDto.profileToResponse(profile);
    }
}
