import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat {
        private int chatID;
        private Client clientONE;
        private Client clientTWO;
        private ArrayList<PrivateChatMessage> messages;

        public PrivateChat(int chatID , Client clientONE, Client clientTWO) {
            this.chatID = chatID;
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

    public int getChatID() {
        return chatID;
    }
    public void setChatID(int chatID){
            this.chatID=chatID;
    }
}

