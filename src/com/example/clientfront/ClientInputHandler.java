package com.example.clientfront;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientInputHandler{
    private Socket clientSocket;
    private ObjectInputStream reader;
    private PortableData portableData;

    public ClientInputHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
        try {
            reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void connect(){
        try {
            portableData = (PortableData) reader.readObject();
            if (portableData.getObject() instanceof ServerDiscord)
                System.out.println(portableData.getObject());
            else if (portableData.getObject() instanceof PrivateChatMessage pvm)
                System.out.println(pvm);
            else if (portableData.getObject() instanceof GroupMessage gm)
                System.out.println(gm);
            else if (portableData.getObject() instanceof PrivateChat){
                System.out.println("recent Messages: ");
                for (PrivateChatMessage pvm : ((PrivateChat) portableData.getObject()).getMessages())
                    System.out.println(pvm);
            }



        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shutdownIN() {
        try {
            reader.close();
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PortableData getPortableData(){
        PortableData tmpData = portableData;
        portableData = null;
        return tmpData;

    }
}
