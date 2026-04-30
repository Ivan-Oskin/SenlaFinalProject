package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.GetDialogRequest;
import com.oskin.ad_board.dto.request.MessageRequest;
import com.oskin.ad_board.dto.response.DialogResponse;
import com.oskin.ad_board.service.DialogService;
import com.oskin.ad_board.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public DialogResponse findDialog(@RequestBody GetDialogRequest getDialogRequest) {
        int currentIdFromJwt = jwtUtils.getCurrentId();
        return dialogService.getDialog(currentIdFromJwt, getDialogRequest);
    }

    @PostMapping
    public DialogResponse sendMessage(@RequestBody MessageRequest messageRequest) {
        int currentIdFRomJwt = jwtUtils.getCurrentId();
        return dialogService.sendMessage(currentIdFRomJwt, messageRequest);
    }
}
