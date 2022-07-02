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
            clientSocket = new Socket("192.168.43.42", 6000);
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
            status = Status.ONLINE;;
        client = new Client(username, password, email, phone_Number, status);
        PortableData portableData = new PortableData("registration", client);
        PortableData response = sendAndReceive(portableData);
        if (response.getOrder().equals("200")) {
            System.out.println("nfdsfdsfs");
            return this.client = (Client) response.getObject();
        }
        else
            return null;
    }


    /**
     * the method that runs the client login service
     * @return the client object
     */
    public Client loginClient() {
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

        PortableData response = sendAndReceive(portableData);

        if (!response.getOrder().equals("200")){
            System.out.println("login failed");
            //TODO: BETTER TO HAVE AN EXCEPTION HERE.
            return null;
        }else{
            System.out.println("login successful");
            this.client = (Client) response.getObject();
            System.out.println("your client id is: " + this.client.getClientID());
            return this.client;

        }
    }

    public Client findClient(){
        Scanner scanner = new Scanner(System.in);
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
        PortableData receivedData = sendAndReceive(portableData);

        if (receivedData.getOrder().equals("unsuccessful")) {
            System.out.println("no client found");
            return null;
        }

        System.out.println("client found");
        this.client = (Client) receivedData.getObject();
        return (Client) receivedData.getObject();
    }

    public void createServer(){

    }

    @Override
    public void run(){

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

    public void receiveAMessage(PortableData newMessage){
        this.portableData = newMessage;
    }

    public PortableData sendAndReceive(PortableData portableData){
        outputHandler.setPortableData(portableData);
//        Thread outThread = new Thread(outputHandler);
//        outThread.start();
        Thread inThread = new Thread(inputHandler);
        PortableData response;
        outputHandler.run();
        inputHandler.run();
        System.out.println("waiting for server response...");
        while ((response = inputHandler.getPortableData()) == null){
            // wait till respond is sent
        }
        return response;
    }
}
