package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepository extends AbstractCrudRepository<City>{

    @Autowired
    public CityRepository() {
        super(City.class);
    }
}
