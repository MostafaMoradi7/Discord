package com.example.clientfront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class ServerDiscord implements Serializable {
    private static final long serialVersionUID = 5L;
    private Integer serverID;
    private String name;
    private HashSet<Client> members;
    private HashSet<Group> groups;
    private HashSet<Client> bannedClient;
    private Client creator;
    private String created_At;

    public ServerDiscord(Integer serverID, String name, Client creator, String created_At) {
        this.serverID = serverID;
        this.name = name;
        this.creator = creator;
        this.created_At = created_At;
    }

    public Integer getServerID() {
        return serverID;
    }

    public void setServerID(Integer serverID) {
        this.serverID = serverID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<Client> getMembers() {
        return members;
    }

    public void setMembers(HashSet<Client> members) {
        this.members = members;
    }

    public HashSet<Group> getGroups() {
        return groups;
    }

    public void setGroups(HashSet<Group> groups) {
        this.groups = groups;
    }

    public HashSet<Client> getBannedClient() {
        return bannedClient;
    }

    public void setBannedClient(HashSet<Client> bannedClient) {
        this.bannedClient = bannedClient;
    }

    public Client getCreator() {
        return creator;
    }

    public void setCreator(Client creator) {
        this.creator = creator;
    }

    public String getCreated_At() {
        return created_At;
    }

    public void setCreated_At(String created_At) {
        this.created_At = created_At;
    }

    @Override
    public String toString() {
        return "ServerDiscord{" +
                "serverID=" + serverID +
                ", name='" + name + '\'' +
                ", members=" + members +
                ", groups=" + groups +
                ", bannedClient=" + bannedClient +
                ", creator=" + creator +
                ", created_At='" + created_At + '\'' +
                '}';
    }
}
