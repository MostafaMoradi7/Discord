package Server;

import Chat.ChatInputHandler;
import Chat.ChatOutputHandler;
import ClientOperations.Client;
import ClientOperations.ClientHandler;
import ClientOperations.PortableData;
import Services.Channel;
import Services.Group;
import Chat.Chat;

import java.util.ArrayList;
import java.util.HashSet;

public class Server extends Chat{
    private Client mainCreator;
    private ClientHandler clientHandler;
    private String serverName;
    private HashSet<Client> members;
    private HashSet<Client> admins;
    private HashSet<Client> bannedClients;
    private HashSet<Group> groups;
    private HashSet<Channel> channels;
    private ChatOutputHandler chatOutputHandler;
    private ChatInputHandler chatInputHandler;


    public Server(Client mainCreator, ClientHandler clientHandler, String serverName) {
        this.mainCreator = mainCreator;
        members = new HashSet<>();
        admins = new HashSet<>();
        bannedClients = new HashSet<>();
        groups = new HashSet<>();
        channels = new HashSet<>();
        members.add(mainCreator);
        admins.add(mainCreator);
        this.serverName = serverName;
        chatOutputHandler = new ChatOutputHandler(this, clientHandler.getClientSocket());
        chatInputHandler = new ChatInputHandler(this, clientHandler.getClientSocket());
    }

    public void addMember(Client member) {
        PortableData portableData = new PortableData("greeting", member);
        Thread chatOutputHandlerThread = new Thread(chatOutputHandler);
        Thread chatInputHandlerThread = new Thread(chatInputHandler);
        ChatInputHandler.setIsRunning(true);
        chatOutputHandler.setPortableData(portableData);
        chatOutputHandlerThread.start();

        members.add(member);
    }

    public void removeMember(Client member) {
        members.remove(member);
        member.removeServer(this);
    }

    public void addAdmin(Client admin) {
        admins.add(admin);
    }

    public void removeAdmin(Client admin) {
        admins.remove(admin);
    }

    public void banClient(Client bannedClient) {
        bannedClients.add(bannedClient);
    }

    public void unbanClient(Client bannedClient) {
        bannedClients.remove(bannedClient);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }


    /*------------------------------------------------------------------------------------*/

    public void broadCast(Object message){
        PortableData portableData = new PortableData("broadcast", message);

    }
    /*------------------------------------------------------------------------------------*/
    public String getServerName() {
        return serverName;
    }

    public String getServerMainCreator() {
        return mainCreator.getUsername();
    }

    public HashSet<Client> getMembers() {
        return (members);
    }

    public HashSet<Client> getAdmins() {
        return (admins);
    }

    public HashSet<Channel> getChannels() {
        return (channels);
    }

    public HashSet<Group> getGroups() {
        return (groups);
    }


    public Channel getChannel(String channelName) {
        for (Channel channel : channels) {
            if (channel.getChannelID().equals(channelName)) {
                return channel;
            }
        }
        return null;
    }


}
