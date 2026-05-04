package com.oskin.ad_board.service_test;

import com.oskin.ad_board.dto.request.ProfileRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.ProfileResponse;
import com.oskin.ad_board.model.City;
import com.oskin.ad_board.model.Profile;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.CityRepository;
import com.oskin.ad_board.repository.ProfileRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.service.ProfileService;
import com.oskin.ad_board.utils.MapperDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    @InjectMocks
    private ProfileService profileService;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private MapperDto mapperDto;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void save_WhenValidProfile_ShouldReturnTrue() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.of(new City()));
        Mockito.when(mapperDto.profileRequestToEntity(any(), any(), any())).thenReturn(new Profile());
        Mockito.when(profileRepository.create(any())).thenReturn(true);
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        BooleanResponse booleanResponse = profileService.save(profileRequest, 1);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void save_WhenFoundProfile_ShouldThrowException() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Assertions.assertThrows(EntityExistsException.class, () -> profileService.save(new ProfileRequest(), 1));
    }

    @Test
    void save_WhenNoFoundUser_ShouldThrowException() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> profileService.save(new ProfileRequest(), 1));
        Assertions.assertEquals("not found user with id = 1", exception.getMessage());
    }

    @Test
    void save_WhenNoFoundCity_ShouldThrowException() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.empty());
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Алжир");
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> profileService.save(profileRequest, 1));
        Assertions.assertEquals("not found city with name = Алжир", exception.getMessage());
    }

    @Test
    void update_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.of(new City()));
        Mockito.when(profileRepository.update(any())).thenReturn(true);
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        BooleanResponse booleanResponse = profileService.update(profileRequest, 1);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void update_WhenNoFoundProfile_ShouldThrowException() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> profileService.update(new ProfileRequest(), 1));
        Assertions.assertEquals("not found profile with user id = 1", exception.getMessage());
    }

    @Test
    void update_WhenNoFoundCity_ShouldThrowException() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.empty());
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> profileService.update(profileRequest, 1));
        Assertions.assertEquals("not found city with name = Москва", exception.getMessage());
    }

    @Test
    void delete_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(profileRepository.delete(any())).thenReturn(true);
        BooleanResponse booleanResponse = profileService.delete(1);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void delete_WhenNoFoundProfile_ShouldThrowException() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> profileService.delete(1));
    }

    @Test
    void findById_WhenValidId_ShouldReturnResponse() {
        Mockito.when(profileRepository.findById(anyInt())).thenReturn(Optional.of(new Profile()));
        ProfileResponse profileResponse = new ProfileResponse();
        Mockito.when(mapperDto.profileToResponse(any())).thenReturn(profileResponse);
        ProfileResponse verifyProfileResponse = profileService.findById(1);
        Assertions.assertEquals(profileResponse, verifyProfileResponse);
    }

    @Test
    void findById_WhenNoFoundProfile_ShouldThrowException() {
        Mockito.when(profileRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> profileService.findById(1));
    }

    @Test
    void findByUserId_WhenValidUserId_ShouldReturnResponse() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        ProfileResponse profileResponse = new ProfileResponse();
        Mockito.when(mapperDto.profileToResponse(any())).thenReturn(profileResponse);
        ProfileResponse verifyProfileResponse = profileService.findByUserId(1);
        Assertions.assertEquals(profileResponse, verifyProfileResponse);
    }

    @Test
    void findByUserId_WhenNoFoundProfile_ShouldThrowException() {
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> profileService.findByUserId(1));
    }
}
