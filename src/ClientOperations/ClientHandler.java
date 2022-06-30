package ClientOperations;

import Exceptions.InvalidInput;
import MessageOperations.ChannelMessage;
import MessageOperations.GroupMessage;
import MessageOperations.PrivateChatMessage;
import Services.Group;
import Services.PrivateChat;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClientHandler implements Runnable {

    private Client client;
    private Socket clientSocket;
    private ClientOutputHandler outputHandler;
    private ClientInputHandler inputHandler;
    private PortableData portableData;

    private ArrayList<PrivateChatMessage> pvMessages;
    private HashMap<String, ArrayList<GroupMessage>> gapMessages;
    private HashMap<String, ArrayList<ChannelMessage>> channelsMessages;

    public ClientHandler() {
        try {
            clientSocket = new Socket("192.168.43.30", 6500);
            outputHandler = new ClientOutputHandler(clientSocket, client);
            inputHandler = new ClientInputHandler(this, clientSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setPortableData(PortableData portableData) {
        this.portableData = portableData;
    }


    public Client registerClient() {
        Scanner scanner = new Scanner(System.in);
        String username, password, email, phone_Number;
        Status status;
        System.out.println("Please Enter Required information");
        System.out.println("fields containing * must be completed. if not enter 'null' to skip.");
        System.out.println("*username: ");
        do {
            username = scanner.nextLine();
            if(!(username.length()<6) || !Pattern.matches("[a-zA-Z0-9]+", username)){
                break;
            }else {
                try {
                    throw new InvalidInput("username must be at least 6 characters and only contain letters and numbers");
                } catch (InvalidInput e) {
                    System.out.println("try again: ");
                }
            }
        }while (true);
        System.out.println("*password: ");
        password = scanner.nextLine();

        // TODO: check if email is valid
        System.out.println("*phone_Number: ");
        do {
            phone_Number = scanner.nextLine();
            if(!(phone_Number.length()<10) || !Pattern.matches("[0-9]+", phone_Number))
                break;
            else
                System.out.println("try again: ");
        } while (true);
        // USING REGEX IN ORDER TO VERIFY IF THE PHONE NUMBER IS VALID
        do {
            System.out.println("*email: ");
            email = scanner.nextLine();
        } while (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email));
        // USING REGEX IN ORDER TO VERIFY IF THE EMAIL IS VALID

        System.out.println("what kind of status do you prefer?");
        System.out.println("""
                [1] ONLINE
                [2] IDLE
                [3] DO_NOT_DISTURB
                [4] INVISIBLE
                [5] SKIP""");
        int choice = scanner.nextInt();
        if (choice == 1) {
            status = Status.ONLINE;
        } else if (choice == 2) {
            status = Status.IDLE;
        } else if (choice == 3) {
            status = Status.DO_NOT_DISTURB;
        } else if (choice == 4) {
            status = Status.INVISIBLE;
        } else
            status = null;
        client = new Client(username, password, email, phone_Number, status);
        ClientOutputHandler outputHandler = new ClientOutputHandler(clientSocket, client);
        ClientInputHandler inputHandler = new ClientInputHandler(this, clientSocket);
        PortableData portableData = new PortableData("register", client);
        outputHandler.setPortableData(portableData);
        this.outputHandler = null;
        Thread outThread = new Thread(outputHandler);
        outThread.start();
        Thread inThread = new Thread(inputHandler);
        inThread.start();
        PortableData response = null;
        while ((response = inputHandler.getPortableData()) == null){
            // wait till respond is sent
        }
        if (response.getOrder().equals("successful"))
            return this.client = (Client) response.getObject();
        else
            return null;
    }


    /**
     * the method that runs the client login service
     * @return the client object
     */
    public Client loginClient() {
        Thread inputThread = new Thread(inputHandler);
        Thread outputThread = new Thread(outputHandler);
        Scanner scanner = new Scanner(System.in);
        String username, password;
        System.out.println("Please Enter Required information");
        System.out.println("fields containing * must be completed. if not enter 'null' to skip.");
        System.out.println("*username: ");
        do {
            username = scanner.nextLine();
            if (username.trim().isEmpty() && Pattern.matches("^[a-zA-Z0-9]*$", username) == false) {
                System.out.println("please enter a valid username!");
            }else{
                break;
            }
        }while (true);
        System.out.println("*password: ");
        do {
            password = scanner.nextLine();
            if (password.trim().isEmpty() && Pattern.matches("^[a-zA-Z0-9]*$", password) == false) {
                System.out.println("please enter a valid password!");
            }else{
                break;
            }
        }while (true);

        Client client = new Client(username, password, null, null, null);

        PortableData portableData = new PortableData("login", client);

        outputHandler.setPortableData(portableData);
        outputThread.start();
        inputThread.start();

        System.out.println("waiting for server response...");
        PortableData receivedData;
        while ((receivedData = inputHandler.getPortableData()) == null) {
            try {

                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ClientInputHandler.setIsRunning(false);

        if (!receivedData.getOrder().equals("successful")){
            System.out.println("login failed");
            shutdown();
            //TODO: BETTER TO HAVE AN EXCEPTION HERE.
            return null;
        }else{
            System.out.println("login successful");
            return this.client = (Client) receivedData.getObject();

        }
    }

    public Client findClient(){
        Scanner scanner = new Scanner(System.in);
        Thread outputThread = new Thread(outputHandler);
        Thread inputThread = new Thread(inputHandler);
        System.out.println("Please Enter Required information");
        System.out.println("clients username: (must be filled!)");
        String username;
        do {
            username = scanner.nextLine();
            if (username.trim().isEmpty() && Pattern.matches("^[a-zA-Z0-9]*$", username) == false) {
                System.out.println("please enter a valid username!");
            }else{
                break;
            }
        }while(true);
        Client client = new Client(username, null, null, null, null);
        // DATA IS SENT TO THE SERVER TO FIND THE CLIENT
        PortableData portableData = new PortableData("find client", client);
        outputHandler.setPortableData(portableData);
        outputThread.start();

        //MAKING THE ClientInputHandler ABLE TO RECEIVE THE DATA
        ClientInputHandler.setIsRunning(true);
        inputThread.start();

        System.out.println("waiting for server response...");
        //WAITING FOR THE SERVER TO SEND THE DATA
        PortableData receivedData;
        while ((receivedData = inputHandler.getPortableData()) == null) {
            try {

                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (receivedData.getOrder().equals("unsuccessful")) {
            System.out.println("no client found");
            shutdown();
            return null;
        }

        System.out.println("client found");
        return (Client) receivedData.getObject();
    }

    public void createServer(){

    }

    @Override
    public void run(){

    }

//    @Override
//    public void run() {
//        //---------------------------------THE RECEIVER OF THE MESSAGES AND SENDER ARE GETTING STARTED---------------------------------\\
//        Thread tOUT = new Thread(outputHandler);
//        Thread tIN = new Thread(inputHandler);
//        //----------------------------------------REGISTRATION AND LOG IN-----------------------------------------\\
//        System.out.println("""
//                [1] Register client
//                [2] Login client
//                [3] Exit
//                """);
//        Scanner scanner = new Scanner(System.in);
//        int choice = scanner.nextInt();
//
//        tIN.start();
//
//        if (choice == 1) {
//            this.client = registerClient();
//            PortableData portableData = new PortableData("registration", this.client);
//            outputHandler.setPortableData(portableData);
//            tOUT.start();
//
//        } else if (choice == 2) {
//            scanner.nextLine();
//            System.out.println("Enter your Username: ");
//            String username = scanner.nextLine();
//            System.out.println("Enter your password: ");
//            String password = scanner.nextLine();
//            PortableData portableData = new PortableData("login", new Client(username, password, null, null, null));
//            outputHandler.setPortableData(portableData);
//            tOUT.start();
//
//            if (inputHandler.getPortableData().getOrder().equals("successful")) {
//                this.client = (Client) inputHandler.getPortableData().getObject();
//                System.out.println("Logged in successfully");
//            } else {
//                System.out.println("Can not log in, please try again later... !");
//            }
//        } else {
//            shutdown();
//        }
//
//        //----------------------------------------WORKING WITH APPLICATION-----------------------------------------\\
//
//        System.out.println("""
//                [1] find user
//                [2] new private chat
//                [3] group chat
//                [4] channels
//                [5] logout
//                """);
//        choice = scanner.nextInt();
//        //-----------------------------------------------CHOICE #1-----------------------------------------------\\
//        // JUST WITH A SINGLE USERNAME AND AN OPTIONAL EMAIL THE CLIENT WILL BE FOUND BY SERVER.
//        if (choice == 1) {
//            System.out.println("Enter clients username: ");
//            scanner.nextLine();
//            String username = scanner.nextLine();
//            System.out.println("Enter clients email: ");
//            String email = scanner.nextLine();
//            Client target = new Client(username, null, email, null, null);
//            PortableData targetFind = new PortableData("find client", target);
//            outputHandler.setPortableData(targetFind);
//
//            tOUT.start();
//
//
//            if (inputHandler.getPortableData().getOrder().equals("successful")) {
//                //TODO: HANDLE WORKING WITH THE FOUNDED CLIENT
//            } else {
//                System.out.println("NO CLIENT FOUND WITH THIS INFORMATION!");
//            }
//        }
//        //-----------------------------------------------CHOICE #2-----------------------------------------------\\
//        else if (choice == 2) {
//            System.out.println("""
//                    [1] new private chat
//                    [2] chat lists
//                    [3] back
//                    """);
//            choice = scanner.nextInt();
//            if (choice == 1) {
//                System.out.println("Enter the username you're looking for:");
//                scanner.nextLine();
//                String username = scanner.nextLine();
//                Client lookingClient = new Client(username, null, null, null, null);
//                PortableData portableData = new PortableData("find and return client", lookingClient);
//                outputHandler.setPortableData(portableData);
//                tOUT.start();
//                PortableData receivedData;
//                while ((receivedData = inputHandler.getPortableData()) != null) {
//                    //TODO: wait till server sends required info
//                }
//                if (receivedData.getOrder().equals("successful")) {
//                    lookingClient = (Client) receivedData.getObject();
//                    PrivateChat privateChat = new PrivateChat(this.client, lookingClient);
//                    PortableData portableData1 = new PortableData("create a new private chat", privateChat);
//                    outputHandler.setPortableData(portableData1);
//                    tOUT.start();
//
//
//                    while ((receivedData = inputHandler.getPortableData()) != null) {
//                        //TODO: wait till server sends required info
//                    }
//                    if (receivedData.getOrder().equals("successful")) {
//                        System.out.println("You can find this chat in your chat lists.");
//                    } else {
//                        System.out.println("Can't create this chat now, please try again later!");
//                    }
//                } else {
//                    System.out.println("NO SUCH CLIENT FOUND!");
//                }
//
//            } else if (choice == 2) {
//                PortableData portableData = new PortableData("clients private chat lists", this.client);
//                outputHandler.setPortableData(portableData);
//                tOUT.start();
//
//                PortableData receivedData;
//                while ((receivedData = inputHandler.getPortableData()) != null) {
//                    //TODO: wait till server sends required info
//                }
//                /*
//                      AN ARRAYLIST OF CLIENTS WILL BE SENT
//                        IF THERE IS NO PRIVATE CHAT WITH THIS CLIENT
//                        THEN THE SERVER WILL SEND NULL
//                                                                        */
//                if (receivedData.getOrder().equals("successful")) {
//                    ArrayList<PrivateChat> privateChats = (ArrayList<PrivateChat>) receivedData.getObject();
//                    if (privateChats.size() == 0) {
//                        System.out.println("You have no private chats!");
//                    } else {
//                        System.out.println("Choose a private chat to work with:");
//                        for (int i = 0; i < privateChats.size(); i++) {
//                            System.out.println("[" + (i + 1) + "] " + privateChats.get(i).getClientONE().getUsername() + " and " + privateChats.get(i).getClientTWO().getUsername());
//                        }
//                        choice = scanner.nextInt();
//                        if (choice > 0 && choice <= privateChats.size()) {
//                            PortableData portableData1 = new PortableData("return private chat", privateChats.get(choice - 1));
//                            outputHandler.setPortableData(portableData1);
//                            tOUT.start();
//
//                            while ((receivedData = inputHandler.getPortableData()) != null) {
//                                //TODO: wait till server sends required info
//                            }
//                            if (receivedData.getOrder().equals("successful")) {
//                                PrivateChat privateChat = (PrivateChat) receivedData.getObject();
//                                //QUESTION: HOW TO SHOW NEW MESSAGES?
//                                outputHandler.setPvChat(privateChat);
//                                tOUT.start();
//
//
//                            } else {
//                                System.out.println("Wrong choice!");
//                            }
//                        } else {
//                            System.out.println("something went wrong! please try again later.");
//                            //TODO: HANDLE THE EXCEPTION
//                        }
//
//                    }
//                }
//            }
//        }
//        //-----------------------------------------------CHOICE #3-----------------------------------------------\\
//        else if (choice == 3) {
//            System.out.println("""
//                    [1] new group chat
//                    [2] chat lists
//                    [3] back
//                    """);
//            choice = scanner.nextInt();
//
//            if (choice == 1) {
//                System.out.println("Enter group name: ");
//                scanner.nextLine();
//                String groupName = scanner.nextLine();
//                Group group = new Group(groupName, this.client);
//                PortableData portableData = new PortableData("create a new group", group);
//                outputHandler.setPortableData(portableData);
//                tOUT.start();
//
//                PortableData receivedData;
//                while ((receivedData = inputHandler.getPortableData()) != null) {
//                    // TODO: wait till server sends required info
//                }
//                if (receivedData.getOrder().equals("successful")) {
//                    System.out.println("You can find this group in your Group Chat lists.");
//                } else {
//                    System.out.println("Can't create this group now, please try again later!");
//                }
//            } else if (choice == 2) {
//                PortableData portableData = new PortableData("clients group chat lists", this.client);
//                outputHandler.setPortableData(portableData);
//                tOUT.start();
//
//                PortableData receivedData;
//                while ((receivedData = inputHandler.getPortableData()) != null) {
//                    //TODO: wait till server sends required info
//                }
//                /*
//                      AN ARRAYLIST OF Groups WILL BE SENT
//                        IF THERE IS NO Group CHAT WITH THIS CLIENT
//                        THEN THE SERVER WILL SEND NULL
//                                                                        */
//                if (receivedData.getOrder().equals("successful")) {
//                    ArrayList<Group> groups = (ArrayList<Group>) receivedData.getObject();
//                    if (groups.size() == 0) {
//                        System.out.println("You have no group chats!");
//                    } else {
//                        System.out.println("Choose a group chat to work with:");
//                        for (int i = 0; i < groups.size(); i++) {
//                            System.out.println("[" + (i + 1) + "] " + groups.get(i).getGroupName());
//                        }
//                        choice = scanner.nextInt();
//                        if (choice > 0 && choice <= groups.size()) {
//                            PortableData portableData1 = new PortableData("return group chat", groups.get(choice - 1));
//                            outputHandler.setPortableData(portableData1);
//                            tOUT.start();
//
//                            while ((receivedData = inputHandler.getPortableData()) != null) {
//                                //TODO: wait till server sends required info
//                            }
//                            if (receivedData.getOrder().equals("successful")) {
//                                Group group = (Group) receivedData.getObject();
//                                //QUESTION: HOW TO SHOW NEW MESSAGES?
//                                outputHandler.setGapChat(group);
//                                tOUT.start();
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    /**
     *SHUTS DOWN THE WHOLE SYSTEM FOR CLIENT
     */
    public void shutdown(){
        try {
            if (!clientSocket.isClosed())
                clientSocket.close();
            inputHandler.shutdownIN();
            outputHandler.shutdownOUT();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void receivePVMessage(PortableData newMessage){
        PrivateChatMessage pvMessage = (PrivateChatMessage) newMessage.getObject();
        pvMessages.add(pvMessage);
    }

    public void receiveChannelMessage(PortableData newMessage){
        ChannelMessage channelMessage = (ChannelMessage) newMessage.getObject();
        channelsMessages.get(channelMessage.getChannelName()).add(channelMessage);
    }

    public void receiveGapMessage(PortableData newMessage){
        GroupMessage groupMessage = (GroupMessage) newMessage.getObject();
        gapMessages.get(groupMessage);
    }

    public Client returnMainClient(){
        return this.client;
    }
}
