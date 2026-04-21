package com.oskin.ad_board.model;

import java.time.LocalDateTime;

public class Message implements IIdentified {
    private int id;
    private int dialogId;
    private int userId;
    private String message;
    private LocalDateTime sendDateTime;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public void setSendDateTime(LocalDateTime sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public Message() {
    }

    public Message(int dialogId, int userId, String message, LocalDateTime sendDateTime) {
        this.dialogId = dialogId;
        this.userId = userId;
        this.message = message;
        this.sendDateTime = sendDateTime;
    }

    public Message(int id, int dialogId, int userId, String message, LocalDateTime sendDateTime) {
        this.id = id;
        this.dialogId = dialogId;
        this.userId = userId;
        this.message = message;
        this.sendDateTime = sendDateTime;
    }
}
