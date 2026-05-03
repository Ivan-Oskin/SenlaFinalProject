package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Deal;
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
public class DealRepository extends AbstractCrudRepository<Deal> {

    @PersistenceContext
    EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(DealRepository.class);

    @Autowired
    public DealRepository() {
        super(Deal.class);
    }

    private String buildHistoryHql(boolean isSell) {
        String hql = "FROM Deal d JOIN FETCH d.ad a JOIN FETCH d.buyer b WHERE ";
        hql += isSell ? "a.seller.id = " : "b.id = ";
        hql += ":id";
        return hql;
    }

    public List<Deal> getHistory(int id, boolean isSell) {
        List<Deal> list = new ArrayList<>();
        String hql = buildHistoryHql(isSell);
        try {
            log.info("start getHistorySell by id: {}", id);
            TypedQuery<Deal> query = entityManager.createQuery(hql, Deal.class);
            query.setParameter("id", id);
            list = query.getResultList();
            log.info("successful getHistorySell by id: {}", id);
        } catch (Exception e) {
            log.info("error getHistorySell by id: {}, exception => {}", id, e.getMessage());
        }
        return list;
    }
}
