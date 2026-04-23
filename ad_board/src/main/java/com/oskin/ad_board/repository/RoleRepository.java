package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository extends AbstractCrudRepository<Role> {

    @Autowired
    public RoleRepository() {
        super(Role.class);
    }
}
