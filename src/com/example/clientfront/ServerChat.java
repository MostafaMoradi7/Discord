package com.example.clientfront;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerChat {
    public  static HashMap<Client, clientHandlerChat> clientSocketHashmap =new HashMap<>();
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6001);
            System.out.println("chatting server started ...");
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("client connected for chatting...");
                clientHandlerChat clientHandlerChat = new clientHandlerChat(socket);
                clientHandlerChat.start();
            }
        }catch (IOException e){
            e.getStackTrace();
        }
    }
}
