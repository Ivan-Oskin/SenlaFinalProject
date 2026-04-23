package com.oskin.ad_board.service;

import com.oskin.ad_board.model.Message;
import com.oskin.ad_board.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public boolean save(Message message) {
        return messageRepository.create(message);
    }

    public boolean update(Message message) {
        return messageRepository.update(message);
    }

    public boolean delete(int id) {
        return messageRepository.delete(id);
    }

    public Message findById(int id) {
        Optional<Message> optional = messageRepository.findById(id);
        return optional.orElse(null);
    }
}
