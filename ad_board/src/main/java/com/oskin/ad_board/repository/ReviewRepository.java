package com.oskin.ad_board.repository;

import com.oskin.ad_board.dto.request.GetReviewRequest;
import com.oskin.ad_board.model.Review;
import com.oskin.ad_board.model.ReviewSortType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class ReviewRepository extends AbstractCrudRepository<Review> {
    @PersistenceContext
    EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(ReviewRepository.class);

    @Autowired
    public ReviewRepository() {
        super(Review.class);
    }

    private String rowValueReviewBuilder(ReviewSortType sortType) {
        switch (sortType) {
            case RATING_ASC -> {
                return "and (r.rating, r.id) > (:lastValue, :lastId) ";
            }
            case RATING_DESC -> {
                return "and (r.rating, r.id) < (:lastValue, :lastId) ";
            }
            default -> {
                return "and (r.createdDateTime, r.id) < (:lastValue, :lastId) ";
            }
        }
    }

    private String buildHql(ReviewSortType sortType, int lastId) {
        String rowValue = lastId > 0 ? rowValueReviewBuilder(sortType) : "";
        String orderById = sortType == ReviewSortType.RATING_ASC ? "" : "DESC ";
        return "select r as review, p.name as authorName from Review r " +
                "join Profile p on p.user.id = r.author.id " +
                "join fetch r.author " +
                "where r.ad.id = :adId " +
                rowValue +
                "order by r." + sortType.getType() + ", r.id " + orderById +
                "limit :count";
    }

    public List<Tuple> findByAd(int adId, GetReviewRequest getReviewRequest) {
        ReviewSortType sortType = getReviewRequest.getReviewSortType();
        int count = getReviewRequest.getCount();
        int lastId = getReviewRequest.getLastId();
        double lastRating = getReviewRequest.getLastRating();
        LocalDateTime lastTime = getReviewRequest.getLastDateTime();
        String hql = buildHql(sortType, lastId);
        try {
            log.info("start findByAd, id : {}", adId);
            TypedQuery<Tuple> query = entityManager.createQuery(hql, Tuple.class);
            query.setParameter("adId", adId);
            query.setParameter("count", count);
            if (lastId > 0) {
                query.setParameter("lastId", lastId);
                if (sortType == ReviewSortType.CREATED_DATE_TIME) {
                    query.setParameter("lastValue", lastTime);
                } else {
                    query.setParameter("lastValue", lastRating);
                }
            }
            List<Tuple> list = query.getResultList();
            log.info("successful findByAd, id : {}", adId);
            return list;
        } catch (Exception e) {
            log.info("error findByAd, id : {}, exception => {}", adId, e.getMessage());
            return Collections.emptyList();
        }
    }
}
