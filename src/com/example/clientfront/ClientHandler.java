package com.example.clientfront;

import java.io.*;
import java.net.Socket;
import java.util.Objects;


public class ClientHandler extends Thread {
    Socket client;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public ClientHandler(Socket client) {
        this.client = client;
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
                if (Objects.equals(portableData.getOrder(), "registration")) {
                    Client client = (Client) portableData.getObject();
                    PortableData sendResponse = UserQueries.insertNewUserData(client);
                    objectOutputStream.writeObject(sendResponse);
                } else if (Objects.equals(portableData.getOrder(), "login")) {
                    Client client = (Client) portableData.getObject();
                    PortableData sendResponse = UserQueries.checkLogin(client);
                    objectOutputStream.writeObject(sendResponse);
                } else if (Objects.equals(portableData.getOrder(), "findUser")) {
                    Client client = (Client) portableData.getObject();
                    Client client1 = UserQueries.findUserWithUsername(client);
                    if (client1 == null) {
                        objectOutputStream.writeObject(new PortableData("400", null));
                    } else {
                        objectOutputStream.writeObject(new PortableData("200", client1));
                    }
                } else if (Objects.equals(portableData.getOrder(), "private")) {
                    Client client = (Client) portableData.getObject();
                    PortableData sendResponse = PrivateChatQueries.listPrivateChat(client);
                    objectOutputStream.writeObject(sendResponse);
                } else if (Objects.equals(portableData.getOrder(), "new private chat")) {
                    PrivateChat privateChat = (PrivateChat) portableData.getObject();
                    PortableData sendResponse = PrivateChatQueries.newPrivateChat(privateChat);
                    objectOutputStream.writeObject(sendResponse);
                }else if(Objects.equals(portableData.getOrder(), "new server")){
                    ServerDiscord serverDiscord = (ServerDiscord) portableData.getObject();
                    PortableData sendResponse = ServerQueries.insertNewServer(serverDiscord);
                    objectOutputStream.writeObject(sendResponse);
                }else if(Objects.equals(portableData.getOrder(), "new group")){
                    Group group = (Group) portableData.getObject();
                    PortableData sendResponse = GroupQueries.newGroup(group);
                    objectOutputStream.writeObject(sendResponse);
                }else if(Objects.equals(portableData.getOrder(), "new member")){
                    ServerDiscord serverDiscord = (ServerDiscord) portableData.getObject();
                    PortableData sendResponse = ServerQueries.insertNewMemberForServer(serverDiscord);
                    objectOutputStream.writeObject(sendResponse);
                }else if(Objects.equals(portableData.getOrder(), "all servers")){
                    Client client = (Client) portableData.getObject();
                    PortableData sendResponse = ServerQueries.findServerClient(client);
                    objectOutputStream.writeObject(sendResponse);
                }else if(Objects.equals(portableData.getOrder(), "chosen server")) {
                    ServerDiscord serverDiscord = (ServerDiscord) portableData.getObject();
                    PortableData sendResponse = ServerQueries.allInformationServer(serverDiscord);
                    objectOutputStream.writeObject(sendResponse);
                }else if(Objects.equals(portableData.getOrder(), "chosen chat")) {
                    PrivateChat privateChat = (PrivateChat) portableData.getObject();
                    PortableData sendResponse = PrivateChatQueries.findPrivateChatMessage(privateChat);
                    objectOutputStream.writeUnshared(sendResponse);
                 //   System.out.println(((PrivateChat) sendResponse.getObject()).toString());
                }else if(Objects.equals(portableData.getOrder(),"upload profile")){
                    Client client = (Client) portableData.getObject();
                    FileOutputStream fileOutputStream = new FileOutputStream(client.getUsername());
                    fileOutputStream.write(client.getBuffer());
                    client.setProfile("C:\\Users\\Mojtaba\\Desktop\\discord project\\" + client.getUsername());
                    PortableData sendResponse =UserQueries.uploadProfile(client);
                    objectOutputStream.writeUnshared(sendResponse);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}