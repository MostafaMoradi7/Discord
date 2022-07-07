package com.example.clientfront;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static boolean loggedIn = false;
    public static Server workingServer = null;

    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler();

        /*
                here the client logs into the system and gets a socket to communicate with the server
                                                                                                            */
        if (login(clientHandler))
            loggedIn = true;

        /*
                here a server is whether chosen or created by the client
                                                                                */

        System.out.println("""
                [1] Directs
                [2] servers
                [3] logout
                """);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> {
                System.out.println("Hello");
                interactWithDirects(clientHandler);
            }
            case "2" -> serverConnection(clientHandler);
            case "3" -> logout();
            default -> System.out.println("Invalid input");
        }

    }

//        /*
//                here the client interacts with the chosen system
//                                                                        */
//        interactWithServer(clientHandler);




    public static boolean login(ClientHandler clientHandler) {
        System.out.println("welcome to discord");
        System.out.println("""
                [1] login
                [2] register
                [3] exit
                """);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        boolean loggedIn2 = false;

        switch (choice) {
            case 1 -> {
                if (clientHandler.loginClient() != null) {
                    loggedIn2 = true;
                    loggedIn = true;
                } else
                    return false;
            }
            case 2 -> {
                if (clientHandler.registerClient() != null) {
                    System.out.println("Registration successful");
                    loggedIn2 = false;
                    if (login(clientHandler))
                        loggedIn = true;
                    else
                        loggedIn = false;
                } else
                    return false;
            }
            case 3 -> {
                System.exit(0);
            }
            default -> {
                System.out.println("invalid choice");
                choice = scanner.nextInt();
            }
        }

        return true;
    }

    public static void logout() {
        loggedIn = false;
        System.exit(0);
    }

    public static boolean createServer(ClientHandler clientHandler) {
        System.out.println("please enter a name for your server: ");
        Scanner scanner = new Scanner(System.in);
        String name;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid input, please try again");
            } else
                break;
        } while (true);
        Server newServer = new Server(clientHandler.returnMainClient(), clientHandler, name);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        Thread clientInputHandlerThread = new Thread(clientInputHandler);
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread clientOutputHandlerThread = new Thread(clientOutputHandler);

        PortableData portableData = new PortableData("new com.example.clientfront.Server", newServer);
        clientOutputHandler.setPortableData(portableData);
        clientOutputHandlerThread.start();
        clientInputHandlerThread.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for server to be created
        }
        if (response.getOrder().equals("successful")) {
            System.out.println("server created");
            clientHandler.returnMainClient().addServer(newServer);
            workingServer = newServer;
            return true;
        } else {
            System.out.println("server creation failed, please try again later");
            return false;
        }
    }

    public static boolean joinServer(ClientHandler clientHandler) {
        System.out.println("please enter name of the server you're looking for:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        do {
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid input, please try again");
                name = scanner.nextLine();
            } else
                break;
        } while (true);
        Server searchingServer = new Server(null, null, name);
        PortableData portableData = new PortableData("find server", searchingServer);

        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        Thread clientInputHandlerThread = new Thread(clientInputHandler);
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread clientOutputHandlerThread = new Thread(clientOutputHandler);

        clientOutputHandler.setPortableData(portableData);
        clientOutputHandlerThread.start();
        clientInputHandlerThread.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for server to be created
        }
        if(response.getOrder().equals("successful")){
            System.out.println("welcome to the" + name + " server");
            workingServer = searchingServer;
            clientHandler.returnMainClient().addServer(searchingServer);
            return true;
        }
        else{
            System.out.println("server not found, try searching later");
            workingServer = null;
            return false;
        }
    }

    public static boolean switchServer(ClientHandler clientHandler){
        System.out.println("please enter your destination server name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Server searchingServer = null;
        do {
            if (!name.trim().isEmpty() && !Pattern.matches("[a-zA-Z0-9]+", name)
             && ((searchingServer = clientHandler.returnMainClient().getDesiredServer(name)) != null)) {
                System.out.println("invalid input, please try again");
                name = scanner.nextLine();
            } else
                break;
        } while (true);
        PortableData portableData = new PortableData("find server", searchingServer);

        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        Thread clientInputHandlerThread = new Thread(clientInputHandler);
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread clientOutputHandlerThread = new Thread(clientOutputHandler);

        clientOutputHandler.setPortableData(portableData);
        clientOutputHandlerThread.start();
        clientInputHandlerThread.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for server to be created
        }
        if(response.getOrder().equals("successful")){
            System.out.println("welcome to the" + name + " server");
            workingServer = searchingServer;
            return true;
        }
        else{
            System.out.println("server could not switch, try searching later");
            workingServer = null;
            return false;
        }
    }

    /**
     * the server is created or joined by the client, the client is now ready to start chatting
     * and the server connection will be handled here and the client will be able to send and receive messages.
     * @param clientHandler so that it will handle the client connection and send and receive messages.
     */
    public static void serverConnection(ClientHandler clientHandler){
        System.out.println("""
                [1] create a new server
                [2] join a server
                [3] switch to another existing server
                [4] exit
                """);
        Scanner scanner = new Scanner(System.in);

        int choice = scanner.nextInt();
        while(choice != 4){
            switch(choice){
                case 1 -> {
                    if(createServer(clientHandler))
                        System.out.println("server created");
                    else
                        return;
                }
                case 2 -> {
                    if(joinServer(clientHandler))
                        System.out.println("joined server");
                    else
                        return;
                }
                case 3 -> {
                    if(switchServer(clientHandler))
                        System.out.println("switched server");
                    else
                        return;
                }
                case 4 -> {
                    logout();
                    workingServer = null;
                    login(clientHandler);
                }
                default -> {
                    System.out.println("invalid choice");
                    choice = scanner.nextInt();
                }
            }
        }
    }

//    public static void interactWithServer(ClientHandler clientHandler) {
//        boolean isAdmin = false;
//        boolean inCreator = false;
//        if (workingServer.getServerMainCreator().equals(clientHandler.returnMainClient().getUsername())) {
//            isAdmin = true;
//            inCreator = true;
//        } else if (workingServer.getAdmins().contains(clientHandler.returnMainClient())) {
//            isAdmin = true;
//        }
//
//        if (inCreator) {
//            System.out.println("""
//                    [$delete-server] delete server
//                    [$create-channel] new channel
//                    [$create-group] new group
//                    [$delete-channel] delete channel
//                    [$delete-group] delete group
//                    [$add-admin] add admin
//                    [$remove-admin] remove admin
//                    """);
//        }
//        if (isAdmin) {
//            System.out.println("""
//                    [$kick] kick a user out
//                    [$ban] ban a user
//                    [$unban] unban a user
//
//                    """);
//        }
//
//        System.out.println("""
//                [1] list channels
//                [2] list groups
//                [3] Exit com.example.clientfront.Server
//                """);
//
//        Scanner scanner = new Scanner(System.in);
//        String choice = scanner.nextLine();
//        boolean continueLoop = true;
//        while (!choice.equals("3") && continueLoop) {
//            switch (choice) {
//                case "1" -> {
//                    Channel channel = channelChosen(clientHandler);
//                    ChannelChatting channelChatting = new ChannelChatting(channel, clientHandler.getClientSocket());
//                    Thread channelChattingThread = new Thread(channelChatting);
//                    channelChattingThread.start();
//                    continueLoop = false;
//                }
//                case "2" -> {
//                    Group group = groupChosen(clientHandler);
//                    GroupChatting groupChatting = new GroupChatting(clientHandler,group);
//                    Thread groupChattingThread = new Thread(groupChatting);
//                    groupChattingThread.start();
//                    continueLoop = false;
//                }
//
//                case "$delete-server" -> {
//                    if (isAdmin) {
//                        if (deleteServer(clientHandler)){
//                            System.out.println("server deleted");
//                        }else
//                            System.out.println("server could not be deleted");
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$create-channel" -> {
//                    if (isAdmin) {
//                        if (createChannel(clientHandler)) {
//                            System.out.println("channel created");
//                        } else {
//                            System.out.println("channel could not be created");
//                        }
//                        continueLoop = false;
//                    }else {
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$create-group" -> {
//                    if (isAdmin) {
//                        if (createGroup(clientHandler)) {
//                            System.out.println("group created");
//                        } else {
//                            System.out.println("group could not be created");
//                        }
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$delete-channel" -> {
//                    if (isAdmin) {
//                        if (deleteChannel(clientHandler))
//                            System.out.println("channel deleted");
//                        else
//                            System.out.println("channel not found or could not be deleted");
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$delete-group" -> {
//                    if (isAdmin) {
//                        if (deleteGroup(clientHandler))
//                            System.out.println("group deleted");
//                        else
//                            System.out.println("group not found or could not be deleted");
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$add-admin" -> {
//                    if (isAdmin) {
//                        if (addAdmin(clientHandler))
//                            System.out.println("admin added");
//                        else
//                            System.out.println("admin could not be added");
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$remove-admin" -> {
//                    if (isAdmin) {
//                        if (removeAdmin(clientHandler))
//                            System.out.println("admin removed");
//                        else
//                            System.out.println("admin could not be removed");
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$kick" -> {
//                    if (isAdmin) {
//                        if (kick(clientHandler))
//                            System.out.println("user kicked");
//                        else
//                            System.out.println("user could not be kicked");
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$ban" -> {
//                    if (isAdmin) {
//                        if (ban(clientHandler))
//                            System.out.println("user banned");
//                        else
//                            System.out.println("user could not be banned");
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                case "$unban" -> {
//                    if (isAdmin) {
//                        if (unban(clientHandler))
//                            System.out.println("user unbanned");
//                        else
//                            System.out.println("user could not be unbanned");
//                        continueLoop = false;
//                    }else{
//                        System.out.println("you are not an admin");
//                    }
//                }
//                default -> {
//                    System.out.println("invalid choice, try again: ");
//                    choice = scanner.nextLine();
//                }
//            }
//        }
//
//        logout();
//        workingServer = null;
//        login(clientHandler);
//    }

    private static Channel channelChosen(ClientHandler clientHandler){
        Channel chosenChannel = null;
        Scanner scanner = new Scanner(System.in);
        if (workingServer.getChannels().size() == 0) {
            System.out.println("no channels");
            return null;
        }
        System.out.println("list of channels:");
        int i = 1;
        for (Channel channel : workingServer.getChannels()) {
            System.out.println((i++) + "." +channel.getChannelName());
        }
        System.out.println("please enter the name of the channel you want to join:");
        String name = scanner.nextLine();
        for (Channel channel : workingServer.getChannels()) {
            if (channel.getChannelName().equals(name)) {
                chosenChannel = channel;
            }
        }

        PortableData data = new PortableData("chosen channel", chosenChannel);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            return chosenChannel;
        }
        else{
            System.out.println("error");
            return null;
        }
    }

    private static Group groupChosen(ClientHandler clientHandler){
        Group chosenGroup = null;
        Scanner scanner = new Scanner(System.in);
        if (workingServer.getGroups().size() == 0) {
            System.out.println("no groups");
            return null;
        }
        System.out.println("list of groups:");
        int i = 1;
        for (Group group : workingServer.getGroups()) {
            System.out.println((i++) + "." +group.getGroupName());
        }
        System.out.println("please enter the name of the group you want to join:");
        String name = scanner.nextLine();
        for (Group group : workingServer.getGroups()) {
            if (group.getGroupName().equals(name)) {
                chosenGroup = group;
            }
        }

        PortableData data = new PortableData("chosen group", chosenGroup);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            return chosenGroup;
        }
        else{
            System.out.println("error");
            return null;
        }
    }

    private static boolean deleteServer(ClientHandler clientHandler) {
        System.out.println("Are you sure you want to delete the server? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if (choice.equals("y")) {
            PortableData data = new PortableData("delete server", workingServer);
            ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
            ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
            Thread tOUT = new Thread(clientOutputHandler);
            Thread tIN = new Thread(clientInputHandler);
            clientOutputHandler.setPortableData(data);
            tOUT.start();
            tIN.start();

            PortableData response;
            while ((response = clientInputHandler.getPortableData()) == null) {
                //wait for response
            }
            if (response.getOrder().equals("successful")) {
                System.out.println("server deleted");
                workingServer = null;
                return true;
            } else {
                System.out.println("error");
                return false;
            }
        }
        else
            return false;
    }

    private static boolean createChannel(ClientHandler clientHandler){
        System.out.println("please enter the name of the channel you want to create:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        do {
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
                name = scanner.nextLine();
            } else
                break;
        } while (true);
        Channel newChannel = new Channel(null ,name,clientHandler.returnMainClient());

        PortableData data = new PortableData("create channel", newChannel);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("channel created");
            newChannel = (Channel) response.getObject();
            workingServer.addChannel(newChannel);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }

    }

    private static boolean createGroup(ClientHandler clientHandler){
        System.out.println("please enter the name of the group you want to create:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        do {
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
                name = scanner.nextLine();
            } else
                break;
        } while (true);
        Group newGroup = new Group(null ,name,clientHandler.returnMainClient());

        PortableData data = new PortableData("create group", newGroup);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("group created");
            newGroup = (Group) response.getObject();
            workingServer.addGroup(newGroup);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }

    }

    private static boolean deleteChannel(ClientHandler clientHandler){
        System.out.println("Please enter the name of the channel you want to delete:");
        Scanner scanner = new Scanner(System.in);
        String name ;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
            } else
                break;
        } while (true);
        Channel channelD = null;

        for (Channel channel : workingServer.getChannels()) {
            if (channel.getChannelName().equals(name)) {
                channelD = channel;
            }
        }

        PortableData data = new PortableData("delete channel", channelD);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("channel deleted");
            workingServer.removeChannel(channelD);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }
    }

    private static boolean deleteGroup(ClientHandler clientHandler){
        System.out.println("Please enter the name of the group you want to delete:");
        Scanner scanner = new Scanner(System.in);
        String name ;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
            } else
                break;
        } while (true);
        Group groupD = null;

        for (Group group : workingServer.getGroups()) {
            if (group.getGroupName().equals(name)) {
                groupD = group;
            }
        }

        PortableData data = new PortableData("delete group", groupD);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("group deleted");
            workingServer.removeGroup(groupD);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }
    }

    private static boolean addAdmin(ClientHandler clientHandler){
        System.out.println("Please enter the username of the user you want to promote to admin:");
        Scanner scanner = new Scanner(System.in);
        String name ;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
            } else
                break;
        } while (true);
        Client client = null;
        for (Client client1 : workingServer.getMembers()) {
            if (client1.getUsername().equals(name)) {
                client = client1;
            }
        }
        if(client == null){
            System.out.println("user not found");
            return false;
        }
        PortableData data = new PortableData("add admin", client);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();
        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("admin added");
            client = (Client) response.getObject();
            workingServer.addAdmin(client);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }
    }

    private static boolean removeAdmin(ClientHandler clientHandler){
        System.out.println("Please enter the username of the user you want to demote to member:");
        Scanner scanner = new Scanner(System.in);
        String name ;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
            } else
                break;
        } while (true);
        Client client = null;
        for (Client client1 : workingServer.getMembers()) {
            if (client1.getUsername().equals(name)) {
                client = client1;
            }
        }
        if(client == null){
            System.out.println("user not found");
            return false;
        }
        PortableData data = new PortableData("remove admin", client);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();
        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("admin removed");
            client = (Client) response.getObject();
            workingServer.removeAdmin(client);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }
    }

    private static boolean kick(ClientHandler clientHandler){
        System.out.println("Please enter the username of the user you want to kick:");
        Scanner scanner = new Scanner(System.in);
        String name ;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
            } else
                break;
        } while (true);
        Client client = null;
        for (Client client1 : workingServer.getMembers()) {
            if (client1.getUsername().equals(name)) {
                client = client1;
            }
        }
        if(client == null){
            System.out.println("user not found");
            return false;
        }
        PortableData data = new PortableData("kick", client);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();
        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("user kicked");
            client = (Client) response.getObject();
            workingServer.removeMember(client);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }
    }

    private static boolean ban(ClientHandler clientHandler){
        System.out.println("Please enter the username of the user you want to ban:");
        Scanner scanner = new Scanner(System.in);
        String name ;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
            } else
                break;
        } while (true);
        Client client = null;
        for (Client client1 : workingServer.getMembers()) {
            if (client1.getUsername().equals(name)) {
                client = client1;
            }
        }
        if(client == null){
            System.out.println("user not found");
            return false;
        }
        PortableData data = new PortableData("ban", client);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();
        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("user banned");
            client = (Client) response.getObject();
            workingServer.banClient(client);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }
    }

    private static boolean unban(ClientHandler clientHandler){
        System.out.println("Please enter the username of the user you want to unban:");
        Scanner scanner = new Scanner(System.in);
        String name ;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid name, please try again: ");
            } else
                break;
        } while (true);
        Client client = null;
        for (Client client1 : workingServer.getBannedClients()) {
            if (client1.getUsername().equals(name)) {
                client = client1;
            }
        }
        if(client == null){
            System.out.println("user not found");
            return false;
        }
        PortableData data = new PortableData("unban", client);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread tOUT = new Thread(clientOutputHandler);
        Thread tIN = new Thread(clientInputHandler);
        clientOutputHandler.setPortableData(data);
        tOUT.start();
        tIN.start();
        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for response
        }
        if(response.getOrder().equals("successful")){
            System.out.println("user unbanned");
            client = (Client) response.getObject();
            workingServer.unbanClient(client);
            return true;
        }
        else{
            System.out.println("error");
            return false;
        }
    }

    private static void interactWithDirects(ClientHandler clientHandler){
        System.out.println("""
                [1] new private chat
                [2] chat lists
                """);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice == 1){
            PortableData data = clientHandler.sendAndReceive(new PortableData("new private",clientHandler.findClient()));
            if (data == null){
                System.out.println("No such client found");
            }else{

            }
        }
        PortableData data = clientHandler.sendAndReceive(new PortableData("private", clientHandler.returnMainClient()));
        System.out.println(data.getObject() + data.getOrder());
    }
}