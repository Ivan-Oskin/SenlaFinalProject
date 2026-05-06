package com.oskin.ad_board.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.ad_board.controller.ProfileController;
import com.oskin.ad_board.dto.request.ProfileRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.ProfileResponse;
import com.oskin.ad_board.exception.GlobalExceptionHandler;
import com.oskin.ad_board.service.ProfileService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {
    @InjectMocks
    ProfileController profileController;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private ProfileService profileService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void findById_WhenValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(profileService.findById(anyInt())).thenReturn(new ProfileResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/profile/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findByUserId_WhenValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(profileService.findByUserId(anyInt())).thenReturn(new ProfileResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/profile/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void create_WhenValidRequest_ShouldReturnTrue() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        profileRequest.setAge(18);
        profileRequest.setName("Иван");
        profileRequest.setSurname("Иванов");
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(profileService.save(any(), anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void create_WhenNullName_ShouldReturnBadRequest() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        profileRequest.setAge(18);
        profileRequest.setName("");
        profileRequest.setSurname("Иванов");
        mockMvc.perform(MockMvcRequestBuilders.post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenNullSurname_ShouldReturnBadRequest() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        profileRequest.setAge(18);
        profileRequest.setName("Иван");
        profileRequest.setSurname("");
        mockMvc.perform(MockMvcRequestBuilders.post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenNoValidAge_ShouldReturnBadRequest() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        profileRequest.setAge(10);
        profileRequest.setName("Иван");
        profileRequest.setSurname("Иванов");
        mockMvc.perform(MockMvcRequestBuilders.post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenNullCity_ShouldReturnBadRequest() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("");
        profileRequest.setAge(18);
        profileRequest.setName("Иван");
        profileRequest.setSurname("Иванов");
        mockMvc.perform(MockMvcRequestBuilders.post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenValidRequest_ShouldReturnTrue() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        profileRequest.setAge(18);
        profileRequest.setName("Иван");
        profileRequest.setSurname("Иванов");
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(profileService.update(any(), anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void update_WhenNullName_ShouldReturnBadRequest() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        profileRequest.setAge(18);
        profileRequest.setName("");
        profileRequest.setSurname("Иванов");
        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNullSurname_ShouldReturnBadRequest() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        profileRequest.setAge(18);
        profileRequest.setName("Иван");
        profileRequest.setSurname("");
        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNoValidAge_ShouldReturnBadRequest() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("Москва");
        profileRequest.setAge(10);
        profileRequest.setName("Иван");
        profileRequest.setSurname("Иванов");
        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNullCity_ShouldReturnBadRequest() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setCity("");
        profileRequest.setAge(18);
        profileRequest.setName("Иван");
        profileRequest.setSurname("Иванов");
        mockMvc.perform(MockMvcRequestBuilders.put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_WhenValidRequest_ShouldReturnTrue() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(profileService.delete(anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.delete("/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }
}

