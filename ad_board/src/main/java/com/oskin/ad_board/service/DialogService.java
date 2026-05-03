package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.GetDialogRequest;
import com.oskin.ad_board.dto.request.MessageRequest;
import com.oskin.ad_board.dto.response.DialogResponse;
import com.oskin.ad_board.dto.response.MessageResponse;
import com.oskin.ad_board.exception.IdMatchException;
import com.oskin.ad_board.model.*;
import com.oskin.ad_board.repository.*;
import com.oskin.ad_board.utils.MapperDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DialogService {
    private final DialogRepository dialogRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MapperDto mapperDto;
    private final ProfileRepository profileRepository;

    @Autowired
    public DialogService(DialogRepository DialogRepository,
                         AdRepository adRepository,
                         UserRepository userRepository,
                         MessageRepository messageRepository,
                         MapperDto mapperDto,
                         ProfileRepository profileRepository) {
        this.dialogRepository = DialogRepository;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.mapperDto = mapperDto;
        this.profileRepository = profileRepository;
    }

    private int getCurrentBuyerId(int buyerId, Ad ad, int jwtId) {
        if (buyerId == 0) {
            return jwtId;
        } else if (ad.getSeller().getId() == jwtId) {
            return buyerId;
        } else {
            throw new IdMatchException("the buyers's ID does not match the jwt's ID");
        }
    }

    @Transactional
    public DialogResponse getDialog(int currentJwtId, GetDialogRequest getDialogRequest) {
        int adId = getDialogRequest.getAdId();
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id: " + adId));
        int buyerId = getCurrentBuyerId(getDialogRequest.getBuyerId(), ad, currentJwtId);
        Optional<User> buyerOptional = userRepository.findById(buyerId);
        User buyer = buyerOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id: " + buyerId));
        return getDialogResponse(buyer, ad, getDialogRequest);
    }

    private Dialog createDialog(Ad ad, User user) {
        Dialog dialog = new Dialog();
        dialog.setBuyer(user);
        dialog.setAd(ad);
        return dialogRepository.createAndGet(dialog);
    }

    private DialogResponse getDialogResponse(User buyer, Ad ad, GetDialogRequest getDialogRequest) {
        Optional<Dialog> dialogOptional = dialogRepository.findByAdAndBuyer(ad.getId(), buyer.getId());
        if (dialogOptional.isPresent()) {
            Dialog dialog = dialogOptional.get();
            List<Tuple> messages = messageRepository.getMessagesByDialog(dialog.getId(), getDialogRequest);
            List<MessageResponse> messageResponses = messages.stream().map(mapperDto::messageToResponse).toList();
            return mapperDto.dialogToResponse(dialog, messageResponses);
        } else {
            Dialog dialog = createDialog(ad, buyer);
            return mapperDto.dialogToResponse(dialog, Collections.emptyList());
        }
    }

    @Transactional
    public MessageResponse sendMessage(int senderId, MessageRequest messageRequest) {
        int adId = messageRequest.getAdId();
        Optional<Ad> adOptional = adRepository.findById(adId);
        Ad ad = adOptional.orElseThrow(() -> new EntityNotFoundException("not found ad with id " + adId));
        Optional<User> userOptional = userRepository.findById(senderId);
        User sender = userOptional.orElseThrow(() -> new EntityNotFoundException("not found sender with id " + senderId));
        Optional<Profile> profileOptional = profileRepository.findByUserId(senderId);
        profileOptional.orElseThrow(() -> new EntityNotFoundException("not found profile with user id " + senderId));
        Dialog dialog;
        int buyerId = getCurrentBuyerId(messageRequest.getBuyerId(), ad, senderId);
        if (buyerId != senderId) {
            Optional<User> buyerOptional = userRepository.findById(buyerId);
            User currentBuyer = buyerOptional.orElseThrow(() -> new EntityNotFoundException("not found user with id "+buyerId));
            dialog = dialogRepository.findByAdAndBuyer(adId, buyerId).orElseGet(() -> createDialog(ad, currentBuyer));
        } else {
            dialog = dialogRepository.findByAdAndBuyer(adId, senderId).orElseGet(() -> createDialog(ad, sender));
        }
        Message message = mapperDto.messageRequestToEntity(messageRequest, sender, dialog);
        message = messageRepository.createAndGet(message);
        return mapperDto.messageToResponse(message, profileOptional.get());
    }
}
