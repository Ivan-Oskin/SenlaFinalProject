package com.oskin.ad_board.service_test;

import com.oskin.ad_board.dto.request.UserRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.exception.IdMatchException;
import com.oskin.ad_board.model.Review;
import com.oskin.ad_board.model.Role;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension .class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void save_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(userRepository.findByMail(any())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(any())).thenReturn("password Encode");
        Mockito.when(userRepository.create(any())).thenReturn(true);
        BooleanResponse booleanResponse = userService.save(new UserRequest());
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void save_WhenUserAlreadyExist_ShouldThrowException() {
        Mockito.when(userRepository.findByMail(any())).thenReturn(Optional.of(new User()));
        Assertions.assertThrows(EntityExistsException.class, () -> userService.save(new UserRequest()));
    }
}
