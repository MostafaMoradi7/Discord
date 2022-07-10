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

    public ServerDiscord(Integer serverId, String name, Client creator) {
        this.creator = creator;
        this.serverID = serverId;
        this.name = name;
        created_At = LocalDateTime.now().toString();
    }
    public Integer getServerID(){
        return serverID;
    }

    public void addGroup(Group newGroup){
        groups.add(newGroup);
    }
    public void addMembers(HashSet<Client> members){
        this.members = members;
    }
    public Client getCreator(){
        return creator;
    }
    public String getName(){
        return name;
    }


    //    public void addMember(Client member) {
//        PortableData portableData = new PortableData("greeting", member);
//        Thread chatOutputHandlerThread = new Thread(chatOutputHandler);
//        Thread chatInputHandlerThread = new Thread(chatInputHandler);
//        ChatInputHandler.setIsRunning(true);
//        chatOutputHandler.setPortableData(portableData);
//        chatOutputHandlerThread.start();
//
//        members.add(member);
//    }
//
//    public void removeMember(Client member) {
//        members.remove(member);
//        member.removeServer(this);
//    }
//
//    public void addAdmin(Client admin) {
//        admins.add(admin);
//    }
//
//    public void removeAdmin(Client admin) {
//        admins.remove(admin);
//    }
//
//    public void banClient(Client bannedClient) {
//        bannedClients.add(bannedClient);
//    }
//
//    public void unbanClient(Client bannedClient) {
//        bannedClients.remove(bannedClient);
//    }
//
//    public void addGroup(Group group) {
//        groups.add(group);
//    }
//
//    public void removeGroup(Group group) {
//        groups.remove(group);
//    }
//
//    public void addChannel(Channel channel) {
//        channels.add(channel);
//    }
//
//    public void removeChannel(Channel channel) {
//        channels.remove(channel);
//    }


    /*------------------------------------------------------------------------------------*/

    public void broadCast(Object message){
        PortableData portableData = new PortableData("broadcast", message);

    }
    /*------------------------------------------------------------------------------------*/
//    public String getServerName() {
//        return serverName;
//    }
//
//    public String getServerMainCreator() {
//        return mainCreator.getUsername();
//    }
//
//    public HashSet<Client> getMembers() {
//        return (members);
//    }
//
//    public HashSet<Client> getAdmins() {
//        return (admins);
//    }
//
//    public HashSet<Channel> getChannels() {
//        return (channels);
//    }
//
//    public HashSet<Group> getGroups() {
//        return (groups);
//    }
//
//    public Channel getChannel(String channelName) {
//        for (Channel channel : channels) {
//            if (channel.getChannelID().equals(channelName)) {
//                return channel;
//            }
//        }
//        return null;
//    }

    public HashSet<Client> getBannedClients() {
        return (bannedClients);
    }


    @Override
    public String toString(){
        return name +" " + serverID + " " + created_At + "\n" +
                "members: " + members + "\n" +
                "groups: " + groups;
    }
}
