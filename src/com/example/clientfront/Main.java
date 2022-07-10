package com.example.clientfront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static boolean loggedIn = false;

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Discord");
        ServerDiscord workingServer = null;
        ClientHandler clientHandler = new ClientHandler();
        ClientHandlerChat chatHandler = new ClientHandlerChat();
//--------------------------------------------------Login--------------------------------------------------\\
        do {
            if (login(clientHandler, chatHandler, workingServer)) {
                loggedIn = true;
            }
        }while (!loggedIn) ;

//--------------------------------------------------Main Menu--------------------------------------------------\\

        Thread clientHandlerChatThread = new Thread(chatHandler);
        clientHandlerChatThread.start();
        chatHandler.sendMessage(new PortableData("main client", clientHandler.returnMainClient()));

        do {
            if (!loggedIn) {
                login(clientHandler, chatHandler, workingServer);
                if (!loggedIn)
                    System.out.println("Have a good time... Bye!");
            }
        } while (loggedIn);
//---------------------------------------------------End---------------------------------------------------\\
    }

    public static void mainMenu(ClientHandler clientHandler, ClientHandlerChat chatHandler, ServerDiscord workingServer) {
        System.out.println("""
                [1] Directs
                [2] servers
                [3] logout
                """);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> {
                interactWithDirects(clientHandler, chatHandler, workingServer);
            }
            case "2" -> {
                workingServer = serverConnection(clientHandler, chatHandler, workingServer);
                interActWithServer(clientHandler, chatHandler, workingServer);
            }
            case "3" -> {
                logout();
                loggedIn = false;
                login(clientHandler, chatHandler, workingServer);
            }
            default -> {
                System.out.println("Invalid input");
                mainMenu(clientHandler, chatHandler, workingServer);
            }
        }
    }


    public static boolean login(ClientHandler clientHandler, ClientHandlerChat chatHandler, ServerDiscord workingServer) {
        System.out.println("""
                [1] login
                [2] register
                [3] exit
                """);
        Scanner scanner = new Scanner(System.in);
        int choice;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            return login(clientHandler, chatHandler, workingServer);
        }
        switch (choice) {
            case 1 -> {
                if (clientHandler.loginClient() != null) {
                    loggedIn = true;
                    mainMenu(clientHandler, chatHandler, workingServer);
                } else
                    return false;
            }
            case 2 -> {
                if (clientHandler.registerClient() != null) {
                    System.out.println("Registration successful");
                    if (login(clientHandler, chatHandler, workingServer)) {
                        loggedIn = true;
                        mainMenu(clientHandler, chatHandler, workingServer);
                    } else
                        loggedIn = false;
                } else {
                    return false;
                }
            }
            case 3 -> {
                System.out.println("Are you sure you want to exit? (y/n)");
                String input = scanner.nextLine();
                if (input.equals("y")) {
                    System.out.println("Have a good time... Bye!");
                    loggedIn = false;
                    System.exit(0);
                } else {
                    login(clientHandler, chatHandler, workingServer);
                }
            }
            default -> {
                try {
                    throw new InvalidInput("Invalid input, try again");
                } catch (InvalidInput e) {
                    System.out.println(e.getMessage());
                    return login(clientHandler, chatHandler, workingServer);
                }
            }
        }

        return true;
    }

    public static void logout() {
        loggedIn = false;
    }

    public static ServerDiscord createServer(ClientHandler clientHandler) {
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
        ServerDiscord newServer = new ServerDiscord(null, name, clientHandler.returnMainClient());
        PortableData portableData = new PortableData("new server", newServer);

        PortableData response;
        while ((response = clientHandler.sendAndReceive(portableData)) == null) {
            //wait for server to be created
        }
        if (response.getOrder().equals("200")) {
            newServer = (ServerDiscord) response.getObject();
            return newServer;
        } else {
            return null;
        }
    }

    //    public static boolean joinServer(ClientHandler clientHandler) {
//        System.out.println("please enter name of the server you're looking for:");
//        Scanner scanner = new Scanner(System.in);
//        String name = scanner.nextLine();
//        do {
//            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
//                System.out.println("invalid input, please try again");
//                name = scanner.nextLine();
//            } else
//                break;
//        } while (true);
//        Server searchingServer = new Server(null, null, name, 0);
//        PortableData portableData = new PortableData("find server", searchingServer);
//
//
//        PortableData response;
//        while ((response = clientHandler.sendAndReceive(portableData)) == null) {
//            //wait for server to be created
//        }
//        if(response.getOrder().equals("successful")){
//            System.out.println("welcome to the" + name + " server");
//            workingServer = searchingServer;
//            clientHandler.returnMainClient().addServer(searchingServer);
//            return true;
//        }
//        else{
//            System.out.println("server not found, try searching later");
//            workingServer = null;
//            return false;
//        }
//    }
//
//    public static boolean switchServer(ClientHandler clientHandler){
//        System.out.println("please enter your destination server name:");
//        Scanner scanner = new Scanner(System.in);
//        String name = scanner.nextLine();
//        Server searchingServer = null;
//        do {
//            if (!name.trim().isEmpty() && !Pattern.matches("[a-zA-Z0-9]+", name)
//             && ((searchingServer = clientHandler.returnMainClient().getDesiredServer(name)) != null)) {
//                System.out.println("invalid input, please try again");
//                name = scanner.nextLine();
//            } else
//                break;
//        } while (true);
//        PortableData portableData = new PortableData("find server", searchingServer);
//
//        PortableData response;
//        while ((response = clientHandler.sendAndReceive(portableData)) == null) {
//            //wait for server to be created
//        }
//        if(response.getOrder().equals("successful")){
//            System.out.println("welcome to the" + name + " server");
//            workingServer = searchingServer;
//            return true;
//        }
//        else{
//            System.out.println("server could not switch, try searching later");
//            workingServer = null;
//            return false;
//        }
//    }
//
//    /**
//     * the server is created or joined by the client, the client is now ready to start chatting
//     * and the server connection will be handled here and the client will be able to send and receive messages.
//     * @param clientHandler so that it will handle the client connection and send and receive messages.
//     */
    public static ServerDiscord serverConnection(ClientHandler clientHandler, ClientHandlerChat chatHandler, ServerDiscord workingServer) {
        System.out.println("""
                [1] create a new server
                [2] join a server
                [3] All servers
                [4] exit
                """);
        Scanner scanner = new Scanner(System.in);

        int choice;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("input does not match, try again");
            return serverConnection(clientHandler, chatHandler, workingServer);
        }
        while (choice != 4) {
            switch (choice) {
                case 1 -> {
                    ServerDiscord serverDiscord = createServer(clientHandler);
                    if (serverDiscord != null) {
                        System.out.println("server created");
                        return serverDiscord;
                    } else {
                        System.out.println("something went wrong, please try again later.");
                        return null;
                    }
                }
                case 3 -> {
                    ArrayList<ServerDiscord> allServers;
                    PortableData response = clientHandler.sendAndReceive(new PortableData("all servers", clientHandler.returnMainClient()));
                    if (response.getOrder().equals("400")) {
                        System.out.println("servers not found, try again later!");
                    } else {
                        int i = 1;
                        allServers = (ArrayList<ServerDiscord>) response.getObject();
                        if (allServers.size() == 0) {
                            System.out.println("no servers created yet!");
                            serverConnection(clientHandler, chatHandler, workingServer);
                        } else {
                            for (ServerDiscord x : allServers) {
                                System.out.println((i++) + "." + x);
                            }
                            int choice2 = scanner.nextInt();
                            if (choice2 <= allServers.size()) {
                                ServerDiscord chosen = (ServerDiscord) clientHandler.sendAndReceive(new PortableData("chosen server", allServers.get(choice2 - 1))).getObject();
                                if (chosen != null)
                                    return chosen;
                                else {
                                    System.out.println("server could not be found!");
                                    return null;
                                }
                            } else {
                                System.out.println("invalid choice, try again later.");
                            }
                        }

                    }
                }

//                case 2 -> {
//                    if(joinServer(clientHandler))
//                        System.out.println("joined server");
//                    else
//                        return;
//                }
//                case 3 -> {
//                    if(switchServer(clientHandler))
//                        System.out.println("switched server");
//                    else
//                        return;
//                }
                default -> {
                    try {
                        throw new InvalidInput("invalid input, try again");
                    } catch (InvalidInput e) {
                        return serverConnection(clientHandler, chatHandler, workingServer);
                    }
                }
            }
            mainMenu(clientHandler, chatHandler, workingServer);
        }
        return null;
    }

    public static void interActWithServer(ClientHandler clientHandler, ClientHandlerChat chatHandler, ServerDiscord workingServer) {
        System.out.println("""
                [1] groups
                [2] add a group
                [3] delete a group
                [4] add a member
                [5] kick a member
                [6] back
                """);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 2 -> {
                Group newGroup = createGroup(clientHandler, workingServer);
                if (newGroup != null) {
                    System.out.println(newGroup);
                }
                interActWithServer(clientHandler, chatHandler, workingServer);
            }
            case 4 -> {
                addMemberToServer(clientHandler, workingServer);
                interActWithServer(clientHandler, chatHandler, workingServer);
            }
            case 6 -> {
                mainMenu(clientHandler, chatHandler, workingServer);
            }
        }

    }

    public static void addMemberToServer(ClientHandler clientHandler, ServerDiscord workingServer) {
        Client clientToAdd = clientHandler.findClient();
        if (clientToAdd != null) {
            ServerDiscord serverDiscord = new ServerDiscord(workingServer.getServerID(), workingServer.getName(), workingServer.getCreator());
            HashSet<Client> members = new HashSet<>();
            members.add(clientToAdd);
            serverDiscord.addMembers(members);
            if ((clientHandler.sendAndReceive(new PortableData("new member", serverDiscord))).getOrder().equals("200")) {
                System.out.println("member added successfully");
            } else
                System.out.println("member didn't added!");
        }
    }


    private static Group createGroup(ClientHandler clientHandler, ServerDiscord workingServer) {
        System.out.println("please enter the name of the group you want to create:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        do {
            if (name.trim().isEmpty()) {
                System.out.println("try again: ");
                name = scanner.nextLine();
            } else
                break;
        } while (true);
        Group newGroup = new Group(workingServer.getServerID(), null, name, clientHandler.returnMainClient());

        PortableData data = new PortableData("new group", newGroup);
        PortableData response;
        response = clientHandler.sendAndReceive(data);
        if (response.getOrder().equals("200")) {
            System.out.println("group created");
            return (Group) response.getObject();

        } else {
            System.out.println("group didn't created!");
            return null;
        }

    }


    private static void interactWithDirects(ClientHandler clientHandler, ClientHandlerChat chatHandler, ServerDiscord workingServer) {

        System.out.println("""
                [1] new private chat
                [2] chat lists
                [3] friends
                [4] back
                """);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice == 1) {
            PortableData privateChatData = clientHandler.sendAndReceive(new PortableData("new private chat", new PrivateChat(clientHandler.returnMainClient(), clientHandler.findClient())));
            PrivateChat privateChat = (PrivateChat) privateChatData.getObject();
            System.out.println(privateChat);
            if (privateChatData == null) {
                System.out.println("No such client found");
            } else {
                //TODO: chat
            }
        } else if (choice == 2) {
            ArrayList<PrivateChat> allPVs = new ArrayList<>();
            allPVs = (ArrayList<PrivateChat>) clientHandler.sendAndReceive(
                    new PortableData("private", clientHandler.returnMainClient())).getObject();
            Client secondClient = null;
            if (allPVs.size() != 0) {
                System.out.println("All your chats:");
                int i = 1;
                for (PrivateChat x : allPVs) {
                    if (x.getClientONE().getUsername().equals(clientHandler.returnMainClient().getUsername())) {
                        secondClient = x.getClientTWO();
                    } else
                        secondClient = x.getClientONE();
                    System.out.println((i++) + "." + secondClient.getUsername());
                }
                System.out.println("*choose one: ");
                int choice2;
                do {
                    choice2 = scanner.nextInt();
                    if ((choice2 - 1) > allPVs.size())
                        System.out.println("invalid input please try again...");
                    else break;
                } while (true);
                PortableData chosenChat = new PortableData("chosen chat", allPVs.get(choice2 - 1));
                PortableData response = clientHandler.sendAndReceive(chosenChat);
//                System.out.println(((PrivateChat)response.getObject()).getMessages());
                if (response.getOrder().equals(400)) {
                    System.out.println("something went wrong, please try again later");
                    interactWithDirects(clientHandler, chatHandler, workingServer);
                } else {
                    PrivateChat privateChat = (PrivateChat) response.getObject();

                    System.out.println("you are now in " + secondClient.getUsername() + "'s chat.");
                    System.out.println("enter any word to send message (enter $back to return)");
                    String message;
                    scanner.nextLine();
                    do {
                        message = scanner.nextLine();

                        if (message.contains("$back")) {
                            mainMenu(clientHandler, chatHandler, workingServer);
                        } else if (message.contains("$file")) {
                            System.out.println("enter file path: ");
                            message = scanner.nextLine();
                            boolean block = true;
                            while (block) {
                                try {
                                    FileInputStream file = new FileInputStream(message);
                                    File file2 = new File(message);
                                    byte[] bytes = file.readAllBytes();
                                    PrivateChatMessage pvMessage = new PrivateChatMessage(allPVs.get(choice2 - 1).getChatID(), clientHandler.returnMainClient()
                                            , secondClient, LocalDateTime.now().toString(), file2.getName(), TypeMVF.FILE);
                                    pvMessage.setBuffer(bytes);
                                    PortableData newMessage = new PortableData("send private file", pvMessage);
                                    chatHandler.sendMessage(newMessage);
                                    block = false;
                                } catch (IOException e) {
                                    System.out.println("file not found, try entering the path again: (enter $back to skip sending file)");
                                    message = scanner.nextLine();
                                    if (message.contains("$back")) {
                                        break;
                                    }
                                    block = true;
                                }
                            }
                        } else {
                            PrivateChatMessage pvMessage = new PrivateChatMessage(allPVs.get(choice2 - 1).getChatID(), clientHandler.returnMainClient()
                                    , secondClient, LocalDateTime.now().toString(), message, TypeMVF.TEXT);

                            PortableData newMessage = new PortableData("private chat message", pvMessage);
                            chatHandler.sendMessage(newMessage);
                        }
                    } while (true);
                }
            } else {
                System.out.println("you don't have any chats now.");
                interactWithDirects(clientHandler, chatHandler, workingServer);
            }

        } else if (choice == 3) {
            System.out.println("""
                    [1] new friend
                    [2] friend list
                    [3] friend requests
                    [4] back
                    """);
            int choice2;
            do {
                try {
                    choice2 = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("invalid input, try again: ");
                    choice2 = scanner.nextInt();
                }
            } while (choice2 != 1 && choice2 != 2 && choice2 != 3 && choice2 != 4);
            if (choice2 == 1) {
                System.out.println("search for friend existence: ");
                Client newFriend = clientHandler.findClient();
                RequestFriend requestFriend = new RequestFriend(0, clientHandler.returnMainClient(), newFriend, 0, LocalDateTime.now().toString());
                PortableData response = clientHandler.sendAndReceive(new PortableData("friend request", requestFriend));
                if (response.getOrder().equals("200")) {
                    System.out.println("friend request sent");
                } else {
                    System.out.println("friend request not sent, try sending another time.");
                }
            } else if (choice2 == 2) {
                ArrayList<Client> friends;
                PortableData response = clientHandler.sendAndReceive(new PortableData("all friends", clientHandler.returnMainClient()));
                if (response.getOrder().equals("200")) {
                    friends = (ArrayList<Client>) response.getObject();
                    if (friends.size() == 0) {
                        System.out.println("you don't have any friends now.");
                        interactWithDirects(clientHandler, chatHandler, workingServer);
                    } else {
                        System.out.println("all your friends:");
                        int i = 1;
                        for (Client x : friends) {
                            System.out.println((i++) + "." + x.getUsername());
                        }
                        System.out.println("*choose one: ");
                        int choice3;
                        do {
                            try {
                                choice3 = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("invalid input, try again: ");
                                scanner = new Scanner(System.in);
                                choice3 = scanner.nextInt();
                            }
                            if ((choice3 - 1) > friends.size())
                                System.out.println("invalid input please try again...");
                            else break;
                        } while (true);
                        Client chosenFriend = friends.get(choice3 - 1);
                        PrivateChat privateChat = new PrivateChat(clientHandler.returnMainClient(), chosenFriend);
                        response = clientHandler.sendAndReceive(new PortableData("chosen chat", privateChat));
                        if (response.getOrder().equals("200")) {
                            System.out.println("you are now in " + chosenFriend.getUsername() + "'s chat.");
                            System.out.println("enter any word to send message (enter $back to return)");
                            String message;
                            scanner.nextLine();
                            do {
                                message = scanner.nextLine();

                                if (message.contains("$back")) {
                                    mainMenu(clientHandler, chatHandler, workingServer);
                                } else if (message.contains("$file")) {
                                    System.out.println("enter file path: ");
                                    message = scanner.nextLine();
                                    while (true) {
                                        try {
                                            FileInputStream file = new FileInputStream(message);
                                            File file2 = new File(message);
                                            byte[] bytes = file.readAllBytes();
                                            PrivateChatMessage pvMessage = new PrivateChatMessage(privateChat.getChatID(), clientHandler.returnMainClient()
                                                    , chosenFriend, LocalDateTime.now().toString(), file2.getName(), TypeMVF.FILE);
                                            pvMessage.setBuffer(bytes);
                                            PortableData newMessage = new PortableData("send private file", pvMessage);
                                            chatHandler.sendMessage(newMessage);
                                        } catch (IOException e) {
                                            System.out.println("file not found, try entering the path again: (enter $back to skip sending file)");
                                            message = scanner.nextLine();
                                            if (message.contains("$back")) {
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    PrivateChatMessage pvMessage = new PrivateChatMessage(privateChat.getChatID(), clientHandler.returnMainClient()
                                            , chosenFriend, LocalDateTime.now().toString(), message, TypeMVF.TEXT);

                                    PortableData newMessage = new PortableData("private chat message", pvMessage);
                                    chatHandler.sendMessage(newMessage);
                                }
                            } while (true);

                        }
                    }
                }
            } else if (choice2 == 3) {
                ArrayList<RequestFriend> requests;
                PortableData response = clientHandler.sendAndReceive(new PortableData("all requests", clientHandler.returnMainClient()));
                if (response.getOrder().equals("200")) {
                    requests = (ArrayList<RequestFriend>) response.getObject();
                    if (requests.size() == 0) {
                        System.out.println("you don't have any friend requests now.");
                        interactWithDirects(clientHandler, chatHandler, workingServer);
                    } else {
                        System.out.println("all your friend requests:");
                        int i = 1;
                        for (RequestFriend x : requests) {
                            System.out.println((i++) + "." + x.getSender().getUsername());
                        }
                        System.out.println("*choose one: ");
                        int choice3;
                        do {
                            try {
                                choice3 = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("invalid input, try again: ");
                                scanner = new Scanner(System.in);
                                choice3 = scanner.nextInt();
                            }
                            if ((choice3 - 1) > requests.size())
                                System.out.println("invalid input please try again...");
                            else break;
                        } while (true);
                        RequestFriend chosenRequest = requests.get(choice3 - 1);
                        System.out.println("do you want to accept this friend request? (y/n)");
                        String choice4;
                        do {
                            choice4 = scanner.nextLine();
                            if (!choice4.equals("y") && !choice4.equals("n")) {
                                System.out.println("invalid input, try again: ");
                            } else
                                break;
                        } while (true);
                        if (choice4.equals("y")) {
                            chosenRequest.setAccept(1);
                        } else {
                            chosenRequest.setAccept(2);
                        }
                        PortableData requestResponse = clientHandler.sendAndReceive(new PortableData("request response", chosenRequest));
                        if (requestResponse.getOrder().equals("200")) {
                            System.out.println("friend request response sent");
                        } else {
                            System.out.println("friend request response not sent, try sending another time.");
                        }

                    }
                }
            }
        }
    }
}
