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

public class ClientHandler implements Runnable{

    private Client client;
    private Socket clientSocket;
    private ClientOutputHandler outputHandler;
    private ClientInputHandler inputHandler;
    private PortableData portableData;

    private ArrayList<PrivateChatMessage> pvMessages;
    private HashMap<String, ArrayList<GroupMessage>> gapMessages;
    private HashMap<String, ArrayList<ChannelMessage>> channelsMessages;

    public ClientHandler(){
        try {
            clientSocket = new Socket("192.168.43.30", 6500);
            outputHandler = new ClientOutputHandler(clientSocket);
            inputHandler = new ClientInputHandler(this, clientSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPortableData(PortableData portableData){
        this.portableData = portableData;
    }


    public Client registerClient(){
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
        }else if (choice == 2){
            status = Status.IDLE;
        }else if (choice == 3){
            status = Status.DO_NOT_DISTURB;
        }else if (choice == 4){
            status = Status.INVISIBLE;
        }else
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

        if (choice == 1){
            this.client = registerClient();
            PortableData portableData = new PortableData("registration", this.client);
            outputHandler.setPortableData(portableData);
            tOUT.start();


            tIN.start();
        }else if (choice == 2){
            scanner.nextLine();
            System.out.println("Enter your Username: ");
            String username = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            PortableData portableData = new PortableData("login", new Client(username, password, null, null, null));
            outputHandler.setPortableData(portableData);
            tOUT.start();

            tIN.start();
            if (inputHandler.getPortableData().getOrder().equals("successful")) {
                this.client = (Client) inputHandler.getPortableData().getObject();
                System.out.println("Logged in successfully");
            }else {
                System.out.println("Can not log in, please try again later... !");
            }
        }else {
            shutdown();
        }

        //--------------------------------------------------------------------------------------\\

        System.out.println("""
                [1] find user
                [2] new private chat
                """);
        choice = scanner.nextInt();
        if (choice == 1){
            System.out.println("Enter clients username: ");
            scanner.nextLine();
            String username = scanner.nextLine();
            System.out.println("Enter clients email: ");
            String email = scanner.nextLine();
            Client target = new Client(username, null, email, null, null);
            PortableData targetFind = new PortableData("find client", target);
            outputHandler.setPortableData(targetFind);

            tOUT.start();

            tIN.start();

            if (inputHandler.getPortableData().getOrder().equals("successful")){
                //TODO: HANDLE WORKING WITH THE FOUNDED CLIENT
            }else{
                System.out.println("NI CLIENT FOUND WITH THIS INFORMATION!");
            }
        }else if (choice == 2){

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
