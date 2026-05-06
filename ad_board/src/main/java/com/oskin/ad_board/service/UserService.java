package com.oskin.ad_board.service;

import com.oskin.ad_board.dto.request.UserRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.model.Role;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        String mail = userRequest.getMail();
        Optional<User> userOptional = userRepository.findByMail(mail);
        if (userOptional.isPresent()) {
            throw new EntityExistsException("User already exists with mail = " + mail);
        }
        String password = passwordEncoder.encode(userRequest.getPassword());
        Role role = new Role();
        role.setStandardUser();
        User user = new User(mail, password, role);
        return new BooleanResponse(userRepository.create(user));
    }
}
