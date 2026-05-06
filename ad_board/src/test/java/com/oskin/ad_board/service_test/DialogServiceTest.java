package com.oskin.ad_board.service_test;

import com.oskin.ad_board.dto.request.GetDialogRequest;
import com.oskin.ad_board.dto.request.MessageRequest;
import com.oskin.ad_board.dto.response.DialogResponse;
import com.oskin.ad_board.dto.response.MessageResponse;
import com.oskin.ad_board.exception.IdMatchException;
import com.oskin.ad_board.model.*;
import com.oskin.ad_board.repository.*;
import com.oskin.ad_board.service.DialogService;
import com.oskin.ad_board.utils.MapperDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class DialogServiceTest {
    @InjectMocks
    private DialogService dialogService;
    @Mock
    private DialogRepository dialogRepository;
    @Mock
    private AdRepository adRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MapperDto mapperDto;
    @Mock
    private ProfileRepository profileRepository;
    private Ad ad;
    private User buyer;
    private Dialog dialog;
    private User seller;

    @BeforeEach
    void setUp() {
        buyer = new User();
        buyer.setId(1);
        seller = new User();
        seller.setId(2);
        City city = new City();
        city.setName("Москва");
        ad = new Ad();
        ad.setSeller(seller);
        ad.setPaid(false);
        ad.setCity(city);
        ad.setPrice(2000);
        ad.setTitle("test");
        ad.setStatus(StatusAd.ACTIVE);
        ad.setId(1);
        dialog = new Dialog();
        dialog.setAd(ad);
        dialog.setBuyer(buyer);
    }

    @Test
    void getDialog_WhenBuyerRequest_ShouldReturnDialog() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setBuyerId(0);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(buyer));
        Mockito.when(dialogRepository.findByAdAndBuyer(anyInt(), anyInt())).thenReturn(Optional.of(dialog));
        Tuple tuple = mock(Tuple.class);
        Mockito.when(messageRepository.getMessagesByDialog(anyInt(), any())).thenReturn(List.of(tuple));
        Mockito.when(mapperDto.messageToResponse(any())).thenReturn(new MessageResponse());
        DialogResponse dialogResponse = new DialogResponse();
        Mockito.when(mapperDto.dialogToResponse(any(), any())).thenReturn(dialogResponse);
        DialogResponse verifyDialogResponse = dialogService.getDialog(1, getDialogRequest);
        Assertions.assertEquals(dialogResponse, verifyDialogResponse);
    }

    @Test
    void getDialog_WhenSellerRequest_ShouldReturnDialog() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setBuyerId(1);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(buyer));
        Mockito.when(dialogRepository.findByAdAndBuyer(anyInt(), anyInt())).thenReturn(Optional.of(dialog));
        Tuple tuple = mock(Tuple.class);
        Mockito.when(messageRepository.getMessagesByDialog(anyInt(), any())).thenReturn(List.of(tuple));
        Mockito.when(mapperDto.messageToResponse(any())).thenReturn(new MessageResponse());
        DialogResponse dialogResponse = new DialogResponse();
        Mockito.when(mapperDto.dialogToResponse(any(), any())).thenReturn(dialogResponse);
        DialogResponse verifyDialogResponse = dialogService.getDialog(2, getDialogRequest);
        Assertions.assertEquals(dialogResponse, verifyDialogResponse);
    }

    @Test
    void getDialog_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setAdId(1);
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dialogService.getDialog(1, getDialogRequest));
        Assertions.assertEquals("not found ad with id = 1", exception.getMessage());
    }

    @Test
    void getDialog_NoValidSellerId_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setBuyerId(1);
        Assertions.assertThrows(IdMatchException.class, () -> dialogService.getDialog(3, getDialogRequest));
    }

    @Test
    void getDialog_WhenNoFoundBuyer_ShouldThrowException() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setBuyerId(1);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dialogService.getDialog(2, getDialogRequest));
        Assertions.assertEquals("not found user with id = 1", exception.getMessage());
    }

    @Test
    void getDialog_WhenNoFoundDialog_ShouldReturnDialog() {
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setBuyerId(1);
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(buyer));
        Mockito.when(dialogRepository.findByAdAndBuyer(anyInt(), anyInt())).thenReturn(Optional.empty());
        Mockito.when(dialogRepository.createAndGet(any())).thenReturn(dialog);
        DialogResponse dialogResponse = new DialogResponse();
        Mockito.when(mapperDto.dialogToResponse(dialog, Collections.emptyList())).thenReturn(dialogResponse);
        DialogResponse verifyDialogResponse = dialogService.getDialog(2, getDialogRequest);
        Assertions.assertEquals(dialogResponse, verifyDialogResponse);
    }

    @Test
    void sendMessage_WhenSenderIsBuyer_shouldReturnMessage() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBuyerId(0);
        messageRequest.setAdId(1);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(buyer));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(dialogRepository.findByAdAndBuyer(anyInt(), anyInt())).thenReturn(Optional.of(dialog));
        Message message = new Message();
        Mockito.when(mapperDto.messageRequestToEntity(any(), any(), any())).thenReturn(message);
        Mockito.when(messageRepository.createAndGet(any())).thenReturn(message);
        MessageResponse messageResponse = new MessageResponse();
        Mockito.when(mapperDto.messageToResponse(any(), any())).thenReturn(messageResponse);
        MessageResponse verifyMessageResponse = dialogService.sendMessage(1, messageRequest);
        Assertions.assertEquals(messageResponse, verifyMessageResponse);
    }

    @Test
    void sendMessage_WhenSenderIsSeller_shouldReturnMessage() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBuyerId(1);
        messageRequest.setAdId(1);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(seller));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(buyer));
        Mockito.when(dialogRepository.findByAdAndBuyer(anyInt(), anyInt())).thenReturn(Optional.of(dialog));
        Message message = new Message();
        Mockito.when(mapperDto.messageRequestToEntity(any(), any(), any())).thenReturn(message);
        Mockito.when(messageRepository.createAndGet(any())).thenReturn(message);
        MessageResponse messageResponse = new MessageResponse();
        Mockito.when(mapperDto.messageToResponse(any(), any())).thenReturn(messageResponse);
        MessageResponse verifyMessageResponse = dialogService.sendMessage(2, messageRequest);
        Assertions.assertEquals(messageResponse, verifyMessageResponse);
    }

    @Test
    void sendMessage_WhenSenderIsNotSellerAndNotBuyer_shouldThrowException() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBuyerId(1);
        messageRequest.setAdId(1);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Assertions.assertThrows(IdMatchException.class, () -> dialogService.sendMessage(3, messageRequest));
    }

    @Test
    void sendMessage_WhenNoFoundDialog_shouldReturnMessage() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBuyerId(1);
        messageRequest.setAdId(1);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(seller));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(buyer));
        Mockito.when(dialogRepository.findByAdAndBuyer(anyInt(), anyInt())).thenReturn(Optional.empty());
        Mockito.when(dialogRepository.createAndGet(any())).thenReturn(dialog);
        Message message = new Message();
        Mockito.when(mapperDto.messageRequestToEntity(any(), any(), any())).thenReturn(message);
        Mockito.when(messageRepository.createAndGet(any())).thenReturn(message);
        MessageResponse messageResponse = new MessageResponse();
        Mockito.when(mapperDto.messageToResponse(any(), any())).thenReturn(messageResponse);
        MessageResponse verifyMessageResponse = dialogService.sendMessage(2, messageRequest);
        Assertions.assertEquals(messageResponse, verifyMessageResponse);
    }

    @Test
    void sendMessage_WhenNoFoundAd_shouldThrowException() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBuyerId(1);
        messageRequest.setAdId(1);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dialogService.sendMessage(1, messageRequest));
        Assertions.assertEquals("not found ad with id = 1", exception.getMessage());
    }

    @Test
    void sendMessage_WhenNoFoundSeller_shouldThrowException() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBuyerId(1);
        messageRequest.setAdId(1);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dialogService.sendMessage(2, messageRequest));
        Assertions.assertEquals("not found user with id = 2", exception.getMessage());
    }

    @Test
    void sendMessage_WhenNoFoundProfile_shouldThrowException() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBuyerId(1);
        messageRequest.setAdId(1);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(seller));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dialogService.sendMessage(2, messageRequest));
        Assertions.assertEquals("not found profile with user id = 2", exception.getMessage());
    }

    @Test
    void sendMessage_WhenNoFoundBuyer_shouldThrowException() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setBuyerId(1);
        messageRequest.setAdId(1);
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(seller));
        Mockito.when(profileRepository.findByUserId(anyInt())).thenReturn(Optional.of(new Profile()));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> dialogService.sendMessage(2, messageRequest));
        Assertions.assertEquals("not found user with id = 1", exception.getMessage());
    }
}
