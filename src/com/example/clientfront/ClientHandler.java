package com.example.clientfront;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClientHandler {

    private Client client;
    private Socket clientSocket;
    private ClientOutputHandler outputHandler;
    private ClientInputHandler inputHandler;
    private PortableData portableData;


    public ClientHandler() {
        try {
            clientSocket = new Socket("localhost", 6000);
            outputHandler = new ClientOutputHandler(clientSocket, client);
            inputHandler = new ClientInputHandler(clientSocket);

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

    public void setClient(Client client) {
        this.client = client;
    }


    public Client registerClient() {
        Scanner scanner = new Scanner(System.in);
        String username, password, email, phone_Number;
        Status status;
        System.out.println("Please Enter Required information; fields containing * must be completed. if not enter 'null' to skip.");
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
        System.out.println("Please Enter Required information; fields containing * must be completed. if not enter 'null' to skip.");
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
            return this.client;

        }
    }

    public Client findClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Required information");
        System.out.println("clients username: (must be filled!)");
        String username;
        do {
            username = scanner.nextLine();
            if (username.trim().isEmpty() && Pattern.matches("^[a-zA-Z0-9]*$", username) == false) {
                System.out.println("please enter a valid username!");
            } else {
                break;
            }
        } while (true);
        Client client = new Client(username, null, null, null, null);
        // DATA IS SENT TO THE SERVER TO FIND THE CLIENT
        PortableData portableData = new PortableData("findUser", client);
        PortableData receivedData = sendAndReceive(portableData);

        if (receivedData.getOrder().equals("unsuccessful")) {
            System.out.println("no client found");
            return null;
        } else {
            System.out.println("client found");
            return (Client) receivedData.getObject();
        }
    }


    public Client returnMainClient(){
        return this.client;
    }


    public PortableData sendAndReceive(PortableData portableData){
        outputHandler.setPortableData(portableData);
        System.out.println("waiting for server response...");
        PortableData response;
        outputHandler.connect();
        inputHandler.connect();

        response = inputHandler.getPortableData();
        return response;
    }


}
