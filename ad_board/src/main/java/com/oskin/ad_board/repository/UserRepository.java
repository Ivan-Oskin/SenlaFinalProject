package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends AbstractCrudRepository<User> {
    @PersistenceContext
    EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    public UserRepository() {
        super(User.class);
    }

    public Optional<User> findByMail(String mail) {
        log.info("Start findByMail");
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE mail = :mail", User.class);
        query.setParameter("mail", mail);
        List<User> users = query.getResultList();
        if (!users.isEmpty()) {
            return Optional.of(users.get(0));
        } else {
            return Optional.empty();
        }
    }
}