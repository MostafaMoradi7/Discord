package com.example.clientfront;

import java.io.Serializable;

public class PrivateChatMessage implements Serializable {
    private static final long serialVersionUID = 4L;
    private int messageID;
    private int chatId;
    private Client sender;
    private Client receiver;
    private String dateTime;
    private String message;
    private TypeMVF type;
    private byte[] buffer;

    public PrivateChatMessage(int chatId, Client sender, Client receiver, String dateTime, String message,TypeMVF type) {
        this.chatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        this.message = message;
        this.type=type;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getChatId() {
        return chatId;
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
                ", chatId=" + chatId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", dateTime='" + dateTime + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                '}';
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public void setReceiver(Client receiver) {
        this.receiver = receiver;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(TypeMVF type) {
        this.type = type;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }
}