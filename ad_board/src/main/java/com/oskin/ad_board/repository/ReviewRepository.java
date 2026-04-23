package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository extends AbstractCrudRepository<Review> {

    @Autowired
    public ReviewRepository() {
        super(Review.class);
    }
}
