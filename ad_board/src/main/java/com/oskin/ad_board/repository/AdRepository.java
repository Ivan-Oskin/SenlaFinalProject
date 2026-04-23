package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Ad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdRepository extends AbstractCrudRepository<Ad> {

    @Autowired
    public AdRepository() {
        super(Ad.class);
    }
}
