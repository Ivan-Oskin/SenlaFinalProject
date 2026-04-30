package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Dialog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DialogRepository extends AbstractCrudRepository<Dialog> {

    @PersistenceContext
    EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(DialogRepository.class);

    @Autowired
    public DialogRepository() {
        super(Dialog.class);
    }

    public Optional<Dialog> findByAdAndBuyer(int adId, int buyerId) {
        try {
            String hql = "from Dialog d " +
                    "join d.buyer b " +
                    "join d.ad a " +
                    "where b.id = :buyerId and a.id = :adId";
            log.info("start find Dialog by Ad id : {} and buyer id: {}", adId, buyerId);
            TypedQuery<Dialog> query = entityManager.createQuery(hql, Dialog.class);
            query.setParameter("buyerId", buyerId);
            query.setParameter("adId", adId);
            Dialog dialog = query.getSingleResult();
            log.info("successful find Dialog by Ad id : {}, buyer Id : {}", adId, buyerId);
            return Optional.ofNullable(dialog);
        } catch (Exception e) {
            log.info("error find Dialog by Ad id : {}, Buyer id : {}, exception => {}", adId, buyerId, e.getMessage());
            return Optional.empty();
        }
    }

    public Dialog createAndGet(Dialog dialog) {
        try {
            log.info("dialog id before = > {}", dialog.getId());
            log.info("start save");
            entityManager.persist(dialog);
            entityManager.flush();
            log.info("successful save");
            log.info("dialog id after => {}", dialog.getId());
            return dialog;
        } catch (Exception e) {
            log.error("error save, exception => {}", e.getMessage());
            return null;
        }
    }
}
