package ClientOperations;

import MessageOperations.PrivateChatMessage;
import MessageOperations.TypeMVF;
import Services.Channel;
import Services.Group;
import Services.PrivateChat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientOutputHandler implements Runnable{
    private Socket clientSocket;
    private Client client;
    private ObjectOutputStream writer;
    private PortableData portableData;
    private PrivateChat pvChat;
    private Group gapChat;
    private Channel channelChat;

    public ClientOutputHandler(Socket clientSocket, Client client){
        this.client = client;
        this.clientSocket = clientSocket;
        try {
            writer = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        pvChat = null;
        gapChat = null;
        channelChat = null;
    }

    public void setPortableData(PortableData portableData) {
        this.portableData = portableData;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            if (portableData != null) {
                writer.writeObject(portableData);
            }

            if (pvChat != null) {
                System.out.println("Enter your message: (enter $quit to finish)");
                System.out.println("to exit the chat, enter '$quitChat'");
                while (pvChat != null) {
                    String message = scanner.nextLine();
                    while (!message.contains("$quit")) {
                        message = message.concat(scanner.nextLine());
                    }
                    PrivateChatMessage privateChatMessage = new PrivateChatMessage(TypeMVF.TEXT,client, pvChat.getClientTWO(), message);
                    PortableData portableData = new PortableData("PV Message", privateChatMessage);
                    writer.writeObject(portableData);


                    if (message.contains("$quitChat")) {
                        pvChat = null;
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void shutdownOUT() {
        try {
            writer.close();
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPvChat(PrivateChat pvChat) {
        this.pvChat = pvChat;
    }

    public void setGapChat(Group gapChat) {
        this.gapChat = gapChat;
    }

    public void setChannelChat(Channel channelChat) {
        this.channelChat = channelChat;
    }

}
