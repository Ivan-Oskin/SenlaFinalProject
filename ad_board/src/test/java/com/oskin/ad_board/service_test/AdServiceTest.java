package com.oskin.ad_board.service_test;

import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.request.GetAdRequest;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.PaginationAdModerationResponse;
import com.oskin.ad_board.dto.response.PaginationAdResponse;
import com.oskin.ad_board.exception.IdMatchException;
import com.oskin.ad_board.exception.StatusNoValidException;
import com.oskin.ad_board.model.*;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.CityRepository;
import com.oskin.ad_board.repository.ProfileRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.service.AdService;
import com.oskin.ad_board.utils.MapperDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class AdServiceTest {
    @InjectMocks
    private AdService adService;
    @Mock
    private AdRepository adRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private MapperDto mapperDto;
    @Mock
    private ProfileRepository profileRepository;

    private AdRequest adRequest;
    private Ad ad;
    private User seller;

    @BeforeEach
    void setUp() {
        adRequest = new AdRequest();
        adRequest.setCity("Москва");
        adRequest.setPrice(2000);
        adRequest.setTitle("test");
        ad = new Ad();
        seller = new User();
        seller.setId(2);
        ad.setSeller(seller);
        ad.setStatus(StatusAd.ACTIVE);
    }

    @Test
    void save_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.of(new City()));
        Mockito.when(mapperDto.adRequestToEntity(any(), any(), any())).thenReturn(new Ad());
        Mockito.when(adRepository.create(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.save(adRequest, 1);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void save_WhenNoFoundUser_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> adService.save(adRequest, 1));
        Assertions.assertEquals("not found user with id = 1", exception.getMessage());
    }

    @Test
    void save_WhenNoFoundProfile_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> adService.save(adRequest, 1));
        Assertions.assertEquals("not found profile with user id = 1", exception.getMessage());
    }

    @Test
    void save_WhenNotFoundCity_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> adService.save(adRequest, 1));
        Assertions.assertEquals("not found city with name = Москва", exception.getMessage());
    }

    @Test
    void update_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.of(new City()));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.update(adRequest, 1, 2);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void update_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> adService.update(adRequest, 1, 2));
        Assertions.assertEquals("not found ad with id = 1", exception.getMessage());
    }

    @Test
    void update_WhenNoFoundCity_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> adService.update(adRequest, 1, 2));
        Assertions.assertEquals("not found city with name = Москва", exception.getMessage());
    }

    @Test
    void update_WhenIdSellerNoMatchJwtId_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.of(new City()));
        Assertions.assertThrows(IdMatchException.class, () -> adService.update(adRequest, 1, 1));
    }

    @Test
    void update_WhenNoValidStatus_ShouldThrowException() {
        Ad adNoValid = new Ad();
        User user = new User();
        user.setId(2);
        adNoValid.setSeller(user);
        adNoValid.setCity(new City("тверь"));
        adNoValid.setStatus(StatusAd.RESERVED);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(adNoValid));
        Mockito.when(cityRepository.findByName(any())).thenReturn(Optional.of(new City()));
        Assertions.assertThrows(StatusNoValidException.class, () -> adService.update(adRequest, 1, 2));
    }

    @Test
    void delete_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(adRepository.delete(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.delete(1, 2);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void delete_WhenSellerIdNoMatchJwtId_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Assertions.assertThrows(IdMatchException.class, () -> adService.delete(1, 1));
    }

    @Test
    void delete_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> adService.delete(1, 2));
        Assertions.assertEquals("not found ad with id = 1", exception.getMessage());
    }

    @Test
    void findById_WhenValidRequest_ShouldReturnAdResponse() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        AdResponse adResponse = new AdResponse();
        Mockito.when(mapperDto.adToResponse(any())).thenReturn(adResponse);
        AdResponse verifyAdResponse = adService.findById(1);
        Assertions.assertEquals(adResponse, verifyAdResponse);
    }

    @Test
    void findById_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> adService.findById(1));
    }

    @Test
    void findByTitle_WhenValidRequest_ShouldReturnPaginationAdResponse() {
        GetAdRequest getAdRequest = new GetAdRequest();
        getAdRequest.setTitle("test");
        getAdRequest.setPage(1);
        Mockito.when(adRepository.searchByTitle(any())).thenReturn(Collections.singletonList(ad));
        Mockito.when(mapperDto.adToResponse(any())).thenReturn(new AdResponse());
        PaginationAdResponse paginationAdResponse = new PaginationAdResponse();
        Mockito.when(mapperDto.adToPaginationResponse(any(), anyInt())).thenReturn(paginationAdResponse);
        PaginationAdResponse verify = adService.findByTitle(getAdRequest);
        Assertions.assertEquals(paginationAdResponse, verify);
    }

    @Test
    void publishBySeller_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.publishBySeller(1, 2);
        Assertions.assertTrue(booleanResponse.isBool());

        ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
        Mockito.verify(adRepository).update(adCaptor.capture());
        Ad updatedAd = adCaptor.getValue();
        Assertions.assertEquals(StatusAd.MODERATION, updatedAd.getStatus());
    }

    @Test
    void publishBySeller_WhenIdNoMatchWithJwt_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Assertions.assertThrows(IdMatchException.class, () -> adService.publishBySeller(1, 1));
    }

    @Test
    void publishBySeller_WhenNoValidStatus_ShouldThrowException() {
        Ad adNoValid = new Ad();
        adNoValid.setStatus(StatusAd.COMPLETED);
        adNoValid.setSeller(seller);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(adNoValid));
        Assertions.assertThrows(StatusNoValidException.class, () -> adService.publishBySeller(1, 2));
    }

    @Test
    void publishBySeller_WhenNoFoundUser_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> adService.publishBySeller(1, 2));
        Assertions.assertEquals("not found user with id = 2", exception.getMessage());
    }

    @Test
    void publishBySeller_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> adService.publishBySeller(1, 2));
        Assertions.assertEquals("not found ad with id = 1", exception.getMessage());
    }

    @Test
    void archiveBySeller_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.archiveBySeller(1, 2);
        Assertions.assertTrue(booleanResponse.isBool());

        ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
        Mockito.verify(adRepository).update(adCaptor.capture());
        Ad updatedAd = adCaptor.getValue();
        Assertions.assertEquals(StatusAd.ARCHIVED, updatedAd.getStatus());
    }

    @Test
    void archiveBySeller_WhenNoFoundUser_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> adService.archiveBySeller(1, 2));
        Assertions.assertEquals("not found user with id = 2", exception.getMessage());
    }

    @Test
    void archiveBySeller_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> adService.archiveBySeller(1, 2));
        Assertions.assertEquals("not found ad with id = 1", exception.getMessage());
    }

    @Test
    void archiveBySeller_WhenIdNoMatchWithJwt_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Assertions.assertThrows(IdMatchException.class, () -> adService.archiveBySeller(1, 1));
    }

    @Test
    void archiveBySeller_WhenNoValidStatus_ShouldThrowException() {
        Ad adNoValid = new Ad();
        adNoValid.setStatus(StatusAd.COMPLETED);
        adNoValid.setSeller(seller);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(adNoValid));
        Assertions.assertThrows(StatusNoValidException.class, () -> adService.archiveBySeller(1, 2));
    }

    @Test
    void publishByModeration_WhenValidRequest_ShouldReturnTrue() {
        Ad validAd = new Ad();
        validAd.setStatus(StatusAd.MODERATION);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(validAd));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.publishAdByModeration(1);
        Assertions.assertTrue(booleanResponse.isBool());

        ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
        Mockito.verify(adRepository).update(adCaptor.capture());
        Ad updatedAd = adCaptor.getValue();
        Assertions.assertEquals(StatusAd.ACTIVE, updatedAd.getStatus());
    }

    @Test
    void publishByModeration_WhenNoValidStatus_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Assertions.assertThrows(StatusNoValidException.class, () -> adService.publishAdByModeration(1));
    }

    @Test
    void hideByModeration_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.hideAdByModeration(1);
        Assertions.assertTrue(booleanResponse.isBool());

        ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
        Mockito.verify(adRepository).update(adCaptor.capture());
        Ad updatedAd = adCaptor.getValue();
        Assertions.assertEquals(StatusAd.HIDDEN, updatedAd.getStatus());
    }

    @Test
    void hideByModeration_WhenNoValidStatus_ShouldThrowException() {
        Ad noValidAd = new Ad();
        noValidAd.setStatus(StatusAd.RESERVED);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(noValidAd));
        Assertions.assertThrows(StatusNoValidException.class, () -> adService.publishAdByModeration(1));
    }

    @Test
    void payAd_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.payAd(1);
        Assertions.assertTrue(booleanResponse.isBool());

        ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
        Mockito.verify(adRepository).update(adCaptor.capture());
        Ad updatedAd = adCaptor.getValue();
        Assertions.assertTrue(updatedAd.isPaid());
    }

    @Test
    void payAd_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> adService.payAd(1));
    }

    @Test
    void removePaidAd_WhenValidRequest_ShouldReturnTrue() {
        Ad adPaid = new Ad();
        adPaid.setPaid(true);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(adPaid));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = adService.removePaidAd(1);
        Assertions.assertTrue(booleanResponse.isBool());

        ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
        Mockito.verify(adRepository).update(adCaptor.capture());
        Ad updatedAd = adCaptor.getValue();
        Assertions.assertFalse(updatedAd.isPaid());
    }

    @Test
    void removePaidAd_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> adService.removePaidAd(1));
    }

    @Test
    void getListModeration_WhenValidRequest_ShouldReturnResponse() {
        Mockito.when(adRepository.findAllToModeration(any())).thenReturn(Collections.singletonList(ad));
        Mockito.when(mapperDto.adToResponse(any())).thenReturn(new AdResponse());
        PaginationAdModerationResponse response = new PaginationAdModerationResponse();
        Mockito.when(mapperDto.adToModerationPaginationResponse(any())).thenReturn(response);
        PaginationAdModerationResponse verifyResponse = adService.getModerationList(any());
        Assertions.assertEquals(response, verifyResponse);
    }
}
