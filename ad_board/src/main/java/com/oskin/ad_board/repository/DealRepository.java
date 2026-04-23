package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Deal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DealRepository extends AbstractCrudRepository<Deal> {

    @Autowired
    public DealRepository() {
        super(Deal.class);
    }
}
