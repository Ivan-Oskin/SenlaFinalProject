package com.oskin.ad_board.service_test;

import com.oskin.ad_board.dto.request.DealRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.DealResponse;
import com.oskin.ad_board.exception.IdMatchException;
import com.oskin.ad_board.exception.StatusNoValidException;
import com.oskin.ad_board.model.*;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.DealRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.service.DealService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {
    @InjectMocks
    private DealService dealService;
    @Mock
    private DealRepository dealRepository;
    @Mock
    private AdRepository adRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MapperDto mapperDto;

    private User buyer;
    private User seller;
    private Ad ad;

    @BeforeEach
    void setUp() {
        buyer = new User();
        buyer.setId(1);
        seller = new User();
        seller.setId(2);
        ad = new Ad();
        ad.setId(1);
        ad.setSeller(seller);
        ad.setStatus(StatusAd.ACTIVE);
    }

    @Test
    void getHistoryBuyByUser_WhenValidId_ShouldReturnList() {
        int id = 1;
        Mockito.when(dealRepository.getHistory(id, false)).thenReturn(Collections.singletonList(new Deal()));
        DealResponse dealResponse = new DealResponse();
        List<DealResponse> dealResponses = Collections.singletonList(dealResponse);
        Mockito.when(mapperDto.dealToResponse(any())).thenReturn(dealResponse);
        List<DealResponse> verifyDealResponses = dealService.getHistoryBuyByUser(id);
        Assertions.assertEquals(dealResponses, verifyDealResponses);
    }

    @Test
    void getHistorySellByUser_WhenValidId_ShouldReturnList() {
        int id = 1;
        Mockito.when(dealRepository.getHistory(id, true)).thenReturn(Collections.singletonList(new Deal()));
        DealResponse dealResponse = new DealResponse();
        List<DealResponse> dealResponses = Collections.singletonList(dealResponse);
        Mockito.when(mapperDto.dealToResponse(any())).thenReturn(dealResponse);
        List<DealResponse> verifyDealResponses = dealService.getHistorySellByUser(id);
        Assertions.assertEquals(dealResponses, verifyDealResponses);
    }

    @Test
    void save_WhenValidRequest_ShouldReturnTrue() {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setBuyerId(1);
        dealRequest.setAdId(1);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(buyer));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        Mockito.when(dealRepository.create(any())).thenReturn(true);
        BooleanResponse booleanResponse = dealService.save(dealRequest, 2);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void save_WhenNoFoundSeller_ShouldThrowException() {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setBuyerId(1);
        dealRequest.setAdId(1);
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dealService.save(dealRequest, 2));
        Assertions.assertEquals("not found user with id = 2", exception.getMessage());
    }

    @Test
    void save_WhenNoFoundAd_ShouldThrowException() {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setBuyerId(1);
        dealRequest.setAdId(1);
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dealService.save(dealRequest, 2));
        Assertions.assertEquals("not found ad with id = 1", exception.getMessage());
    }

    @Test
    void save_WhenNoFoundBuyer_ShouldThrowException() {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setBuyerId(1);
        dealRequest.setAdId(1);
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dealService.save(dealRequest, 2));
        Assertions.assertEquals("not found user with id = 1", exception.getMessage());
    }

    @Test
    void save_WhenSellerMatchBuyer_ShouldThrowException() {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setBuyerId(1);
        dealRequest.setAdId(1);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(seller));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Exception exception = Assertions.assertThrows(IdMatchException.class,
                () -> dealService.save(dealRequest, 1));
        Assertions.assertEquals("the buyer and seller have the same ID", exception.getMessage());
    }

    @Test
    void save_WhenSellerIdNoMatchIdFromJwt_ShouldThrowException() {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setBuyerId(1);
        dealRequest.setAdId(1);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(buyer));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        User noValidSeller = new User();
        noValidSeller.setId(3);
        Mockito.when(userRepository.findById(3)).thenReturn(Optional.of(noValidSeller));
        Exception exception = Assertions.assertThrows(IdMatchException.class,
                () -> dealService.save(dealRequest, 3));
        Assertions.assertEquals("the user's ID does not match the seller's ID", exception.getMessage());
    }

    @Test
    void save_WhenNoValidStatus_ShouldThrowException() {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setBuyerId(1);
        dealRequest.setAdId(1);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(buyer));
        Ad noValidAd = new Ad();
        noValidAd.setStatus(StatusAd.RESERVED);
        noValidAd.setId(1);
        noValidAd.setSeller(seller);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(noValidAd));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(seller));
        Assertions.assertThrows(StatusNoValidException.class,
                () -> dealService.save(dealRequest, 2));
    }

    @Test
    void cancelDeal_WhenValidRequestBuyer_ShouldReturnTrue() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.CREATED);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(buyer));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        Mockito.when(dealRepository.update(deal)).thenReturn(true);
        BooleanResponse booleanResponse = dealService.cancelDeal(1, 1);
        Assertions.assertTrue(booleanResponse.isBool());
        ArgumentCaptor<Deal> dealCaptor = ArgumentCaptor.forClass(Deal.class);
        Mockito.verify(dealRepository).update(dealCaptor.capture());
        Deal capturedDeal = dealCaptor.getValue();
        Assertions.assertEquals(StatusDeal.CANCELED, capturedDeal.getStatus());
    }

    @Test
    void cancelDeal_WhenValidRequestSeller_ShouldReturnTrue() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.CREATED);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        Mockito.when(dealRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = dealService.cancelDeal(1, 1);
        Assertions.assertTrue(booleanResponse.isBool());
        ArgumentCaptor<Deal> dealCaptor = ArgumentCaptor.forClass(Deal.class);
        Mockito.verify(dealRepository).update(dealCaptor.capture());
        Deal capturedDeal = dealCaptor.getValue();
        Assertions.assertEquals(StatusDeal.CANCELED, capturedDeal.getStatus());
    }

    @Test
    void cancelDeal_WhenNoValidStatus_ShouldThrowException() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.RETURNED);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Assertions.assertThrows(StatusNoValidException.class, () -> dealService.cancelDeal(1, 1));
    }

    @Test
    void soldDeal_WhenValidRequestBuyer_ShouldThrowException() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.CREATED);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(buyer));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Assertions.assertThrows(IdMatchException.class, () -> dealService.soldDeal(1, 1));
    }

    @Test
    void soldDeal_WhenValidRequestSeller_ShouldReturnTrue() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.CREATED);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        Mockito.when(dealRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = dealService.soldDeal(1, 1);
        Assertions.assertTrue(booleanResponse.isBool());
        ArgumentCaptor<Deal> dealCaptor = ArgumentCaptor.forClass(Deal.class);
        Mockito.verify(dealRepository).update(dealCaptor.capture());
        Deal capturedDeal = dealCaptor.getValue();
        Assertions.assertEquals(StatusDeal.SOLD, capturedDeal.getStatus());
    }

    @Test
    void soldDeal_WhenNoValidStatus_ShouldThrowException() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.RETURNED);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Assertions.assertThrows(StatusNoValidException.class, () -> dealService.soldDeal(1, 1));
    }

    @Test
    void returnDeal_WhenValidRequestBuyer_ShouldThrowException() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.SOLD);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(buyer));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Assertions.assertThrows(IdMatchException.class, () -> dealService.returnDeal(1, 1));
    }

    @Test
    void returnDeal_WhenValidRequestSeller_ShouldReturnTrue() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.SOLD);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Mockito.when(adRepository.update(any())).thenReturn(true);
        Mockito.when(dealRepository.update(any())).thenReturn(true);
        BooleanResponse booleanResponse = dealService.returnDeal(1, 1);
        Assertions.assertTrue(booleanResponse.isBool());
        ArgumentCaptor<Deal> dealCaptor = ArgumentCaptor.forClass(Deal.class);
        Mockito.verify(dealRepository).update(dealCaptor.capture());
        Deal capturedDeal = dealCaptor.getValue();
        Assertions.assertEquals(StatusDeal.RETURNED, capturedDeal.getStatus());
    }

    @Test
    void returnDeal_WhenNoValidStatus_ShouldThrowException() {
        Deal deal = new Deal();
        deal.setBuyer(buyer);
        deal.setAd(ad);
        deal.setStatus(StatusDeal.RETURNED);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(dealRepository.findById(anyInt())).thenReturn(Optional.of(deal));
        Assertions.assertThrows(StatusNoValidException.class, () -> dealService.returnDeal(1, 1));
    }
}
