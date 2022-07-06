package com.example.clientfront;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerChat {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6001);
            System.out.println("server started ...");
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("client connected ...");
                clientHandlerChat clientHandlerChat = new clientHandlerChat(socket);
                clientHandlerChat.start();
            }
        }catch (IOException e){
            e.getStackTrace();
        }
    }
}
