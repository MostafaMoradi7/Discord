package com.example.clientfront;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GroupChatting extends Chat implements Runnable , HandleChat, MemberInteraction {
    private ClientHandler clientHandler;
    private Client client;
    private Group group;
    private ChatInputHandler chatInputHandler;
    private ChatOutputHandler chatOutputHandler;
    private ArrayList<GroupMessage> groupMessages;

    private boolean admin;

    public GroupChatting(ClientHandler clientHandler, Group group){
        this.clientHandler = clientHandler;
        this.client = clientHandler.returnMainClient();
        this.group = group;

        chatInputHandler = new ChatInputHandler(this, clientHandler.getClientSocket());
        chatOutputHandler = new ChatOutputHandler(this, clientHandler.getClientSocket());
        if (group.getCreator().getUsername().equals(client.getUsername())) {
            admin = true;
        } else {
            admin = false;
        }
    }

    @Override
    public void run() {
        System.out.println("com.example.clientfront.GroupChatting is running");
        Thread chatInputHandlerThread = new Thread(chatInputHandler);
        Thread chatOutputHandlerThread = new Thread(chatOutputHandler);
        ChatInputHandler.setIsRunning(true);
        chatInputHandlerThread.start();
        System.out.println("com.example.clientfront.Type any message to send: ");
        if (admin) {
            System.out.println("You are the creator of this group, you can add members to this group");
            System.out.println("com.example.clientfront.Type '$addMember' to add a member to this group");
            System.out.println("com.example.clientfront.Type '$removeMember' to remove a member from this group");
        }

        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine();
            if (admin && message.equals("$addMember")) {
                System.out.println("com.example.clientfront.Type the username of the member you want to add: ");
                do {
                    message = scanner.nextLine();
                    if (message.equals("$back")) {
                        break;
                    }
                    if (!message.trim().isEmpty() && Pattern.compile("^[a-zA-Z0-9]*$").matcher(message).matches()) {
                        break;
                    }
                } while (true);
                Client searchingClient = new Client(message, null, null, null, null);
                addMember(searchingClient);
            }
            /*
            REMOVE MEMBER FROM GROUP
             */
            else if (admin && message.equals("$removeMember")) {
                System.out.println("com.example.clientfront.Type the username of the member you want to remove: ");
                do {
                    message = scanner.nextLine();
                    if (message.equals("$back")) {
                        break;
                    }
                    if (!message.trim().isEmpty() && Pattern.compile("^[a-zA-Z0-9]*$").matcher(message).matches()) {
                        break;
                    }
                } while (true);
                Client searchingClient = new Client(message, null, null, null, null);
                removeMember(searchingClient);
            }
            else {
                GroupMessage groupMessage = new GroupMessage(TypeMVF.TEXT, client, message);
                PortableData portableData2 = new PortableData("group com.example.clientfront.Message", groupMessage);
                chatOutputHandler.setPortableData(portableData2);
                chatOutputHandlerThread.start();

            }
            if (this.message != null)
                readMessage();
        }
    }

    @Override
    public void readMessage() {
        if (((GroupMessage)message) != null && (((GroupMessage)message).getBody() instanceof String)) {
            System.out.println("**" + ((GroupMessage)message).getFrom().getUsername() + ": " + ((GroupMessage)message).getBody());
            this.message = null;
        }
    }

    @Override
    public void endChat() {
        ChatInputHandler.setIsRunning(false);
    }

    @Override
    public void addMember(Client client) {
        PortableData portableData = new PortableData("searchingClient", client);
        this.message = null;
        chatOutputHandler.setPortableData(portableData);
        Thread chatOutputHandlerThread = new Thread(chatOutputHandler);
        chatOutputHandlerThread.start();
        System.out.println("Waiting for the client to be returned from server");
        while (this.message == null) {

        }
        //IF THE OBJECT BODY IS NULL IT MEANS THE CLIENT COULD NOT BE FOUND
        // ELSE IT HAS BEEN FOUND AND IS READY TO BE ADDED TO THE GROUP
        if (this.message.getBody() == null) {
            System.out.println("The client could not be found");
        } else {
            Client clientToAdd = (Client) this.message.getBody();
            group.addMember(clientToAdd);
            portableData = new PortableData("new member added", group);
            chatOutputHandler.setPortableData(portableData);
            chatOutputHandlerThread.start();
            this.message = null;

            while (this.message == null) {
                // wait till server listens to the message
            }
            if (this.message.getBody().equals("successful")) {
                System.out.println("The client has been added to the group");
            } else {
                System.out.println("The client could not be added to the group");
            }

        }

    }

    @Override
    public synchronized void removeMember(Client client) {
        PortableData portableData = new PortableData("searchingClient", client);
        this.message = null;
        chatOutputHandler.setPortableData(portableData);
        Thread chatOutputHandlerThread = new Thread(chatOutputHandler);
        chatOutputHandlerThread.start();
        System.out.println("Waiting for the client to be returned from server");
        while (this.message == null) {

        }
        //IF THE OBJECT BODY IS NULL IT MEANS THE CLIENT COULD NOT BE FOUND
        // ELSE IT HAS BEEN FOUND AND IS READY TO BE ADDED TO THE GROUP
        if (this.message.getBody() == null) {
            System.out.println("The client could not be found");
        } else {
            Client clientToRemove = (Client) this.message.getBody();
            group.removeMember(clientToRemove);
            portableData = new PortableData("member removed", group);
            chatOutputHandler.setPortableData(portableData);
            chatOutputHandlerThread.start();
            this.message = null;

            while (this.message == null) {
                // wait till server listens to the message
            }
            if (this.message.getBody().equals("successful")) {
                System.out.println("The client has been removed from the group");
            } else {
                System.out.println("The client could not be removed from the group");
            }

        }

    }

    public void addMessage(GroupMessage groupMessage) {
        this.groupMessages.add(groupMessage);
    }
}
