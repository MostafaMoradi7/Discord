package com.example.clientfront;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ReactionQueries.createTable();
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("server started ...");
            while (true){
                Socket client = serverSocket.accept();
                System.out.println("client connected ...");
                ClientHandler clientHandler = new ClientHandler(client);
                clientHandler.start();
            }
        }catch (IOException  e){
            e.getStackTrace();
        }
    }
}
