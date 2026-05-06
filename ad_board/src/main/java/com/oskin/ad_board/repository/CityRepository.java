package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CityRepository extends AbstractCrudRepository<City> {
    @PersistenceContext
    EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(CityRepository.class);

    @Autowired
    public CityRepository() {
        super(City.class);
    }

    public Optional<City> findByName(String name) {
        try {
            log.info("start findByName: {}", name);
            TypedQuery<City> query = entityManager.createQuery("FROM City WHERE name = :name", City.class);
            query.setParameter("name", name);
            City city = query.getSingleResult();
            log.info("successful findByName: {}", name);
            return Optional.of(city);
        } catch (Exception e) {
            log.info("error findByName: {}, exception => {}", name, e.getMessage());
            return Optional.empty();
        }
    }
}
