package com.example.clientfront;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            clientSocket = new Socket("192.168.43.30", 6001);
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
                    if (newMessage.getOrder().equals("new private message"))
                    System.out.println(newMessage.getObject());
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

}

