package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.UserRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.model.Role;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public BooleanResponse save(UserRequest userRequest) {
        BooleanResponse booleanResponse = new BooleanResponse(false);
        String mail = userRequest.getMail();
        String password = passwordEncoder.encode(userRequest.getPassword());
        Role role = new Role();
        role.setStandardUser();
        User user = new User(mail, password, role);
        booleanResponse.setBool(userRepository.create(user));
        return booleanResponse;
    }
}
