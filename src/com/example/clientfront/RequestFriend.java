package com.example.clientfront;

import java.io.Serializable;

public class RequestFriend implements Serializable {
    private static final long serialVersionUID = 9L;
    private int id;
    private Client sender;
    private Client receiver;
    private int isAccept;
    private String created_At;

    public RequestFriend(int id, Client sender, Client receiver, int isAccept, String created_At) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.isAccept = isAccept;
        this.created_At = created_At;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public Client getReceiver() {
        return receiver;
    }

    public void setReceiver(Client receiver) {
        this.receiver = receiver;
    }

    public int getAccept() {
        return isAccept;
    }

    public void setAccept(int accept) {
        isAccept = accept;
    }

    public String getCreated_At() {
        return created_At;
    }

    public void setCreated_At(String created_At) {
        this.created_At = created_At;
    }
}