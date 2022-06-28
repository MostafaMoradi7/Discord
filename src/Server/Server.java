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

public class Server extends Chat implements Runnable{
    private Client mainCreator;
    private ClientHandler clientHandler;
    private ArrayList<Client> members;
    private ArrayList<Client> admins;
    private ArrayList<Client> bannedClients;
    private ArrayList<Group> groups;
    private ArrayList<Channel> channels;
    private ChatOutputHandler chatOutputHandler;
    private ChatInputHandler chatInputHandler;


    public Server(Client mainCreator, ClientHandler clientHandler) {
        this.mainCreator = mainCreator;
        members = new ArrayList<>();
        admins = new ArrayList<>();
        bannedClients = new ArrayList<>();
        groups = new ArrayList<>();
        channels = new ArrayList<>();
        members.add(mainCreator);
        admins.add(mainCreator);

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
    @Override
    public void run() {

    }
}
