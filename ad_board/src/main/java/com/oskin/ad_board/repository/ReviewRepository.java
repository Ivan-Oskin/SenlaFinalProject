package com.oskin.ad_board.repository;

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

    public List<Tuple> findByAd(int adId, ReviewSortType sortType) {
        String hql = "select r as review, p.name as authorName from Review r " +
                "join Profile p on p.user.id = r.author.id " +
                "join fetch r.author " +
                "where r.ad.id = :id " +
                "order by r." + sortType.getType();
        try {
            log.info("start findByAd, id : {}", adId);
            TypedQuery<Tuple> query = entityManager.createQuery(hql, Tuple.class);
            query.setParameter("id", adId);
            List<Tuple> list = query.getResultList();
            log.info("successful findByAd, id : {}", adId);
            return list;
        } catch (Exception e) {
            log.info("error findByAd, id : {}, exception => {}", adId, e.getMessage());
            return Collections.emptyList();
        }
    }
}
