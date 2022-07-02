import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ChannelChatting extends Chat implements Runnable, HandleChat, MemberInteraction{
    private Client client;
    private Socket clientSocket;
    private Channel channel;
    private ChatInputHandler chatInputHandler;
    private ChatOutputHandler chatOutputHandler;

    public ChannelChatting(Channel channel, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.channel = channel;
        chatInputHandler = new ChatInputHandler(this, clientSocket);
        chatOutputHandler = new ChatOutputHandler(this, clientSocket);
    }

    @Override
    public void run() {
        Thread chatInputThread = new Thread(chatInputHandler);
        Thread chatOutputThread = new Thread(chatOutputHandler);
        ChatInputHandler.setIsRunning(true);
        chatInputThread.start();
        Scanner scanner = new Scanner(System.in);

        System.out.println("type '$back' to go back");
        while (true) {

            String message = scanner.nextLine();
            if (message.equals("$back")) {
                endChat();
                break;
            }

            if (isAdmin() || client.getUsername().equals(channel.getCreator().getUsername())){
                if (message.equals("$kick")) {
                    Client client = getUserToProcess(scanner);
                    kickMember(client);
                    continue;
                }
                if (message.equals("$ban")) {
                    Client client = getUserToProcess(scanner);
                    banMember(client);
                    continue;
                }
                if (message.equals("$unban")) {
                    Client client = getUserToProcess(scanner);
                    unbanMember(client);
                    continue;
                }
                if (message.equals("$promote")) {
                    Client client = getUserToProcess(scanner);
                    promoteMember(client);
                    continue;
                }
                if (message.equals("$demote")) {
                    Client client = getUserToProcess(scanner);
                    demoteMember(client);
                    continue;
                }
            }

            if (message.equals("$leave")){
                PortableData portableData = new PortableData("leave", channel);
                chatOutputHandler.setPortableData(portableData);
                chatOutputThread.start();
                endChat();
                break;
            }
            readMessage();
        }
    }

    private Client getUserToProcess(Scanner scanner) {
        String message;
        System.out.println("type the username of the member you want to kick");
        Client client;
        do {
            message = scanner.nextLine();
            if(!message.trim().isEmpty() && Pattern.matches("^[a-zA-Z0-9]+$", message)){
                client = new Client(message, null, null, null, null);
                break;
            }
            System.out.println("Invalid username format");
        }while(true);
        return client;
    }

    private synchronized void kickMember(Client client) {
        PortableData portableData = new PortableData("kick", client);
        chatOutputHandler.setPortableData(portableData);
        Thread chatOutputThread = new Thread(chatOutputHandler);
        chatOutputThread.start();
        Thread chatInputThread = new Thread(chatInputHandler);
        ChatInputHandler.setIsRunning(true);
        this.message = null;
        chatInputThread.start();
        while (this.message == null){

        }
        if(((ChannelMessage)this.message).getBody().equals("success")){
            client = (Client) this.message.getBody();
            System.out.println("member kicked out of the group");
            this.channel.removeMember(client);
        }
        else{
            System.out.println("member could not be kicked");
        }
    }

    private synchronized void banMember(Client client){
        PortableData portableData = new PortableData("ban", client);
        chatOutputHandler.setPortableData(portableData);
        Thread chatOutputThread = new Thread(chatOutputHandler);
        chatOutputThread.start();
        Thread chatInputThread = new Thread(chatInputHandler);
        ChatInputHandler.setIsRunning(true);
        this.message = null;
        chatInputThread.start();
        while (this.message == null){

        }
        if(((ChannelMessage)this.message).getBody().equals("success")){
            client = (Client) this.message.getBody();
            System.out.println("member banned from the group");
            this.channel.banUser(client);
        }
        else{
            System.out.println("member could not be banned");
        }
    }

    private synchronized void unbanMember(Client client){
        PortableData portableData = new PortableData("unban", client);
        chatOutputHandler.setPortableData(portableData);
        Thread chatOutputThread = new Thread(chatOutputHandler);
        chatOutputThread.start();
        Thread chatInputThread = new Thread(chatInputHandler);
        this.message = null;
        ChatInputHandler.setIsRunning(true);
        chatInputThread.start();
        while (this.message == null){

        }
        if(((ChannelMessage)this.message).getBody().equals("success")){
            client = (Client) this.message.getBody();
            System.out.println("member unbanned from the group");
            this.channel.unbanUser(client);
        }
        else{
            System.out.println("member could not be unbanned");
        }
    }

    private synchronized void promoteMember(Client client){
        PortableData portableData = new PortableData("promote", client);
        chatOutputHandler.setPortableData(portableData);
        Thread chatOutputThread = new Thread(chatOutputHandler);
        chatOutputThread.start();
        Thread chatInputThread = new Thread(chatInputHandler);
        this.message = null;
        ChatInputHandler.setIsRunning(true);
        chatInputThread.start();
        while (this.message == null){

        }
        if(((ChannelMessage)this.message).getBody().equals("success")){
            client = (Client) this.message.getBody();
            System.out.println("member promoted to admin");
            this.channel.addAdmin(client);
        }
        else{
            System.out.println("member could not be promoted");
        }
    }

    private synchronized void demoteMember(Client client){
        PortableData portableData = new PortableData("demote", client);
        chatOutputHandler.setPortableData(portableData);
        Thread chatOutputThread = new Thread(chatOutputHandler);
        chatOutputThread.start();
        Thread chatInputThread = new Thread(chatInputHandler);
        this.message = null;
        ChatInputHandler.setIsRunning(true);
        chatInputThread.start();
        while (this.message == null){

        }
        if(((ChannelMessage)this.message).getBody().equals("success")){
            client = (Client) this.message.getBody();
            System.out.println("member demoted to member");
            this.channel.removeAdmin(client);
        }
        else{
            System.out.println("member could not be demoted");
        }
    }

    private boolean isAdmin(){
        return channel.getAdmins().contains(client);
    }

    @Override
    public void readMessage() {
        if (message != null && message.getBody() instanceof String){
            System.out.println("**" + ((ChannelMessage)message).getAdmin() +": " + ((ChannelMessage)message).getBody());
            channel.addMessage((ChannelMessage) message.getBody());
            message = null;
        }
    }

    @Override
    public void endChat() {
        ChatInputHandler.setIsRunning(false);
    }

    @Override
    public void addMember(Client client) {
        PortableData portableData = new PortableData("add member", client);
        chatOutputHandler.setPortableData(portableData);
        Thread chatOutputHandlerThread = new Thread(chatOutputHandler);
        chatOutputHandlerThread.start();

        if (message != null && message.getBody() instanceof String ){
            if (((String)message.getBody()).equals("success")){
                System.out.println("Member added successfully");
                channel.addMember(client);
            }
            else{
                System.out.println("Member already exists");
            }
        }
    }

    @Override
    public void removeMember(Client client) {

    }
}
