package com.oskin.ad_board.service_test;

import com.oskin.ad_board.dto.request.GetReviewRequest;
import com.oskin.ad_board.dto.request.ReviewRequest;
import com.oskin.ad_board.dto.response.BooleanResponse;
import com.oskin.ad_board.dto.response.PaginationReviewResponse;
import com.oskin.ad_board.dto.response.ReviewResponse;
import com.oskin.ad_board.exception.IdMatchException;
import com.oskin.ad_board.exception.PaginationRequestException;
import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.model.Review;
import com.oskin.ad_board.model.ReviewSortType;
import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.AdRepository;
import com.oskin.ad_board.repository.ReviewRepository;
import com.oskin.ad_board.repository.UserRepository;
import com.oskin.ad_board.service.ReviewService;
import com.oskin.ad_board.utils.MapperDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdRepository adRepository;
    @Mock
    private MapperDto mapperDto;

    @Test
    public void save_WhenValidRequest_ShouldReturnTrue() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.of(new Ad()));
        Review review = new Review();
        Mockito.when(mapperDto.reviewRequestToEntity(any(), any(), any())).thenReturn(review);
        Mockito.when(reviewRepository.create(any())).thenReturn(true);
        BooleanResponse booleanResponse = reviewService.save(new ReviewRequest(), 1, 1);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    public void save_WhenNoFoundUser_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> reviewService.save(new ReviewRequest(), 1, 1));
        Assertions.assertEquals("not found user with id = 1", exception.getMessage());
    }

    @Test
    public void save_WhenNoFoundAd_ShouldThrowException() {
        Mockito.when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        Mockito.when(adRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> reviewService.save(new ReviewRequest(), 1, 1));
        Assertions.assertEquals("not found ad with id = 1", exception.getMessage());
    }

    @Test
    void delete_WhenFoundReviewAndMatchId_ShouldReturnTrue() {
        Review review = new Review();
        User user = new User();
        user.setId(1);
        review.setAuthor(user);
        Mockito.when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        Mockito.when(reviewRepository.delete(review)).thenReturn(true);
        BooleanResponse booleanResponse = reviewService.delete(1, 1);
        Assertions.assertTrue(booleanResponse.isBool());
    }

    @Test
    void delete_WhenNoFoundReview_ShouldThrowException() {
        Mockito.when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> reviewService.delete(1, 1));
        Assertions.assertEquals("not found review with id = 1", exception.getMessage());
    }

    @Test
    void delete_WhenNoMatchId_ShouldThrowException() {
        Review review = new Review();
        User user = new User();
        user.setId(1);
        review.setAuthor(user);
        Mockito.when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        Exception exception = Assertions.assertThrows(IdMatchException.class,
                () -> reviewService.delete(1, 2));
        Assertions.assertEquals("the user's ID does not match the review's ID", exception.getMessage());
    }

    @Test
    void getReviewsByAd_WhenValidFirstRequest_ShouldReturnList() {
        GetReviewRequest getReviewRequest = new GetReviewRequest();
        getReviewRequest.setLastId(0);
        Tuple tuple = mock(Tuple.class);
        ReviewResponse reviewResponse = new ReviewResponse();
        PaginationReviewResponse paginationReviewResponse = new PaginationReviewResponse();
        paginationReviewResponse.setList(Collections.singletonList(reviewResponse));
        List<Tuple> list = Collections.singletonList(tuple);
        Mockito.when(reviewRepository.findByAd(1, getReviewRequest)).thenReturn(list);
        Mockito.when(mapperDto.tupleReviewToResponse(any())).thenReturn(reviewResponse);
        Mockito.when(mapperDto.ReviewsToPaginationResponse(anyList())).thenReturn(paginationReviewResponse);
        PaginationReviewResponse verifyPaginationReviewResponse = reviewService.getReviewByAd(1, getReviewRequest);
        Assertions.assertEquals(paginationReviewResponse, verifyPaginationReviewResponse);
    }

    @Test
    void getReviewsByAd_WhenSortTypeTimeAndLastTimeNull_ShouldThrowException() {
        GetReviewRequest getReviewRequest = new GetReviewRequest();
        getReviewRequest.setLastId(1);
        getReviewRequest.setReviewSortType(ReviewSortType.CREATED_DATE_TIME);
        Exception exception = Assertions.assertThrows(PaginationRequestException.class,
                () -> reviewService.getReviewByAd(1, getReviewRequest));
        Assertions.assertEquals("sort type = time, but last time = null", exception.getMessage());
    }

    @Test
    void getReviewsByAd_WhenSortTypeRatingAndLastRatingNull_ShouldThrowException() {
        GetReviewRequest getReviewRequest = new GetReviewRequest();
        getReviewRequest.setLastId(1);
        getReviewRequest.setReviewSortType(ReviewSortType.RATING_ASC);
        Exception exception = Assertions.assertThrows(PaginationRequestException.class,
                () -> reviewService.getReviewByAd(1, getReviewRequest));
        Assertions.assertEquals("sort type = rating, but last rating = null", exception.getMessage());
    }
}
