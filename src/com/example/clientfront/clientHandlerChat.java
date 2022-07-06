package com.example.clientfront;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class clientHandlerChat extends Thread {
    Socket socket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public clientHandlerChat(Socket client) {
        this.socket = client;
        try {
            objectInputStream = new ObjectInputStream(client.getInputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            while (true) {
                PortableData portableData = (PortableData) objectInputStream.readObject();
                if (Objects.equals(portableData.getOrder(), "main client")) {
                    Client client1 = (Client) portableData.getObject();
                    ServerChat.clientSocketHashmap.put(client1.getClientID(), this);
                    System.out.println(ServerChat.clientSocketHashmap);
                } else if (Objects.equals(portableData.getOrder(), "private chat message")) {
                    PrivateChatMessage privateChatMessage = (PrivateChatMessage) portableData.getObject();
                    PrivateChatQueries.insertNewMessagePrivateChat(privateChatMessage);
                    System.out.println(privateChatMessage);
                    sendMessagePrivateChat(privateChatMessage);
                } else if (Objects.equals(portableData.getOrder(), "groupChatMessage")) {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessagePrivateChat(PrivateChatMessage privateChatMessage) {
        clientHandlerChat clientHandlerChat = ServerChat.clientSocketHashmap.get(privateChatMessage.getReceiver().getClientID());
        if (clientHandlerChat == null) {
        } else {
            try {
                clientHandlerChat.objectOutputStream.writeObject(new PortableData("new private message", privateChatMessage));
                System.out.println("finish");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
