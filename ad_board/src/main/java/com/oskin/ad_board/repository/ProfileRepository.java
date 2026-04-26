package com.oskin.ad_board.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.oskin.ad_board.model.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProfileRepository extends AbstractCrudRepository<Profile> {
    @PersistenceContext
    EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(CityRepository.class);

    @Autowired
    public ProfileRepository() {
        super(Profile.class);
    }

    public Optional<Profile> findByUserId(int userId) {
        try {
            log.info("start findByUserId: {}", userId);
            TypedQuery<Profile> query = entityManager.createQuery("FROM Profile WHERE user.id = :userId", Profile.class);
            query.setParameter("userId", userId);
            Profile profile = query.getSingleResult();
            log.info("successful findByUserId: {}", userId);
            return Optional.of(profile);
        } catch (Exception e) {
            log.info("error findByUserId: {}, exception => {}", userId, e.getMessage());
            return Optional.empty();
        }
    }
}
