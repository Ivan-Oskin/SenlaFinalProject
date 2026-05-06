package com.oskin.ad_board.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.ad_board.controller.UserController;
import com.oskin.ad_board.dto.request.UserRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.exception.GlobalExceptionHandler;
import com.oskin.ad_board.security.UserDetailService;
import com.oskin.ad_board.service.UserService;
import com.oskin.ad_board.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailService userDetailService;
    @Mock
    private JwtUtils jwtUtils;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private UserDetails userDetails;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        userRequest = new UserRequest();
        userRequest.setMail("test@mail.com");
        userRequest.setPassword("test_password");
        userDetails = User.builder()
                .username(userRequest.getMail())
                .password(userRequest.getPassword()).build();
    }

    @Test
    void save_whenValidRequest_ShouldReturnJwtToken() throws Exception {
        Mockito.when(userService.save(any())).thenReturn(new BooleanResponse(true));
        Mockito.when(userDetailService.loadUserByUsername(any())).thenReturn(userDetails);
        String jwt = "secret_token_jwt";
        Mockito.when(jwtUtils.generateToken(any())).thenReturn(jwt);
        mockMvc.perform(MockMvcRequestBuilders.post("/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwt));
    }

    @Test
    void save_WhenReturnFalse_ShouldReturnInternalServerError() throws Exception {
        Mockito.when(userService.save(any())).thenReturn(new BooleanResponse(false));
        mockMvc.perform(MockMvcRequestBuilders.post("/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void save_whenNoValidMail_ShouldReturnConflict() throws Exception {
        UserRequest noValidRequest = new UserRequest();
        noValidRequest.setMail("is bad mail");
        noValidRequest.setPassword("goodPassword");
        mockMvc.perform(MockMvcRequestBuilders.post("/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noValidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void save_whenNoValidPassword_ShouldReturnConflict() throws Exception {
        UserRequest noValidRequest = new UserRequest();
        noValidRequest.setMail("good@mail.ru");
        noValidRequest.setPassword("bad");
        mockMvc.perform(MockMvcRequestBuilders.post("/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noValidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void auth_WhenValidRequest() throws Exception {
        Mockito.when(userDetailService.loadUserByUsername(any())).thenReturn(userDetails);
        String jwt = "secret_token_jwt";
        Mockito.when(jwtUtils.generateToken(any())).thenReturn(jwt);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwt));
    }

    @Test
    void auth_whenNoValidMail_ShouldReturnConflict() throws Exception {
        UserRequest noValidRequest = new UserRequest();
        noValidRequest.setMail("is bad mail");
        noValidRequest.setPassword("goodPassword");
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noValidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void auth_whenNoValidPassword_ShouldReturnConflict() throws Exception {
        UserRequest noValidRequest = new UserRequest();
        noValidRequest.setMail("good@mail.ru");
        noValidRequest.setPassword("bad");
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noValidRequest)))
                .andExpect(status().isBadRequest());
    }
}
