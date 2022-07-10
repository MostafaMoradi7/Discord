package com.example.clientfront;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;


public class ClientHandlerChat implements Runnable {

    private Client client;
    private Socket clientSocket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
//    private ClientOutputHandler outputHandler;
//    private ClientInputHandler inputHandler;

    private ArrayList<PrivateChatMessage> pvMessages;
    private HashMap<String, ArrayList<GroupMessage>> gapMessages;
    private HashMap<String, ArrayList<ChannelMessage>> channelsMessages;

    public ClientHandlerChat() {
        try {
            clientSocket = new Socket("localhost", 6001);
            writer = new ObjectOutputStream(clientSocket.getOutputStream());
            reader = new ObjectInputStream(clientSocket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    @Override
    public void run() {
        while (true){
            PortableData newMessage;
            try {
                if ((newMessage = (PortableData) reader.readObject()) != null){
                    if (newMessage.getOrder().equals("new private message")) {
                        PrivateChatMessage message = (PrivateChatMessage) newMessage.getObject();
                        if(message.getType().name().equals("TEXT")){
                            System.out.println(message.getSender().getUsername() + ": " + message.getMessage());
                        }else {
                            System.out.println(message.getSender() + ": " + message.getMessage());
                            System.out.println("do you want to download this file? (y/n)");
                            Scanner scanner = new Scanner(System.in);
                            String answer = scanner.nextLine();
                            if(answer.equals("y")){
                                System.out.println("downloading file...");
                                File file = new File("C:\\Users\\User\\Desktop\\" + message.getMessage());
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(message.getBuffer());
                                fos.close();
                                System.out.println("file downloaded");
                            }else if (answer.equals("n")) {
                                System.out.println("file not downloaded");
                            }else {
                                System.out.println("invalid answer, file not downloaded");
                            }

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(PortableData portableData){
        try {
            writer.writeObject(portableData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Client returnMainClient() {
        return this.client;
    }

    public void sentFile(String path) throws IOException {
        byte[] buffer = new byte[Integer.MAX_VALUE];
        FileInputStream file = new FileInputStream(path);
        buffer = file.readAllBytes();
        PortableData portableData = new PortableData("file", buffer);
    }

}

