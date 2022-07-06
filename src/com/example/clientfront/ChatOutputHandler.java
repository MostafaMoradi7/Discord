package com.example.clientfront;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatOutputHandler implements Runnable{
    private Socket clientSocket;
    private Chat chat;
    private ObjectOutputStream writer;
    private PortableData portableData;

    public ChatOutputHandler(Chat chat, Socket clientSocket){
        this.chat = chat;
        this.clientSocket = clientSocket;
        try {
            writer = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPortableData(PortableData portableData) {
        this.portableData = portableData;
    }

    @Override
    public void run() {
        if (portableData != null) {
            try {
                writer.writeObject(portableData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
