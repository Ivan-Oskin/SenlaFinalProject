package com.oskin.ad_board.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.ad_board.controller.ModerationController;
import com.oskin.ad_board.dto.request.GetAdToModeration;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.PaginationAdModerationResponse;
import com.oskin.ad_board.exception.GlobalExceptionHandler;
import com.oskin.ad_board.service.AdService;
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
public class ModerationControllerTest {
    @InjectMocks
    private ModerationController moderationController;
    @Mock
    private AdService adService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(moderationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getModerationList_WhenValidRequest_ShouldReturnOk() throws Exception {
        GetAdToModeration getAdToModeration = new GetAdToModeration();
        getAdToModeration.setCount(5);
        Mockito.when(adService.getModerationList(any())).thenReturn(new PaginationAdModerationResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/moderation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getAdToModeration)))
                .andExpect(status().isOk());
    }

    @Test
    void getModerationList_WhenNoValidCount_ShouldReturnBadRequest() throws Exception {
        GetAdToModeration getAdToModeration = new GetAdToModeration();
        getAdToModeration.setCount(0);
        mockMvc.perform(MockMvcRequestBuilders.get("/moderation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getAdToModeration)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pay_WhenValidId_ShouldReturnTrue() throws Exception {
        Mockito.when(adService.payAd(anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/moderation/pay/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void removePaid_WhenValidId_ShouldReturnTrue() throws Exception {
        Mockito.when(adService.removePaidAd(anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/moderation/remove_paid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void publish_WhenValidId_ShouldReturnTrue() throws Exception {
        Mockito.when(adService.publishAdByModeration(anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/moderation/publish/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void hide_WhenValidId_ShouldReturnTrue() throws Exception {
        Mockito.when(adService.hideAdByModeration(anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/moderation/hide/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }
}
