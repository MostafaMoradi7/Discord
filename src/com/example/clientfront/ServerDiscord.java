package com.example.clientfront;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;

public class ServerDiscord implements Serializable {
    private static final long serialVersionUID = 5L;
    private Integer serverID;
    private Client creator;
    private String name;
    private HashSet<Client> members;
    private HashSet<Client> admins;
    private HashSet<Client> bannedClients;
    private HashSet<Group> groups;
    private String created_At;

    public ServerDiscord(Integer serverID,  String name ,Client creator, String created_At) {
        this.serverID = serverID;
        this.creator = creator;
        this.name = name;
        this.created_At = created_At;
    }

    public void setServerID(Integer serverID) {
        this.serverID = serverID;
    }

    public void setCreator(Client creator) {
        this.creator = creator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(HashSet<Client> members) {
        this.members = members;
    }

    public void setAdmins(HashSet<Client> admins) {
        this.admins = admins;
    }

    public void setBannedClients(HashSet<Client> bannedClients) {
        this.bannedClients = bannedClients;
    }

    public void setGroups(HashSet<Group> groups) {
        this.groups = groups;
    }

    public void setCreated_At(String created_At) {
        this.created_At = created_At;
    }

    public Integer getServerID() {
        return serverID;
    }

    public Client getCreator() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public HashSet<Client> getMembers() {
        return members;
    }

    public HashSet<Client> getAdmins() {
        return admins;
    }

    public HashSet<Client> getBannedClients() {
        return bannedClients;
    }

    public HashSet<Group> getGroups() {
        return groups;
    }

    public String getCreated_At() {
        return created_At;
    }
}