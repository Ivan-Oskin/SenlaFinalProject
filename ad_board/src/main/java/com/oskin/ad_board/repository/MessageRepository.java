package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository extends AbstractCrudRepository<Message> {

    @Autowired
    public MessageRepository() {
        super(Message.class);
    }
}
