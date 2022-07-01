package Services;

import ClientOperations.Client;
import MessageOperations.GroupMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String groupID;
    private String name;
    private Client creator;
    private ArrayList<Client> clients;
    private ArrayList<GroupMessage> messages;
    private ArrayList<Client> admins;

    public Group(String groupID,String name, Client creator) {
        this.name = name;
        this.creator = creator;
        clients = new ArrayList<>();
        messages = new ArrayList<>();
        admins = new ArrayList<>();
        this.groupID = groupID;
    }

    public void addMember(Client client) {
        clients.add(client);
    }

    public Client getCreator() {
        return creator;
    }

    public void removeMember(Client client) {
        clients.remove(client);
    }

    public void addAdmin(Client client) {
        admins.add(client);
    }

    public void removeAdmin(Client client) {
        admins.remove(client);
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return name;
    }

    public void addMessage(GroupMessage message) {
        messages.add(message);
    }


}
