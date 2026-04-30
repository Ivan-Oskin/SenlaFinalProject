package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Message;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageRepository extends AbstractCrudRepository<Message> {

    @PersistenceContext
    EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(MessageRepository.class);

    @Autowired
    public MessageRepository() {
        super(Message.class);
    }

    public List<Tuple> getMessagesByDialog(int id) {
        String hql = "select m as message, p.name as userName from Message m " +
                "join fetch user u " +
                "join fetch dialog d " +
                "join Profile p on p.user.id = m.user.id " +
                "where d.id = :id " +
                "order by createdDateTime";
        List<Tuple> list = new ArrayList<>();
        try {
            log.info("start getMessagesByDialog, dialog id: {}", id);
            TypedQuery<Tuple> query = entityManager.createQuery(hql, Tuple.class);
            query.setParameter("id", id);
            list = query.getResultList();
            log.info("successful getMessagesByDialog, dialog id: {}", id);
            return list;
        } catch (Exception e) {
            log.info("error getMessagesByDialog, dialog id: {}, error => {}", id, e.getMessage());
            return list;
        }
    }
}
