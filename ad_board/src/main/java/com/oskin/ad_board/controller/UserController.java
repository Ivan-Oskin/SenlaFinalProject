package com.oskin.ad_board.controller;

import com.oskin.ad_board.dto.request.UserRequest;
import com.oskin.ad_board.dto.response.JwtResponse;
import com.oskin.ad_board.security.UserDetailService;
import com.oskin.ad_board.service.UserService;
import com.oskin.ad_board.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, UserDetailService userDetailService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/reg")
    public boolean save(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @PostMapping("/auth")
    public JwtResponse createAuthToken(@RequestBody UserRequest userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getMail(), userRequest.getPassword()));
        UserDetails userDetails = userDetailService.loadUserByUsername(userRequest.getMail());
        String token = jwtUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }
}