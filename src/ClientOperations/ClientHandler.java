package ClientOperations;

import MessageOperations.ChannelMessage;
import MessageOperations.GroupMessage;
import MessageOperations.PrivateChatMessage;
import Services.Channel;
import Services.Group;
import Services.PrivateChat;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
        username = scanner.nextLine();
        System.out.println("*password: ");
        password = scanner.nextLine();
        System.out.println("*email: ");
        email = scanner.nextLine();

        System.out.println("phone number: ");
        phone_Number = scanner.nextLine();
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


        return client;

    }


    @Override
    public void run() {
        System.out.println("""
                [1] Register client
                [2] Login client
                [3] Exit
                """);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        Thread tOUT = new Thread(outputHandler);
        Thread tIN = new Thread(inputHandler);
        tIN.start();

        if (choice == 1) {
            this.client = registerClient();
            PortableData portableData = new PortableData("registration", this.client);
            outputHandler.setPortableData(portableData);
            tOUT.start();

        } else if (choice == 2) {
            scanner.nextLine();
            System.out.println("Enter your Username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            PortableData portableData = new PortableData("login", new Client(username, password, null, null, null));
            outputHandler.setPortableData(portableData);
            tOUT.start();

            if (inputHandler.getPortableData().getOrder().equals("successful")) {
                this.client = (Client) inputHandler.getPortableData().getObject();
                System.out.println("Logged in successfully");
            } else {
                System.out.println("Can not log in, please try again later... !");
            }
        } else {
            shutdown();
        }

        //----------------------------------------WORKING WITH APPLICATION-----------------------------------------\\

        System.out.println("""
                [1] find user
                [2] new private chat
                """);
        choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println("Enter clients username: ");
            scanner.nextLine();
            String username = scanner.nextLine();
            System.out.println("Enter clients email: ");
            String email = scanner.nextLine();
            Client target = new Client(username, null, email, null, null);
            PortableData targetFind = new PortableData("find client", target);
            outputHandler.setPortableData(targetFind);

            tOUT.start();


            if (inputHandler.getPortableData().getOrder().equals("successful")) {
                //TODO: HANDLE WORKING WITH THE FOUNDED CLIENT
            } else {
                System.out.println("NO CLIENT FOUND WITH THIS INFORMATION!");
            }
        } else if (choice == 2) {
            System.out.println("""
                    [1] new private chat
                    [2] chat lists
                    [3] back
                    """);
            choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Enter the username you're looking for:");
                scanner.nextLine();
                String username = scanner.nextLine();
                Client lookingClient = new Client(username, null, null, null, null);
                PortableData portableData = new PortableData("find and return client", lookingClient);
                outputHandler.setPortableData(portableData);
                tOUT.start();
                PortableData receivedData;
                while ((receivedData = inputHandler.getPortableData()) != null) {
                    //TODO: wait till server sends required info
                }
                if (receivedData.getOrder().equals("successful")) {
                    lookingClient = (Client) receivedData.getObject();
                    PrivateChat privateChat = new PrivateChat(this.client, lookingClient);
                    PortableData portableData1 = new PortableData("create a new private chat", privateChat);
                    outputHandler.setPortableData(portableData1);
                    tOUT.start();


                    while ((receivedData = inputHandler.getPortableData()) != null) {
                        //TODO: wait till server sends required info
                    }
                    if (receivedData.getOrder().equals("successful")) {
                        System.out.println("You can find this chat in your chat lists.");
                    } else {
                        System.out.println("Can't create this chat now, please try again later!");
                    }
                } else {
                    System.out.println("NO SUCH CLIENT FOUND!");
                }

            } else if (choice == 2) {
                PortableData portableData = new PortableData("clients private chat lists", this.client);
                outputHandler.setPortableData(portableData);
                tOUT.start();

                PortableData receivedData;
                while ((receivedData = inputHandler.getPortableData()) != null) {
                    //TODO: wait till server sends required info
                }
                /*
                      AN ARRAYLIST OF CLIENTS WILL BE SENT
                        IF THERE IS NO PRIVATE CHAT WITH THIS CLIENT
                        THEN THE SERVER WILL SEND NULL
                                                                        */
                if (receivedData.getOrder().equals("successful")) {
                    ArrayList<PrivateChat> privateChats = (ArrayList<PrivateChat>) receivedData.getObject();
                    if (privateChats.size() == 0) {
                        System.out.println("You have no private chats!");
                    } else {
                        System.out.println("Choose a private chat to work with:");
                        for (int i = 0; i < privateChats.size(); i++) {
                            System.out.println("[" + (i + 1) + "] " + privateChats.get(i).getClientONE().getUsername() + " and " + privateChats.get(i).getClientTWO().getUsername());
                        }
                        choice = scanner.nextInt();
                        if (choice > 0 && choice <= privateChats.size()) {
                            PortableData portableData1 = new PortableData("return private chat", privateChats.get(choice - 1));
                            outputHandler.setPortableData(portableData1);
                            tOUT.start();

                            while ((receivedData = inputHandler.getPortableData()) != null) {
                                //TODO: wait till server sends required info
                            }
                            if (receivedData.getOrder().equals("successful")) {
                                PrivateChat privateChat = (PrivateChat) receivedData.getObject();
                                //QUESTION: HOW TO SHOW NEW MESSAGES?
                                outputHandler.setPvChat(privateChat);
                                tOUT.start();


                            } else {
                                System.out.println("Wrong choice!");
                            }
                        } else {
                            System.out.println("something went wrong! please try again later.");
                            //TODO: HANDLE THE EXCEPTION
                        }

                    }
                }
            }
        }
    }

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
}
