package com.oskin.ad_board.controller_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.ad_board.controller.ReviewController;
import com.oskin.ad_board.dto.request.GetReviewRequest;
import com.oskin.ad_board.dto.request.ReviewRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.PaginationReviewResponse;
import com.oskin.ad_board.exception.GlobalExceptionHandler;
import com.oskin.ad_board.model.ReviewSortType;
import com.oskin.ad_board.service.ReviewService;
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
public class ReviewControllerTest {
    @InjectMocks
    ReviewController reviewController;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private ReviewService reviewService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void save_WhenValidRequest_ShouldReturnTrue() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setComment("this good comment");
        reviewRequest.setRating(5);
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(reviewService.save(any(), anyInt(), anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.post("/review/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void save_WhenNoValidRating_ShouldReturnBadRequest() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setRating(10);
        mockMvc.perform(MockMvcRequestBuilders.post("/review/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_WhenValidRequest_ShouldReturnTrue() throws Exception {
        Mockito.when(jwtUtils.getCurrentId()).thenReturn(1);
        Mockito.when(reviewService.delete(anyInt(), anyInt())).thenReturn(new BooleanResponse(true));
        mockMvc.perform(MockMvcRequestBuilders.delete("/review/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bool").value("true"));
    }

    @Test
    void getReviewAd_WhenValidGetReviewRequest_ShouldReturnOk() throws Exception {
        GetReviewRequest getReviewRequest = new GetReviewRequest();
        getReviewRequest.setReviewSortType(ReviewSortType.CREATED_DATE_TIME);
        getReviewRequest.setCount(20);
        Mockito.when(reviewService.getReviewByAd(anyInt(), any())).thenReturn(new PaginationReviewResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/review/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getReviewRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void getReviewAd_WhenNoValidCount_ShouldReturnIsBadRequest() throws Exception {
        GetReviewRequest getReviewRequest = new GetReviewRequest();
        getReviewRequest.setReviewSortType(ReviewSortType.CREATED_DATE_TIME);
        getReviewRequest.setCount(0);
        mockMvc.perform(MockMvcRequestBuilders.get("/review/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getReviewRequest)))
                .andExpect(status().isBadRequest());
    }
}
