package com.example.clientfront;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Group implements Serializable {
    private static final long serialVersionUID = 7L;
    private int serverID;
    private Integer groupID;
    private String name;
    private Client creator;
    private ArrayList<Client> clients;
    private ArrayList<GroupMessage> messages;
    private ArrayList<Client> admins;
    private String created_At;
    private GroupMessage pinnedMessage;


    public Group(int serverID, Integer groupID, String name, Client creator) {
        this.serverID = serverID;
        this.groupID = groupID;
        this.name = name;
        this.creator = creator;
        created_At = LocalDateTime.now().toString();
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

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return name;
    }

    public void addMessage(GroupMessage message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return "Group{" +
                "serverID=" + serverID +
                ", groupID=" + groupID +
                ", name='" + name + '\'' +
                ", creator=" + creator +
                ", clients=" + clients +
                ", messages=" + messages +
                ", admins=" + admins +
                ", created_At='" + created_At + '\'' +
                ", pinnedMessage=" + pinnedMessage +
                '}';
    }
}
