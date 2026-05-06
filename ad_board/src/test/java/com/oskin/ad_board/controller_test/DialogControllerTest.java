package com.oskin.ad_board.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.ad_board.controller.DialogController;
import com.oskin.ad_board.dto.request.GetDialogRequest;
import com.oskin.ad_board.dto.request.MessageRequest;
import com.oskin.ad_board.dto.response.DialogResponse;
import com.oskin.ad_board.dto.response.MessageResponse;
import com.oskin.ad_board.exception.GlobalExceptionHandler;
import com.oskin.ad_board.service.DialogService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class DialogControllerTest {

    @InjectMocks
    DialogController dialogController;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private DialogService dialogService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(dialogController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void findDialog_WhenValidRequest_ShouldReturnOk() throws Exception {
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setCount(5);
        getDialogRequest.setAdId(1);
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(dialogService.getDialog(anyInt(), any())).thenReturn(new DialogResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/dialog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDialogRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void findDialog_WhenNoValidCount_ShouldReturnIsBadRequest() throws Exception {
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setCount(0);
        getDialogRequest.setAdId(1);
        mockMvc.perform(MockMvcRequestBuilders.get("/dialog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDialogRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findDialog_WhenNoValidAdId_ShouldReturnIsBadRequest() throws Exception {
        GetDialogRequest getDialogRequest = new GetDialogRequest();
        getDialogRequest.setCount(5);
        getDialogRequest.setAdId(0);
        mockMvc.perform(MockMvcRequestBuilders.get("/dialog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDialogRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendMessage_WhenValidRequest_ShouldReturnOk() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage("message");
        messageRequest.setAdId(1);
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(dialogService.sendMessage(anyInt(), any())).thenReturn(new MessageResponse());
        mockMvc.perform(MockMvcRequestBuilders.post("/dialog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void sendMessage_WhenNullMessage_ShouldReturnIsBadRequest() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage("");
        messageRequest.setAdId(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/dialog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendMessage_WhenNoValidAdId_ShouldReturnIsBadRequest() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage("message");
        messageRequest.setAdId(0);
        mockMvc.perform(MockMvcRequestBuilders.post("/dialog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isBadRequest());
    }
}
