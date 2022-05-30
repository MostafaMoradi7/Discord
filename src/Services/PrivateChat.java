package Services;

import ClientOperations.Client;
import MessageOperations.PrivateChatMessage;

import java.util.ArrayList;

public class PrivateChat implements Runnable{
    private Client clientONE;
    private Client clientTWO;
    private ArrayList<PrivateChatMessage> messages;

    public PrivateChat(Client clientONE, Client clientTWO) {
        this.clientONE = clientONE;
        this.clientTWO = clientTWO;
        messages = new ArrayList<>();
    }

    @Override
    public void run() {

    }
}
