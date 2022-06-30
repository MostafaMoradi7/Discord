package Services;

import ClientOperations.Client;
import MessageOperations.ChannelMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class Channel implements Serializable {
    private String channelID;
    private String channelName;
    private ArrayList<Client> members;
    private ArrayList<Client> admins;
    private ArrayList<Client> bannedClients;
    private Client creator;
    private ArrayList<ChannelMessage> messages;

    public Channel(String channelID, String channelName, Client creator) {
        this.channelID = channelID;
        this.channelName = channelName;
        this.creator = creator;
        members = new ArrayList<>();
        admins = new ArrayList<>();
        bannedClients = new ArrayList<>();
        members.add(creator);
        admins.add(creator);
    }


    public String getChannelID() {
        return channelID;
    }

    public String getChannelName() {
        return channelName;
    }

public Client getCreator() {
        return creator;
    }

    public ArrayList<Client> getMembers() {
        return members;
    }

    public ArrayList<Client> getAdmins() {
        return admins;
    }


    public void addMember(Client member) {
        members.add(member);
    }

    public void removeMember(Client member) {
        members.remove(member);
    }

    public void addAdmin(Client admin) {
        admins.add(admin);
    }

    public void removeAdmin(Client admin) {
        admins.remove(admin);
    }

    public void banUser(Client client) {
        bannedClients.add(client);
    }

    public void unbanUser(Client client) {
        bannedClients.remove(client);
    }

    public void addMessage(ChannelMessage message) {
        messages.add(message);
    }


}
