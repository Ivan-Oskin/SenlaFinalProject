package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends AbstractCrudRepository<User> {

    @Autowired
    public UserRepository() {
        super(User.class);
    }
}
