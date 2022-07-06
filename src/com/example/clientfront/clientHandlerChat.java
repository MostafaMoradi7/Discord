package com.example.clientfront;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class clientHandlerChat extends Thread{
    Socket client;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    public clientHandlerChat(Socket client) {
        this.client = client;
        try {
            objectInputStream = new ObjectInputStream(client.getInputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
