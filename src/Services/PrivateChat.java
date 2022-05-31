package Services;

import ClientOperations.Client;
import ClientOperations.PortableData;
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

    public void receiveMessage(PortableData portableData){
        PrivateChatMessage privateChat = (PrivateChatMessage) portableData.getObject();
        messages.add(privateChat);
    }
}
