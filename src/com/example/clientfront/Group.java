package com.example.clientfront;

import java.util.ArrayList;

public class Group {
    private Integer groupID;
    private int serverID;
    private String name;
    private Client creator;
    private GroupMessage pinnedMessage;
    private String created_At;
    private ArrayList<Client> clients;
    private ArrayList<GroupMessage> messages;
    private ArrayList<Client> admins;

    public Group(Integer groupID, int serverID, String name, Client creator, GroupMessage pinnedMessage, String created_At) {
        clients = new ArrayList<>();
        messages = new ArrayList<>();
        admins = new ArrayList<>();
        this.groupID = groupID;
        this.serverID = serverID;
        this.name = name;
        this.creator = creator;
        this.pinnedMessage = pinnedMessage;
        this.created_At = created_At;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getName() {
        return name;
    }

    public Client getCreator() {
        return creator;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public String getCreated_At() {
        return created_At;
    }

    public ArrayList<GroupMessage> getMessages() {
        return messages;
    }

    public ArrayList<Client> getAdmins() {
        return admins;
    }

    public int getServerID() {
        return serverID;
    }

    public GroupMessage getPinnedMessage() {
        return pinnedMessage;
    }

    public void addMember(Client client) {
        clients.add(client);
    }
    public void addAdmin(Client client) {
        admins.add(client);
    }
}
