package com.oskin.ad_board.repository;

import org.springframework.beans.factory.annotation.Autowired;
import com.oskin.ad_board.model.Profile;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepository extends AbstractCrudRepository<Profile> {

    @Autowired
    public ProfileRepository() {
        super(Profile.class);
    }
}
