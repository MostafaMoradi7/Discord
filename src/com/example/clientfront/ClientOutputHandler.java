package com.example.clientfront;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientOutputHandler{
    private Socket clientSocket;
    private Client client;
    private ObjectOutputStream writer;
    private PortableData portableData;

    public ClientOutputHandler(Socket clientSocket, Client client){
        this.client = client;
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


    public void connect() {
        Scanner scanner = new Scanner(System.in);
        try {
            if (portableData != null) {
                writer.writeObject(portableData);
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
