package Services;

import ClientOperations.Client;
import MessageOperations.PrivateChatMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat implements Serializable {
    private String chatID;
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

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
}
