package Chat;

import ClientOperations.*;
import MessageOperations.PrivateChatMessage;
import MessageOperations.TypeMVF;
import Services.PrivateChat;

import java.util.Scanner;

public class PVChatting extends Chat implements Runnable, HandleChat{
    private ClientHandler clientHandler;
    private Client client;
    private PrivateChat pvChat;



    public PVChatting(ClientHandler clientHandler, Client client, PrivateChat pvChat){
        this.clientHandler = clientHandler;
        this.client = client;
        this.pvChat = pvChat;

        chatInputHandler = new ChatInputHandler(this, clientHandler.getClientSocket());
        chatOutputHandler = new ChatOutputHandler(this, clientHandler.getClientSocket());
    }


    @Override
    public void run() {
        System.out.println("PVChatting is running");
        Thread chatInputHandlerThread = new Thread(chatInputHandler);
        Thread chatOutputHandlerThread = new Thread(chatOutputHandler);
        ChatInputHandler.setIsRunning(true);
        chatInputHandlerThread.start();
        System.out.println("Type any message to send: ");
        System.out.println("Type '$back' to go back");
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine();
            if (message.equals("$back")) {
                endChat();
                break;
            }

            PrivateChatMessage privateChatMessage = new PrivateChatMessage(TypeMVF.TEXT, client, pvChat.getClientTWO(), message);
            PortableData portableData = new PortableData("pv Message", privateChatMessage);


            chatOutputHandler.setPortableData(portableData);
            chatOutputHandlerThread.start();

            if(message != null)
                readMessage();
        }
    }


    @Override
    public void readMessage() {
        if (message != null && (((PrivateChatMessage)message).getBody() instanceof String)) {
            System.out.println("**" + ((PrivateChatMessage)message).getTo() + ": " + ((PrivateChatMessage)message).getBody());
            pvChat.addMessage((PrivateChatMessage)message);
            message = null;
        }
    }

    @Override
    public void endChat() {
        ChatInputHandler.setIsRunning(false);
    }
}
