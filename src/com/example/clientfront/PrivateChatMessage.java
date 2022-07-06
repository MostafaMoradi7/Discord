package com.example.clientfront;

import java.io.Serializable;

public class PrivateChatMessage implements Serializable {
    private static final long serialVersionUID = 4L;
    private int messageID;
    private int ChatId;
    private Client sender;
    private Client receiver;
    private String dateTime;
    private String message;
    private TypeMVF type;

    public PrivateChatMessage(int chatId, Client sender, Client receiver, String dateTime, String message,TypeMVF type) {
        this.ChatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        this.message = message;
        this.type=type;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getChatId() {
        return ChatId;
    }

    public Client getSender() {
        return sender;
    }

    public Client getReceiver() {
        return receiver;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public TypeMVF getType() {
        return type;
    }

    @Override
    public String toString() {
        return "PrivateChatMessage{" +
                "messageID=" + messageID +
                ", ChatId=" + ChatId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", dateTime='" + dateTime + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                '}';
    }
}