package com.example.clientfront;

import java.io.*;
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
                    sendMessagePrivateChat(privateChatMessage);
                }else if (Objects.equals(portableData.getOrder(), "send private file")) {
                    PrivateChatMessage privateChatMessage = (PrivateChatMessage) portableData.getObject();
                    FileOutputStream fileOutputStream = new FileOutputStream(privateChatMessage.getMessage());
                    fileOutputStream.write(privateChatMessage.getBuffer());
                    String name = privateChatMessage.getMessage();
                    privateChatMessage.setMessage("C:\\Users\\Mojtaba\\Desktop\\discord project\\" + name );
                    PrivateChatQueries.insertNewMessagePrivateChat(privateChatMessage);
                    sendMessagePrivateChat(privateChatMessage);
                } else if (Objects.equals(portableData.getOrder(), "groupChatMessage")) {
                    GroupMessage groupMessage = (GroupMessage) portableData.getObject();
                    GroupQueries.insertNewGroupMessage(groupMessage);

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
                if (Objects.equals(privateChatMessage.getType().toString(), "FILE")){
                    byte[] buffer;
                    FileInputStream fileInputStream = new FileInputStream(privateChatMessage.getMessage());
                    File file = new File(privateChatMessage.getMessage());
                    buffer = fileInputStream.readAllBytes();
                    privateChatMessage.setBuffer(buffer);
                    privateChatMessage.setMessage(file.getName());
                    clientHandlerChat.objectOutputStream.writeObject(new PortableData("new private message", privateChatMessage));
                }else{
                    clientHandlerChat.objectOutputStream.writeObject(new PortableData("new private message", privateChatMessage));
                    System.out.println("finish");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMessageGroup(GroupMessage groupMessage) {
        for (int i=0 ; i<groupMessage.getClients().size() ; i++){
            clientHandlerChat clientHandlerChat = ServerChat.clientSocketHashmap.get(groupMessage.getClients().get(i).getClientID());
            if (clientHandlerChat == null) {
            } else {
                try {
//                    if (Objects.equals(groupMessage.getType().toString(), "FILE")){
//                        byte[] buffer;
//                        FileInputStream fileInputStream = new FileInputStream(privateChatMessage.getMessage());
//                        File file = new File(privateChatMessage.getMessage());
//                        buffer = fileInputStream.readAllBytes();
//                        privateChatMessage.setBuffer(buffer);
//                        privateChatMessage.setMessage(file.getName());
//                        clientHandlerChat.objectOutputStream.writeObject(new PortableData("new private message", privateChatMessage));}
//                        else{
                        clientHandlerChat.objectOutputStream.writeObject(new PortableData("new private message", groupMessage));
                        System.out.println("done");
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
