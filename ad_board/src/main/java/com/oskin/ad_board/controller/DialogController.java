package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.GetDialogRequest;
import com.oskin.ad_board.dto.request.MessageRequest;
import com.oskin.ad_board.dto.response.DialogResponse;
import com.oskin.ad_board.dto.response.MessageResponse;
import com.oskin.ad_board.service.DialogService;
import com.oskin.ad_board.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/dialog")
public class DialogController {
    private final DialogService dialogService;
    private final JwtUtils jwtUtils;

    @Autowired
    DialogController(DialogService dialogService, JwtUtils jwtUtils) {
        this.dialogService = dialogService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public DialogResponse findDialog(@RequestBody @Valid GetDialogRequest getDialogRequest) {
        int currentIdFromJwt = jwtUtils.getCurrentId();
        return dialogService.getDialog(currentIdFromJwt, getDialogRequest);
    }

    @PostMapping
    public MessageResponse sendMessage(@RequestBody @Valid MessageRequest messageRequest) {
        int currentIdFRomJwt = jwtUtils.getCurrentId();
        return dialogService.sendMessage(currentIdFRomJwt, messageRequest);
    }
}
