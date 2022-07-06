package com.example.clientfront;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class clientHandlerChat extends Thread {
    boolean firstTime = false;
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
                if (!firstTime) {
                    Client client1 = (Client) portableData.getObject();
                    ServerChat.clientSocketHashmap.put(client1, this);
                    if (client1 != null){
                        firstTime = true;
                    }
                } else {
                if (Objects.equals(portableData.getOrder(), "privateChatMessage")){
                    PrivateChatMessage privateChatMessage = (PrivateChatMessage) portableData.getObject();
                    PrivateChatQueries.insertNewMessagePrivateChat(privateChatMessage);
                    sendMessagePrivateChat(privateChatMessage);
                }else if (Objects.equals(portableData.getOrder(), "groupChatMessage")){

                }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMessagePrivateChat(PrivateChatMessage privateChatMessage){
            clientHandlerChat clientHandlerChat = ServerChat.clientSocketHashmap.get(privateChatMessage.getTo());
            if (clientHandlerChat == null){
                return;
            }else {
                try {
                    clientHandlerChat.objectOutputStream.writeObject(privateChatMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
