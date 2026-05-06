package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        try {
            log.info("Start findByMail: {}", mail);
            TypedQuery<User> query = entityManager.createQuery("FROM User WHERE mail = :mail", User.class);
            query.setParameter("mail", mail);
            User user = query.getSingleResult();
            log.info("Successful findByMail: {}", mail);
            return Optional.of(user);
        } catch (Exception e) {
            log.info("error findByMail: {}", mail);
            return Optional.empty();
        }
    }
}