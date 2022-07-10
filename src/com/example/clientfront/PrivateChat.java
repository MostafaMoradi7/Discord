package com.example.clientfront;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat implements Serializable {
    private static final long serialVersionUID = 3L;
    private int chatID;
    private Client clientONE;
    private Client clientTWO;
    private ArrayList<PrivateChatMessage> messages;

    public PrivateChat(Client clientONE, Client clientTWO) {
        this.clientONE = clientONE;
        this.clientTWO = clientTWO;
        messages = new ArrayList<>();
    }


    public Client getClientTWO() {
        return clientTWO;
    }

    public Client getClientONE() {
        return clientONE;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public void addMessage(PrivateChatMessage message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return "PrivateChat [chatID=" + chatID + ", clientONE=" + clientONE + ", clientTWO=" + clientTWO + ", messages=" + messages + "]";
    }

    public int getChatID() {
        return chatID;
    }

    public ArrayList<PrivateChatMessage> getMessages() {
        return messages;
    }

    public void showUnseenMessages(){

    }
}
