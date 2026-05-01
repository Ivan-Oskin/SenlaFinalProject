package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.GetDialogRequest;
import com.oskin.ad_board.dto.request.MessageRequest;
import com.oskin.ad_board.dto.response.DialogResponse;
import com.oskin.ad_board.dto.response.MessageResponse;
import com.oskin.ad_board.model.*;
import com.oskin.ad_board.repository.*;
import com.oskin.ad_board.utils.MapperDto;
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
            return 0;
        }
    }

    @Transactional
    public DialogResponse getDialog(int currentJwtId, GetDialogRequest getDialogRequest) {
        int adId = getDialogRequest.getAdId();
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            int buyerId = getCurrentBuyerId(getDialogRequest.getBuyerId(), ad, currentJwtId);
            Optional<User> buyerOptional = userRepository.findById(buyerId);
            if (buyerOptional.isPresent()) {
                User buyer = buyerOptional.get();
                return getDialogResponse(buyer, ad, getDialogRequest);
            }
        }
        return null;
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
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            Optional<User> userOptional = userRepository.findById(senderId);
            Optional<Profile> profileOptional = profileRepository.findByUserId(senderId);
            if (userOptional.isPresent() && profileOptional.isPresent()) {
                User sender = userOptional.get();
                Dialog dialog = new Dialog();
                User buyer = new User();
                int buyerId = getCurrentBuyerId(messageRequest.getBuyerId(), ad, senderId);
                if (buyerId == 0) return null; //throw exception
                if (buyerId != senderId) {
                    Optional<User> buyerOptional = userRepository.findById(buyerId);
                    if (buyerOptional.isPresent()) {
                        User currentBuyer = buyerOptional.get();
                        dialog = dialogRepository.findByAdAndBuyer(adId, buyerId).orElseGet(() -> createDialog(ad, currentBuyer));
                        buyer = currentBuyer;
                    }
                } else {
                    dialog = dialogRepository.findByAdAndBuyer(adId, senderId).orElseGet(() -> createDialog(ad, sender));
                    buyer = sender;
                }
                Message message = mapperDto.messageRequestToEntity(messageRequest, sender, dialog);
                message = messageRepository.createAndGet(message);
                return mapperDto.messageToResponse(message, profileOptional.get());
            }
        }
        return null;
    }
}
