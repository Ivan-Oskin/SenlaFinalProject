package com.oskin.ad_board.repository;

import com.oskin.ad_board.dto.request.GetAdRequest;
import com.oskin.ad_board.model.Ad;
import com.oskin.ad_board.model.AdSortType;
import com.oskin.ad_board.model.City;
import com.oskin.ad_board.model.StatusAd;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdRepository extends AbstractCrudRepository<Ad> {

    @PersistenceContext
    EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(AdRepository.class);

    @Autowired
    public AdRepository() {
        super(Ad.class);
    }

    private String buildSearch(GetAdRequest getAdRequest) {
        AdSortType adSortType = getAdRequest.getAdSortType();
        boolean isPaid = getAdRequest.isPaid();
        String city = getAdRequest.getCity();
        String hql = "SELECT a FROM Ad a " +
                "JOIN Profile p on p.user = a.seller " +
                "JOIN FETCH a.city c " +
                "JOIN FETCH a.seller s " +
                "WHERE (LOWER(a.title) LIKE LOWER(:search) " +
                "OR LOWER(a.description) LIKE LOWER(:search)) " +
                "AND (a.status = :active or a.status = :reserved) " +
                "ORDER BY ";

        if (adSortType == AdSortType.DEFAULT) {
            hql += "CASE WHEN LOWER(a.title) LIKE LOWER(:exact) THEN 1 " +
                    "WHEN LOWER(a.title) LIKE LOWER(:search) THEN 2 " +
                    "ELSE 3 END, ";
        }

        if (city != null) {
            hql += "CASE WHEN a.city.name LIKE LOWER(:city) THEN 1 " +
                    "ELSE 2 END, ";
        }

        if (isPaid) {
            log.info("isPaid check");
            hql += "a.isPaid DESC, ";
        }

        hql += adSortType.getHqlString() + "rating DESC";
        return hql;
    }

    public List<Ad> searchByTitle(GetAdRequest getAdRequest) {
        List<Ad> list = new ArrayList<>();
        String hql = buildSearch(getAdRequest);
        String title = getAdRequest.getTitle();
        String city = getAdRequest.getCity();
        try {
            log.info("start findByTitle");
            TypedQuery<Ad> query = entityManager.createQuery(hql, Ad.class);
            query.setParameter("search", "%" + title + "%");
            if (getAdRequest.getAdSortType() == AdSortType.DEFAULT) query.setParameter("exact", title);
            if(city != null) query.setParameter("city", city);
            query.setParameter("active", StatusAd.ACTIVE);
            query.setParameter("reserved", StatusAd.RESERVED);
            list = query.getResultList();
            log.info("successful findByTitle");
            return list;
        } catch (Exception e) {
            log.info("error findByTitle, exception => {}", e.getMessage());
            return list;
        }
    }

    public List<Ad> findBySeller(int sellerId) {
        List<Ad> list = new ArrayList<>();
        try {
            log.info("start findBySeller");
            TypedQuery<Ad> query = entityManager.createQuery("FROM Ad a " +
                    "JOIN FETCH a.city c " +
                    "JOIN FETCH a.seller s " +
                    "WHERE a.seller.id = :id", Ad.class);
            query.setParameter("id", sellerId);
            list = query.getResultList();
            log.info("successful findBySeller");
            return list;
        } catch (Exception e) {
            log.info("error findBySeller, exception => {}", e.getMessage());
            return list;
        }
    }

    public List<Ad> findAllToModeration() {
        List<Ad> list = new ArrayList<>();
        try {
            log.info("start findAllToModeration");
            TypedQuery<Ad> query = entityManager.createQuery(
                    "FROM Ad a " +
                            "JOIN FETCH a.city c " +
                            "JOIN FETCH a.seller s " +
                            "WHERE a.status = :status", Ad.class);
            query.setParameter("status", StatusAd.MODERATION);
            list = query.getResultList();
            log.info("successful findAllToModeration");
            return list;
        } catch (Exception e) {
            log.info("error findAllToModeration, exception => {}", e.getMessage());
            return list;
        }
    }
}
