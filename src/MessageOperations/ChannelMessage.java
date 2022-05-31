package MessageOperations;

import ClientOperations.Client;
import Services.Type;

import java.io.Serializable;
import java.util.ArrayList;

public class ChannelMessage implements Serializable {
    private String channelName;
    private String pinnedMessage;
    private Client mainADMIN;
    private Type type;
    private ArrayList<Client> members;
    private ArrayList<Client> admins;
    private ArrayList<ChannelMessage> messages;

    public ChannelMessage(String channelName, String pinnedMessage, Client mainADMIN, Type type) {
        this.channelName = channelName;
        this.pinnedMessage = pinnedMessage;
        this.mainADMIN = mainADMIN;
        this.type = type;
        members = new ArrayList<>();
        admins = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public String getPinnedMessage() {
        return pinnedMessage;
    }

    public Client getMainADMIN() {
        return mainADMIN;
    }

    public ArrayList<Client> getMembers() {
        return members;
    }

    public ArrayList<Client> getAdmins() {
        return admins;
    }

    public ArrayList<ChannelMessage> getMessages() {
        return messages;
    }
}
