package com.example.clientfront;

import java.io.Serializable;

public class PrivateChatMessage implements Serializable {

    private final static long serialVersionUID = 4L;
    private int messageID;
    private int chatId;
    private Client sender;
    private Client receiver;
    private String dateTime;
    private String message;
    private TypeMVF type;
    private byte[] buffer;

    public PrivateChatMessage(int chatId, Client sender, Client receiver, String dateTime, String message, TypeMVF type) {
        this.chatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        this.message = message;
        this.type = type;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setMessage(String message) {
        this.message = message;
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
    public String toString(){
        String time = dateTime.substring(0,19);
        return sender.getUsername() +": " + message +"\n"
                +"\t\t\t\t\t\t" + time;
    }
}
