import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat {
        private String chatID;
        private Client clientONE;
        private Client clientTWO;
        private ArrayList<PrivateChatMessage> messages;

        public PrivateChat(Client clientONE, Client clientTWO) {
            this.clientONE = clientONE;
            this.clientTWO = clientTWO;
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

    public String getChatID() {
        return chatID;
    }
}

