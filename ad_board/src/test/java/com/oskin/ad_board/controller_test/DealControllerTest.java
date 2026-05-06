package com.oskin.ad_board.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.ad_board.controller.DealController;
import com.oskin.ad_board.dto.request.DealRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.exception.GlobalExceptionHandler;
import com.oskin.ad_board.service.DealService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class DealControllerTest {

    @InjectMocks
    private DealController dealController;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private DealService dealService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(dealController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void create_WhenValidRequest_ShouldReturnTrue() throws Exception {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setAdId(1);
        dealRequest.setBuyerId(1);
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(2);
        Mockito.when(dealService.save(any(), anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dealRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void create_WhenNoValidAdId_ShouldReturnBadRequest() throws Exception {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setAdId(0);
        dealRequest.setBuyerId(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dealRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenNoValidBuyerId_ShouldReturnBadRequest() throws Exception {
        DealRequest dealRequest = new DealRequest();
        dealRequest.setAdId(1);
        dealRequest.setBuyerId(0);
        mockMvc.perform(MockMvcRequestBuilders.post("/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dealRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHistoryBuy_WhenValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(dealService.getHistoryBuyByUser(anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/deal/history/buy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getHistorySell_WhenValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(dealService.getHistorySellByUser(anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/deal/history/sell")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void cancel_WhenValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(dealService.cancelDeal(1, 1)).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/deal/cancel/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void sold_WhenValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(dealService.soldDeal(1, 1)).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/deal/sold/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void return_WhenValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(dealService.returnDeal(1, 1)).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/deal/return/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }
}
