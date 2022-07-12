package com.example.clientfront;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat implements Serializable {
     private static final long serialVersionUID = 3L;
        private int chatID;
        private Client clientONE;
        private Client clientTWO;
        private int banned;
        private ArrayList<PrivateChatMessage> messages;

    public PrivateChat(int chatID, Client clientONE, Client clientTWO, int banned) {
        this.chatID = chatID;
        this.clientONE = clientONE;
        this.clientTWO = clientTWO;
        this.banned = banned;
        messages = new ArrayList<>();
    }

    public Client getClientONE() {
        return clientONE;
    }

    public Client getClientTWO() {
        return clientTWO;
    }

    public ArrayList<PrivateChatMessage> getMessages() {
        return messages;
    }

    public int getChatID() {
        return chatID;
    }
    public void setChatID(int chatID){
            this.chatID=chatID;
    }

    @Override
    public String toString() {
        return "PrivateChat{" +
                "chatID=" + chatID +
                ", clientONE=" + clientONE +
                ", clientTWO=" + clientTWO +
                ", messages=" + messages +
                '}';
    }

    public void setClientONE(Client clientONE) {
        this.clientONE = clientONE;
    }

    public void setClientTWO(Client clientTWO) {
        this.clientTWO = clientTWO;
    }

    public void setMessages(ArrayList<PrivateChatMessage> messages) {
        this.messages = messages;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }
}

