package com.example.clientfront;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class PrivateChatMessage implements Serializable {
    @Serial
    private final static long serialVersionUID = 4L;
    private int messageID;
    private int chatId;
    private Client sender;
    private Client receiver;
    private String dateTime;
    private String message;
    private TypeMVF type;

    public PrivateChatMessage(int chatId, Client sender, Client receiver, String dateTime, String message, TypeMVF type) {
        this.chatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        this.message = message;
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }




}
