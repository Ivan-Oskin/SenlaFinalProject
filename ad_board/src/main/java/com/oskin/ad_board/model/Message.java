package com.oskin.ad_board.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message implements IIdentified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "message")
    private String message;
    @CreationTimestamp
    @Column(name = "send_date_time", updatable = false)
    private LocalDateTime sendDateTime;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSendDateTime() {
        return sendDateTime;
    }

    public Message() {
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message(Dialog dialog, User user, String message) {
        this.dialog = dialog;
        this.user = user;
        this.message = message;
    }

    public Message(int id, Dialog dialog, User user, String message) {
        this.id = id;
        this.dialog = dialog;
        this.user = user;
        this.message = message;
    }
}
