package com.oskin.ad_board.Controller;

import com.oskin.ad_board.dto.request.UserRequest;
import com.oskin.ad_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reg")
    public boolean save(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }
}