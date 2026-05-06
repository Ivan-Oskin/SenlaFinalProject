package com.oskin.ad_board.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.ad_board.controller.AdController;
import com.oskin.ad_board.dto.request.AdRequest;
import com.oskin.ad_board.dto.request.GetAdRequest;
import com.oskin.ad_board.dto.response.AdResponse;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.PaginationAdResponse;
import com.oskin.ad_board.exception.GlobalExceptionHandler;
import com.oskin.ad_board.model.AdSortType;
import com.oskin.ad_board.service.AdService;
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
public class AdControllerTest {
    @InjectMocks
    private AdController adController;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AdService adService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(adController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void create_WhenValidRequest_ShouldReturnTrue() throws Exception {
        AdRequest adRequest = new AdRequest();
        adRequest.setTitle("title");
        adRequest.setCity("city");
        adRequest.setPrice(2000);
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(2);
        Mockito.when(adService.save(any(), anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/ad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void create_WhenNullTitle_ShouldReturnBadRequest() throws Exception {
        AdRequest adRequest = new AdRequest();
        adRequest.setTitle("");
        adRequest.setCity("city");
        adRequest.setPrice(2000);
        mockMvc.perform(MockMvcRequestBuilders.post("/ad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenNullCity_ShouldReturnBadRequest() throws Exception {
        AdRequest adRequest = new AdRequest();
        adRequest.setTitle("title");
        adRequest.setCity("");
        adRequest.setPrice(2000);
        mockMvc.perform(MockMvcRequestBuilders.post("/ad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenNoValidPrice_ShouldReturnBadRequest() throws Exception {
        AdRequest adRequest = new AdRequest();
        adRequest.setTitle("title");
        adRequest.setCity("city");
        adRequest.setPrice(-1);
        mockMvc.perform(MockMvcRequestBuilders.post("/ad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenValidRequest_ShouldReturnTrue() throws Exception {
        AdRequest adRequest = new AdRequest();
        adRequest.setTitle("title");
        adRequest.setCity("city");
        adRequest.setPrice(2000);
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(2);
        Mockito.when(adService.update(any(), anyInt(), anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/ad/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void update_WhenNullTitle_ShouldReturnBadRequest() throws Exception {
        AdRequest adRequest = new AdRequest();
        adRequest.setTitle("");
        adRequest.setCity("city");
        adRequest.setPrice(2000);
        mockMvc.perform(MockMvcRequestBuilders.put("/ad/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNullCity_ShouldReturnBadRequest() throws Exception {
        AdRequest adRequest = new AdRequest();
        adRequest.setTitle("title");
        adRequest.setCity("");
        adRequest.setPrice(2000);
        mockMvc.perform(MockMvcRequestBuilders.put("/ad/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNoValidPrice_ShouldReturnBadRequest() throws Exception {
        AdRequest adRequest = new AdRequest();
        adRequest.setTitle("title");
        adRequest.setCity("city");
        adRequest.setPrice(-1);
        mockMvc.perform(MockMvcRequestBuilders.put("/ad/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_WhenValidRequest_ShouldReturnTrue() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(2);
        Mockito.when(adService.delete(1, 2)).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.delete("/ad/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void publish_WhenValidRequest_ShouldReturnTrue() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(2);
        Mockito.when(adService.publishBySeller(1, 2)).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/ad/publish/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void archive_WhenValidRequest_ShouldReturnTrue() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(2);
        Mockito.when(adService.archiveBySeller(1, 2)).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.put("/ad/archive/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void findById_WhenValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(adService.findById(1)).thenReturn(new AdResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/ad/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findByTitle_WhenValidRequest_ShouldReturnOk() throws Exception {
        GetAdRequest getAdRequest = new GetAdRequest();
        getAdRequest.setTitle("test");
        getAdRequest.setCount(5);
        getAdRequest.setAdSortType(AdSortType.DEFAULT);
        Mockito.when(adService.findByTitle(any())).thenReturn(new PaginationAdResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/ad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getAdRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void findByTitle_WhenNoValidCount_ShouldReturnBadRequest() throws Exception {
        GetAdRequest getAdRequest = new GetAdRequest();
        getAdRequest.setTitle("test");
        getAdRequest.setCount(0);
        getAdRequest.setAdSortType(AdSortType.DEFAULT);
        mockMvc.perform(MockMvcRequestBuilders.get("/ad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getAdRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByTitle_WhenNullTitle_ShouldReturnBadRequest() throws Exception {
        GetAdRequest getAdRequest = new GetAdRequest();
        getAdRequest.setTitle("");
        getAdRequest.setCount(1);
        getAdRequest.setAdSortType(AdSortType.DEFAULT);
        mockMvc.perform(MockMvcRequestBuilders.get("/ad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getAdRequest)))
                .andExpect(status().isBadRequest());
    }
}

